package net.lab1024.tms.fixedasset.module.business.check;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.check.dao.CheckEmployeeDao;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEntity;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckItemEntity;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.*;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckAssetVO;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckDetailVO;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckEmployeeVO;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckVO;
import net.lab1024.tms.fixedasset.module.business.check.manager.CheckItemManager;
import net.lab1024.tms.fixedasset.module.business.check.manager.CheckManager;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:14
 */
@Service
public class CheckService {

    @Autowired
    private CheckManager checkManager;
    @Autowired
    private CheckItemManager checkItemManager;
    @Autowired
    private CheckEmployeeDao checkEmployeeDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private AssetService assetService;
    @Autowired
    private CommonEmployeeDao commonEmployeeDao;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private CheckDataTracerService checkDataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<CheckVO> queryPage(CheckQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CheckEntity> checkEntityList = checkManager.getBaseMapper().queryPage(page, queryForm);

        List<CheckVO> checkList = SmartBeanUtil.copyList(checkEntityList, CheckVO.class);
        buildList(checkList);
        PageResult<CheckVO> pageResult = SmartPageUtil.convert2PageResult(page, checkList);
        return pageResult;
    }

    private void buildList(List<CheckVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> locationIdSet = list.stream().map(CheckVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        Set<Long> enterpriseIdSet = list.stream().map(CheckVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        list.forEach(item -> {
            item.setLocationName(locationNameMap.getOrDefault(item.getLocationId(), StringConst.EMPTY));
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
        });
    }

    /**
     * 添加
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> add(CheckAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {

        LocationEntity locationEntity = locationDao.selectById(addForm.getLocationId());
        if (null == locationEntity || locationEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("盘点位置不存在");
        }
        List<EmployeeEntity> employeeEntityList = commonEmployeeDao.selectBatchIds(addForm.getEmployeeIdList());
        if (employeeEntityList.size() != addForm.getEmployeeIdList().size()) {
            return ResponseDTO.userErrorParam("盘点人员不存在");
        }
        // 根据盘点位置查询资产
        List<AssetEntity> assetList = assetDao.selectByEnterpriseLocationId(addForm.getEnterpriseId(), addForm.getLocationId(), Boolean.FALSE);
        if (CollectionUtils.isEmpty(assetList)) {
            return ResponseDTO.userErrorParam("该位置没有待盘点资产，请重新选择盘点位置");
        }
        List<Long> assetIdList = assetList.stream().map(AssetEntity::getAssetId).collect(Collectors.toList());

        CheckEntity checkEntity = SmartBeanUtil.copy(addForm, CheckEntity.class);
        checkEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        checkEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        checkManager.save(checkEntity, addForm.getEmployeeIdList(), assetIdList);
        CheckDetailVO checkDetailVO = getDetail(checkEntity.getCheckId());
        checkDataTracerService.saveLog(addForm, checkDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取盘点单详情
     *
     * @param checkId
     * @return
     */
    public CheckDetailVO getDetail(Long checkId) {
        CheckEntity checkEntity = checkManager.getBaseMapper().selectById(checkId);
        if (null == checkEntity) {
            return null;
        }
        CheckDetailVO checkDetailVO = SmartBeanUtil.copy(checkEntity, CheckDetailVO.class);
        buildList(Arrays.asList(checkDetailVO));
        // 设置盘点清单
        queryAssetListByCheckId(checkDetailVO);
        // 设置盘点人
        buildEmployeeList(checkDetailVO);
        return checkDetailVO;
    }

    private void queryAssetListByCheckId(CheckDetailVO checkDetailVO) {
        Long checkId = checkDetailVO.getCheckId();
        List<CheckItemEntity> checkItemEntityList = checkItemManager.getBaseMapper().queryByCheckId(checkId);

        // 设置盘点状态的数量
        Map<Integer, Long> checkItemStatusMap = checkItemEntityList.stream().collect(Collectors.groupingBy(CheckItemEntity::getStatus, Collectors.counting()));
        checkDetailVO.setProfitCount(checkItemStatusMap.getOrDefault(CheckStatusEnum.PROFIT.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());
        checkDetailVO.setLossCount(checkItemStatusMap.getOrDefault(CheckStatusEnum.LOSS.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());
        checkDetailVO.setNotCheckCount(checkItemStatusMap.getOrDefault(CheckStatusEnum.NOT_CHECK.getValue(), NumberConst.DEFAULT_PARENT_ID).intValue());

        List<Long> assetIdList = checkItemEntityList.stream().map(CheckItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);
        Map<Long, AssetVO> assetVOMap = assetVOList.stream().collect(Collectors.toMap(AssetVO::getAssetId, Function.identity()));

        Set<Long> employeeIdSet = checkItemEntityList.stream().filter(e -> null != e.getEmployeeId()).map(CheckItemEntity::getEmployeeId).collect(Collectors.toSet());
        Map<Long, String> employeeNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(employeeIdSet)) {
            List<EmployeeEntity> employeeEntityList = commonEmployeeDao.selectBatchIds(employeeIdSet);
            employeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }

        List<CheckAssetVO> checkAssetList = Lists.newArrayList();
        for (CheckItemEntity checkItemEntity : checkItemEntityList) {
            Long assetId = checkItemEntity.getAssetId();
            AssetVO assetVO = assetVOMap.get(assetId);
            CheckAssetVO checkAssetVO = SmartBeanUtil.copy(assetVO, CheckAssetVO.class);
            checkAssetVO.setItemId(checkItemEntity.getItemId());
            checkAssetVO.setStatus(checkItemEntity.getStatus());
            checkAssetVO.setEmployeeName(employeeNameMap.getOrDefault(checkItemEntity.getEmployeeId(), StringConst.EMPTY));
            checkAssetVO.setCheckTime(checkItemEntity.getCheckTime());
            checkAssetVO.setRemark(checkItemEntity.getRemark());
            checkAssetList.add(checkAssetVO);
        }
        checkDetailVO.setAssetList(checkAssetList);
    }

    private void buildEmployeeList(CheckDetailVO checkDetailVO) {
        List<Long> employeeIdList = checkEmployeeDao.queryByCheckId(checkDetailVO.getCheckId());
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return;
        }
        List<EmployeeEntity> employeeList = commonEmployeeDao.selectBatchIds(employeeIdList);
        checkDetailVO.setEmployeeList(SmartBeanUtil.copyList(employeeList, CheckEmployeeVO.class));
    }

    /**
     * 盘点
     *
     * @param checkForm
     * @return
     */
    public ResponseDTO<String> check(CheckForm checkForm, DataTracerRequestForm dataTracerRequestForm) {
        CheckEntity checkEntity = checkManager.getBaseMapper().selectById(checkForm.getCheckId());
        if (null == checkEntity) {
            return ResponseDTO.userErrorParam("盘点单不存在");
        }
        if (checkEntity.getCompleteFlag()) {
            return ResponseDTO.userErrorParam("已完成盘点，不能重复操作");
        }
        List<Long> checkEmployeeIdList = checkEmployeeDao.queryByCheckId(checkForm.getCheckId());
        if (!checkEmployeeIdList.contains(dataTracerRequestForm.getOperatorId())) {
            return ResponseDTO.userErrorParam("您非该单盘点人");
        }

        List<CheckAssetForm> checkAssetList = checkForm.getAssetList();
        List<CheckItemEntity> checkItemList = checkAssetList.stream().map(asset -> {
            CheckItemEntity checkItemEntity = new CheckItemEntity();
            checkItemEntity.setItemId(asset.getItemId());
            checkItemEntity.setEmployeeId(dataTracerRequestForm.getOperatorId());
            checkItemEntity.setRemark(asset.getRemark());
            checkItemEntity.setStatus(asset.getStatus());
            if (!CheckStatusEnum.NOT_CHECK.equalsValue(asset.getStatus())) {
                checkItemEntity.setCheckTime(LocalDateTime.now());
            }
            return checkItemEntity;
        }).collect(Collectors.toList());
        CheckDetailVO beforeData = getDetail(checkForm.getCheckId());
        checkItemManager.updateBatchById(checkItemList);
        CheckDetailVO afterData = getDetail(checkForm.getCheckId());
        checkDataTracerService.updateCheckLog(beforeData, afterData, checkForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 完成盘点单
     *
     * @param completeForm
     * @return
     */
    public ResponseDTO<String> completeCheck(CheckCompleteForm completeForm, DataTracerRequestForm dataTracerRequestForm) {
        CheckEntity checkEntity = checkManager.getBaseMapper().selectById(completeForm.getCheckId());
        if (null == checkEntity) {
            return ResponseDTO.userErrorParam("盘点单不存在");
        }
        CheckEntity updateEntity = new CheckEntity();
        updateEntity.setCheckId(completeForm.getCheckId());
        updateEntity.setCompleteFlag(Boolean.TRUE);
        updateEntity.setCompleteTime(completeForm.getCompleteTime());
        checkManager.updateById(updateEntity);
        checkDataTracerService.completeLog(completeForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
