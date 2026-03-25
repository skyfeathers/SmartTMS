package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillVoucherDao;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillPathForm;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillPathUpdateForm;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillPathManager;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillPathEntity;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.baidumap.BaiduMapService;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/12/15 5:06 下午
 */
@Service
public class WaybillPathService {

    @Autowired
    private WaybillPathManager waybillPathManager;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillVoucherDao waybillVoucherDao;
    @Autowired
    private WaybillDataTracerService waybillDataTracerService;
    @Autowired
    private BaiduMapService baiduMapService;

    /**
     * 运单路线更新
     *
     * @param waybillPathUpdateForm
     * @return
     */
    public ResponseDTO<String> waybillPathUpdate(WaybillPathUpdateForm waybillPathUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = waybillPathUpdateForm.getWaybillId();

        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        if (CollectionUtils.isEmpty(waybillPathUpdateForm.getPathList())) {
            return ResponseDTO.userErrorParam("运单路线不能为空");
        }

        WaybillEntity updateWaybillEntity = new WaybillEntity();
        updateWaybillEntity.setWaybillId(waybillEntity.getWaybillId());
        updateWaybillEntity.setDistance(waybillPathUpdateForm.getDistance());

        List<WaybillPathEntity> pathList = SmartBeanUtil.copyList(waybillPathUpdateForm.getPathList(), WaybillPathEntity.class);
        WaybillPathService waybillPathService = (WaybillPathService) AopContext.currentProxy();
        waybillPathService.updateWaybillPath(updateWaybillEntity, pathList);

        // 新增操作记录
        waybillDataTracerService.updatePathLog(waybillPathUpdateForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateWaybillPath(WaybillEntity updateWaybillEntity, List<WaybillPathEntity> pathList) {
        waybillDao.updateById(updateWaybillEntity);

        Long waybillId = updateWaybillEntity.getWaybillId();
        waybillPathManager.getBaseMapper().deleteByWaybillId(waybillId);
        pathList.forEach(e -> {
            String address = e.getProvinceName() + e.getCityName() + e.getDistrictName() + e.getAddress();
            Map<String, String> latLngMap = baiduMapService.getLocationLatLng(address);
            BigDecimal lat = SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.getOrDefault(BaiduMapService.LAT, "0")), 6);
            BigDecimal lng = SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.getOrDefault(BaiduMapService.LNG, "0")), 6);
            e.setLatitude(lat);
            e.setLongitude(lng);
            e.setWaybillId(waybillId);
        });
        waybillPathManager.saveBatch(pathList);

        List<WaybillVoucherVO> waybillVoucherList = waybillVoucherDao.selectByWaybillId(waybillId);
        if (CollectionUtils.isEmpty(waybillVoucherList)) {
            return;
        }
    }

    /**
     * 获取提箱、装货第一个地址
     *
     * @param waybillPathEntityList
     * @return
     */
    public WaybillPathEntity getStartPath(List<WaybillPathEntity> waybillPathEntityList) {
        if (CollectionUtils.isEmpty(waybillPathEntityList)) {
            return null;
        }
        waybillPathEntityList = waybillPathEntityList.stream().filter(e -> PathTypeEnum.CONTAINER_LOCATION.equalsValue(e.getType()) || PathTypeEnum.PLACING_LOADING.equalsValue(e.getType())).collect(Collectors.toList());
        return waybillPathEntityList.get(0);
    }

    /**
     * 获取还箱、卸货货最后一个地址
     *
     * @param waybillPathEntityList
     * @return
     */
    public WaybillPathEntity getEndPath(List<WaybillPathEntity> waybillPathEntityList) {
        if (CollectionUtils.isEmpty(waybillPathEntityList)) {
            return null;
        }
        waybillPathEntityList = waybillPathEntityList.stream().filter(e -> PathTypeEnum.RETURN_CONTAINER_LOCATION.equalsValue(e.getType()) || PathTypeEnum.UNLOADING_LOCATION.equalsValue(e.getType())).collect(Collectors.toList());
        return waybillPathEntityList.get(waybillPathEntityList.size() - 1);
    }

    /**
     * 获取地址明细 省市区详细地址
     * @param pathEntity
     * @return
     */
    public String getProvinceCityAddress(WaybillPathEntity pathEntity) {
        if (pathEntity == null) {
            return "";
        }
        String mailArea = "";
        if (StringUtils.isNotBlank(pathEntity.getProvinceName())) {
            mailArea = mailArea + pathEntity.getProvinceName();
        }
        if (StringUtils.isNotBlank(pathEntity.getCityName())) {
            mailArea = mailArea + pathEntity.getCityName();
        }
        return mailArea;
    }
}