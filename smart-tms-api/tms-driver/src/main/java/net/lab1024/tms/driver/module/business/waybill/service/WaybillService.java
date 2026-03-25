package net.lab1024.tms.driver.module.business.waybill.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillVoucherEntity;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillVoucherDao;
import net.lab1024.tms.driver.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillCountVO;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillDetailVO;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class WaybillService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private waybillDetailService waybillDetailService;
    @Resource
    private WaybillVoucherDao waybillVoucherDao;
    @Resource
    private WaybillDataTracerService waybillDataTracerService;
    @Resource
    private WaybillManager waybillManager;
    @Resource
    private DriverDao driverDao;
    
    /**
     * 分页查询运单
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<WaybillVO>> selectWaybillPage(WaybillQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        DriverEntity driverEntity = driverDao.selectById(queryForm.getDriverId());
        if (driverEntity == null || driverEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("司机账号不存在");
        }
        List<WaybillVO> waybillVOList = waybillDao.selectWaybillPage(page, queryForm);
        // 构建车辆、车队、货主信息
        waybillDetailService.buildVehicleFleetShipper(waybillVOList);
        PageResult<WaybillVO> pageResult = SmartPageUtil.convert2PageResult(page, waybillVOList);
        return ResponseDTO.ok(pageResult);
    }
    
    /**
     * 运单详情
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<WaybillDetailVO> selectWaybillDetail(Long waybillId) {
        WaybillDetailVO waybillDetailVO = waybillDetailService.selectWaybillDetail(waybillId);
        if (ObjectUtil.isEmpty(waybillDetailVO)) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        return ResponseDTO.ok(waybillDetailVO);
    }
    
    /**
     * 运单凭证上传
     *
     * @param voucherForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> addVoucher(WaybillVoucherForm voucherForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(voucherForm.getWaybillId());
        if (ObjectUtil.isEmpty(waybillEntity) || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在或已作废");
        }
        WaybillVoucherEntity voucherEntity = waybillVoucherDao.selectByWaybillIdAndType(voucherForm.getWaybillId(), voucherForm.getType());
        if (ObjectUtil.isNotEmpty(voucherEntity)) {
            return ResponseDTO.userErrorParam("此类型凭证已存在");
        }
        
        WaybillVoucherEntity waybillVoucherEntity = SmartBeanUtil.copy(voucherForm, WaybillVoucherEntity.class);
        waybillVoucherEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        waybillVoucherEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        waybillVoucherEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        waybillVoucherEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        waybillVoucherEntity.setCreateTime(LocalDateTime.now());
        
        waybillManager.addVoucher(waybillVoucherEntity, waybillEntity);
        waybillDataTracerService.addVoucherLog(voucherForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 统计运单数量和运输完成的运单数量
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<WaybillCountVO> selectWaybillCount(Long driverId) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (driverEntity == null || driverEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("司机账号不存在");
        }

        WaybillCountVO waybillCountVO = new WaybillCountVO();
        List<Integer> waybillStatusList = Arrays.asList(WaybillStatusEnum.COMPLETE.getValue());
        waybillCountVO.setWaybillCount(waybillDao.selectWaybillCount(driverId, null, WaybillStatusEnum.CANCEL.getValue()));
        waybillCountVO.setWaybillCompleteCount(waybillDao.selectWaybillCount(driverId,  waybillStatusList, WaybillStatusEnum.CANCEL.getValue()));
        return ResponseDTO.ok(waybillCountVO);
    }

    /**
     * 查询物流凭证
     *
     * @param waybillId
     * @return
     */
    public List<WaybillVoucherVO> getVoucherList(Long waybillId) {
        return waybillVoucherDao.selectByWaybillId(waybillId);
    }

}
