package net.lab1024.tms.fixedasset.module.business.asset.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.material.category.CategoryCommonDao;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
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
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetQueryForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetUpdateForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.entity.RepairEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairAssetVO;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.entity.ScrapEntity;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapAssetVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 固定资产 Service
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Service
public class AssetService {

    @Autowired
    private AssetDao assetDao;
    @Autowired
    private CategoryCommonDao commonCategoryDao;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private CommonDepartmentDao departmentDao;
    @Autowired
    private CommonEmployeeDao employeeDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private AssetDataTracerService assetDataTracerService;
    @Autowired
    private AssetManager assetManager;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<AssetVO> queryPage(AssetQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AssetEntity> entityList = assetDao.queryPage(page, queryForm);
        List<AssetVO> list = SmartBeanUtil.copyList(entityList, AssetVO.class);
        buildList(list);
        PageResult<AssetVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<AssetVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 分类
        Set<Long> categoryIdSet = list.stream().map(AssetVO::getCategoryId).collect(Collectors.toSet());
        List<CategoryEntity> categoryList = commonCategoryDao.selectBatchIds(categoryIdSet);
        Map<Long, String> categoryNameMap = categoryList.stream().collect(Collectors.toMap(CategoryEntity::getCategoryId, CategoryEntity::getCategoryName));

        // 所属公司
        Set<Long> enterpriseIdSet = list.stream().map(AssetVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 存放位置
        Set<Long> locationIdSet = list.stream().map(AssetVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        // 使用部门
        Set<Long> departmentIdSet = list.stream().filter(e -> e.getDepartmentId() != null).map(AssetVO::getDepartmentId).collect(Collectors.toSet());
        Map<Long, String> departmentNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(departmentIdSet)) {
            List<DepartmentEntity> departmentList = departmentDao.selectBatchIds(departmentIdSet);
            departmentNameMap = departmentList.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
        }

        // 使用人
        Set<Long> employeeIdSet = list.stream().filter(e -> e.getUserId() != null).map(AssetVO::getUserId).collect(Collectors.toSet());
        Map<Long, String> employeeNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(employeeIdSet)) {
            List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(employeeIdSet);
            employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }

        // 使用人
        Set<Long> manageIdSet = list.stream().filter(e -> e.getManagerId() != null).map(AssetVO::getManagerId).collect(Collectors.toSet());
        Map<Long, String> managerNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(manageIdSet)) {
            List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(manageIdSet);
            managerNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }
        for (AssetVO assetVO : list) {
            assetVO.setEnterpriseName(enterpriseNameMap.getOrDefault(assetVO.getEnterpriseId(), StringConst.EMPTY));
            assetVO.setCategoryName(categoryNameMap.getOrDefault(assetVO.getCategoryId(), StringConst.EMPTY));
            assetVO.setLocationName(locationNameMap.getOrDefault(assetVO.getLocationId(), StringConst.EMPTY));
            assetVO.setDepartmentName(departmentNameMap.getOrDefault(assetVO.getDepartmentId(), StringConst.EMPTY));
            assetVO.setUserName(employeeNameMap.getOrDefault(assetVO.getUserId(), StringConst.EMPTY));
            assetVO.setManagerName(managerNameMap.getOrDefault(assetVO.getManagerId(), StringConst.EMPTY));
        }
    }

    /**
     * 添加
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> add(AssetAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        ResponseDTO<String> validateResult = validate(addForm);
        if (!validateResult.getOk()) {
            return validateResult;
        }
        AssetEntity assetEntity = SmartBeanUtil.copy(addForm, AssetEntity.class);
        if (SmartStringUtil.isBlank(assetEntity.getAssetNo())) {
            assetEntity.setAssetNo(serialNumberService.generate(SerialNumberIdEnum.ASSET));
        }
        // 默认设置闲置状态
        assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
        if (null != addForm.getUserId()) {
            assetEntity.setStatus(AssetStatusEnum.USED.getValue());
        }
        assetEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        assetEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        assetDao.insert(assetEntity);
        AssetVO assetVO = this.buildAssetDetail(assetEntity.getAssetId());
        assetDataTracerService.saveLog(addForm, assetVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(AssetUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        ResponseDTO<String> validateResult = validate(updateForm);
        if (!validateResult.getOk()) {
            return validateResult;
        }
        AssetEntity dbAssetEntity = assetDao.selectById(updateForm.getAssetId());
        if (!updateForm.getEnterpriseId().equals(dbAssetEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("资产所属企业和当前登录企业不一致");
        }
        AssetEntity assetEntity = SmartBeanUtil.copy(updateForm, AssetEntity.class);
        assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
        if (null != updateForm.getUserId()) {
            assetEntity.setStatus(AssetStatusEnum.USED.getValue());
        }
        AssetVO beforeAsset = this.buildAssetDetail(assetEntity.getAssetId());
        assetDao.updateById(assetEntity);
        AssetVO afterAsset = this.buildAssetDetail(assetEntity.getAssetId());
        assetDataTracerService.updateLog(beforeAsset, afterAsset, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private ResponseDTO<String> validate(AssetAddForm addForm) {
        if (null == commonCategoryDao.selectById(addForm.getCategoryId())) {
            return ResponseDTO.userErrorParam("所属分类不存在");
        }
        if (null == locationDao.selectById(addForm.getLocationId())) {
            return ResponseDTO.userErrorParam("所属分类不存在");
        }
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        assetDao.batchUpdateDeleted(idList, true);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long assetId, DataTracerRequestForm dataTracerRequestForm) {
        if (null == assetId) {
            return ResponseDTO.ok();
        }
        AssetEntity assetEntity = assetDao.selectById(assetId);
        if (!AssetStatusEnum.UNUSED.equalsValue(assetEntity.getStatus())) {
            return ResponseDTO.userErrorParam("资产非闲置状态，不能删除");
        }

        assetDao.updateDeleted(assetId, true);
        assetDataTracerService.deleteLog(assetId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<AssetVO> assetDetail(Long assetId) {
        AssetEntity assetEntity = assetDao.selectById(assetId);
        if (null == assetEntity || assetEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("资产不存在");
        }
        AssetVO assetVO = this.buildAssetDetail(assetEntity);
        return ResponseDTO.ok(assetVO);
    }

    /**
     * 获取详情
     *
     * @param assetId
     * @return
     */
    public AssetVO buildAssetDetail(Long assetId) {
        AssetEntity assetEntity = assetDao.selectById(assetId);
        return this.buildAssetDetail(assetEntity);
    }

    /**
     * 获取详情
     *
     * @param assetEntity
     * @return
     */
    public AssetVO buildAssetDetail(AssetEntity assetEntity) {
        AssetVO assetVO = SmartBeanUtil.copy(assetEntity, AssetVO.class);
        buildList(Arrays.asList(assetVO));
        return assetVO;
    }

    /**
     * 根据资产ID列表
     *
     * @param assetIdList
     * @return
     */
    public List<AssetVO> queryDetailByIdList(List<Long> assetIdList) {
        if (CollectionUtils.isEmpty(assetIdList)) {
            return CommonConst.EMPTY_LIST;
        }
        List<AssetEntity> assetEntityList = assetDao.selectBatchIds(assetIdList);
        if (CollectionUtils.isEmpty(assetEntityList)) {
            return CommonConst.EMPTY_LIST;
        }
        List<AssetVO> assetList = SmartBeanUtil.copyList(assetEntityList, AssetVO.class);
        buildList(assetList);
        return assetList;
    }

    /**
     * 更新为作废
     *
     * @param assetList
     * @param dataTracerRequestForm
     */
    public void batchUpdateCancel(List<ScrapAssetVO> assetList, List<ScrapEntity> scrapList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(assetList)) {
            return;
        }
        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.CANCEL.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
        assetDataTracerService.batchCancelLog(assetList, scrapList, dataTracerRequestForm);
    }

    /**
     * 更新为维修
     *
     * @param assetList
     * @param repairNo
     * @param dataTracerRequestForm
     */
    public void batchUpdateRepair(List<AssetEntity> assetList, String repairNo, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(assetList)) {
            return;
        }
        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.REPAIR.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
        assetDataTracerService.batchRepairLog(assetList, repairNo, dataTracerRequestForm);
    }

    /**
     * 完成维修
     *
     * @param assetList
     * @param repairEntityList
     * @param dataTracerRequestForm
     */
    public void finishRepair(List<RepairAssetVO> assetList, List<RepairEntity> repairEntityList, String operateName, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(assetList)) {
            return;
        }
        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(asset.getAssetStatus());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
        assetDataTracerService.finishRepairLog(assetList, repairEntityList, operateName, dataTracerRequestForm);
    }


}
