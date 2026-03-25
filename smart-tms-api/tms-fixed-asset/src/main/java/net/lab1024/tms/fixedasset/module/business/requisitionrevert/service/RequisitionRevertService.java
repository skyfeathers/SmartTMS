package net.lab1024.tms.fixedasset.module.business.requisitionrevert.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.department.CommonDepartmentDao;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetDataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertTypeEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionStatusEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.dao.RequisitionRevertItemDao;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.*;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.manager.RequisitionManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 领用Service
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:37
 */
@Service
public class RequisitionRevertService {

    @Autowired
    private RequisitionRevertItemDao requisitionItemDao;
    @Autowired
    private RequisitionManager requisitionManager;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private CommonDepartmentDao departmentDao;
    @Autowired
    private CommonEmployeeDao employeeDao;
    @Autowired
    private RequisitionRevertDataTracerService requisitionRevertDataTracerService;
    @Autowired
    private AssetDataTracerService assetDataTracerService;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<RequisitionRevertVO> queryPage(RequisitionRevertQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RequisitionRevertEntity> requisitionList = requisitionManager.getBaseMapper().queryPage(page, queryForm);
        List<RequisitionRevertVO> list = SmartBeanUtil.copyList(requisitionList, RequisitionRevertVO.class);
        buildList(list);
        PageResult<RequisitionRevertVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<RequisitionRevertVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> requisitionIdList = list.stream().map(RequisitionRevertVO::getRequisitionRevertId).collect(Collectors.toList());
        List<RequisitionRevertItemEntity> itemEntityList = requisitionItemDao.queryByRequisitionIdList(requisitionIdList);
        Map<Long, List<Long>> requisitionAssetIdMap = itemEntityList.stream().collect(Collectors.groupingBy(RequisitionRevertItemEntity::getRequisitionRevertId, Collectors.mapping(RequisitionRevertItemEntity::getAssetId, Collectors.toList())));

        List<Long> assetIdList = itemEntityList.stream().map(RequisitionRevertItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);


        // 所属公司
        Set<Long> enterpriseIdSet = list.stream().map(RequisitionRevertVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 存放位置
        Set<Long> locationIdSet = list.stream().filter(e -> null != e.getLocationId()).map(RequisitionRevertVO::getLocationId).collect(Collectors.toSet());
        Map<Long, String> locationNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(locationIdSet)) {
            List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
            locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));
        }

        // 使用部门
        Set<Long> departmentIdSet = list.stream().map(RequisitionRevertVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, String> departmentNameMap = departmentList.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));

        // 使用人
        Set<Long> employeeIdSet = list.stream().map(RequisitionRevertVO::getUserId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(employeeIdSet);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));


        for (RequisitionRevertVO requisitionVO : list) {
            requisitionVO.setEnterpriseName(enterpriseNameMap.getOrDefault(requisitionVO.getEnterpriseId(), StringConst.EMPTY));
            requisitionVO.setLocationName(locationNameMap.getOrDefault(requisitionVO.getLocationId(), StringConst.EMPTY));
            requisitionVO.setDepartmentName(departmentNameMap.getOrDefault(requisitionVO.getDepartmentId(), StringConst.EMPTY));
            requisitionVO.setUserName(employeeNameMap.getOrDefault(requisitionVO.getUserId(), StringConst.EMPTY));
            // 设置资产列表
            List<Long> requisitionAssetIdList = requisitionAssetIdMap.getOrDefault(requisitionVO.getRequisitionRevertId(), CommonConst.EMPTY_LIST);
            List<AssetVO> assetList = assetVOList.stream().filter(asset -> requisitionAssetIdList.contains(asset.getAssetId())).collect(Collectors.toList());
            requisitionVO.setAssetList(assetList);
        }
    }

    /**
     * 添加领用
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addRequisition(RequisitionRevertAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }
        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.UNUSED.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能领用", assetEntity.getAssetName(), statusName));
            }
        }

        RequisitionRevertEntity requisitionEntity = SmartBeanUtil.copy(addForm, RequisitionRevertEntity.class);
        requisitionEntity.setType(RequisitionRevertTypeEnum.REQUISITION.getValue());
        requisitionEntity.setRequisitionRevertNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_REQUISITION));
        requisitionEntity.setStatus(RequisitionStatusEnum.WAIT.getValue());
        requisitionEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        requisitionEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        requisitionManager.saveRequisition(requisitionEntity, assetList);
        RequisitionRevertVO requisitionRevertVO = getDetail(requisitionEntity.getRequisitionRevertId());
        requisitionRevertDataTracerService.saveLog(addForm, requisitionRevertVO, dataTracerRequestForm);
        assetDataTracerService.applyRequisitionLog(requisitionEntity.getRequisitionRevertNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * @param requisitionRevertId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> completeRequisition(Long requisitionRevertId, DataTracerRequestForm dataTracerRequestForm) {
        RequisitionRevertEntity requisitionEntity = requisitionManager.getBaseMapper().selectById(requisitionRevertId);
        if (null == requisitionEntity) {
            return ResponseDTO.userErrorParam("领用单据不存在");
        }
        List<RequisitionRevertItemEntity> itemList = requisitionItemDao.queryByRequisitionIdList(Arrays.asList(requisitionRevertId));
        List<Long> assetIdList = itemList.stream().map(RequisitionRevertItemEntity::getAssetId).collect(Collectors.toList());
        requisitionManager.completeRequisition(requisitionEntity, assetIdList);
        requisitionRevertDataTracerService.operateRequisitionLog(requisitionRevertId, RequisitionRevertDataTracerOperateTypeEnum.REQUISITION_COMPLETE, dataTracerRequestForm);
        assetDataTracerService.passRejectRequisitionLog(requisitionEntity.getRequisitionRevertNo(), assetIdList, RequisitionRevertDataTracerOperateTypeEnum.REQUISITION_COMPLETE, AssetStatusEnum.REQUISITION, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 驳回领用申请
     *
     * @param requisitionRevertId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> rejectRequisition(Long requisitionRevertId, DataTracerRequestForm dataTracerRequestForm) {
        RequisitionRevertEntity requisitionEntity = requisitionManager.getBaseMapper().selectById(requisitionRevertId);
        if (null == requisitionEntity) {
            return ResponseDTO.userErrorParam("单据不存在");
        }
        List<RequisitionRevertItemEntity> itemList = requisitionItemDao.queryByRequisitionIdList(Arrays.asList(requisitionRevertId));
        List<Long> assetIdList = itemList.stream().map(RequisitionRevertItemEntity::getAssetId).collect(Collectors.toList());
        requisitionManager.rejectRequisition(requisitionEntity, assetIdList);
        requisitionRevertDataTracerService.operateRequisitionLog(requisitionRevertId, RequisitionRevertDataTracerOperateTypeEnum.REQUISITION_COMPLETE, dataTracerRequestForm);
        assetDataTracerService.passRejectRequisitionLog(requisitionEntity.getRequisitionRevertNo(), assetIdList, RequisitionRevertDataTracerOperateTypeEnum.REQUISITION_REJECT, AssetStatusEnum.UNUSED, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 添加退回
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addRevert(RequisitionRevertAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }

        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.REQUISITION.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能退还", assetEntity.getAssetName(), statusName));
            }
        }

        RequisitionRevertEntity requisitionEntity = SmartBeanUtil.copy(addForm, RequisitionRevertEntity.class);
        requisitionEntity.setType(RequisitionRevertTypeEnum.REVERT.getValue());
        requisitionEntity.setRequisitionRevertNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_REVERT));
        requisitionEntity.setStatus(RequisitionStatusEnum.COMPLETE.getValue());
        requisitionEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        requisitionEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        requisitionManager.saveRevert(requisitionEntity, assetList);
        RequisitionRevertVO requisitionRevertVO = getDetail(requisitionEntity.getRequisitionRevertId());
        requisitionRevertDataTracerService.saveLog(addForm, requisitionRevertVO, dataTracerRequestForm);
        assetDataTracerService.applyRevertLog(requisitionEntity.getRequisitionRevertNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public RequisitionRevertVO getDetail(Long requisitionRevertId) {
        RequisitionRevertEntity requisitionRevertEntity = requisitionManager.getBaseMapper().selectById(requisitionRevertId);
        if (null == requisitionRevertEntity) {
            return null;
        }
        RequisitionRevertVO requisitionRevertVO = SmartBeanUtil.copy(requisitionRevertEntity, RequisitionRevertVO.class);
        buildList(Arrays.asList(requisitionRevertVO));
        return requisitionRevertVO;
    }
}
