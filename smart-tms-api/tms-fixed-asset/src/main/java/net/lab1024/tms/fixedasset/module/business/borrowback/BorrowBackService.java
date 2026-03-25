package net.lab1024.tms.fixedasset.module.business.borrowback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackTypeEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowStatusEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.dao.BorrowBackItemDao;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.*;
import net.lab1024.tms.fixedasset.module.business.borrowback.manager.BorrowBackManager;
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
 * @author lidoudou‰
 * @date 2023/3/21 上午9:37
 */
@Service
public class BorrowBackService {

    @Autowired
    private BorrowBackItemDao borrowBackItemDao;
    @Autowired
    private BorrowBackManager borrowBackManager;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private CommonDepartmentDao departmentDao;
    @Autowired
    private CommonEmployeeDao employeeDao;
    @Autowired
    private BorrowBackDataTracerService borrowBackDataTracerService;
    @Autowired
    private AssetDataTracerService assetDataTracerService;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<BorrowBackVO> queryPage(BorrowBackQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<BorrowBackEntity> borrowBackEntityList = borrowBackManager.getBaseMapper().queryPage(page, queryForm);
        List<BorrowBackVO> list = SmartBeanUtil.copyList(borrowBackEntityList, BorrowBackVO.class);
        buildList(list);
        PageResult<BorrowBackVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<BorrowBackVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> borrowBackIdList = list.stream().map(BorrowBackVO::getBorrowBackId).collect(Collectors.toList());
        List<BorrowBackItemEntity> itemEntityList = borrowBackItemDao.queryByBorrowBackIdList(borrowBackIdList);
        Map<Long, List<Long>> borrowBackAssetIdMap = itemEntityList.stream().collect(Collectors.groupingBy(BorrowBackItemEntity::getBorrowBackId, Collectors.mapping(BorrowBackItemEntity::getAssetId, Collectors.toList())));

        List<Long> assetIdList = itemEntityList.stream().map(BorrowBackItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);


        // 所属公司
        Set<Long> enterpriseIdSet = list.stream().map(BorrowBackVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 使用部门
        Set<Long> departmentIdSet = list.stream().map(BorrowBackVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, String> departmentNameMap = departmentList.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));

        // 使用人
        Set<Long> employeeIdSet = list.stream().map(BorrowBackVO::getUserId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(employeeIdSet);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));


        for (BorrowBackVO borrowBackVO : list) {
            borrowBackVO.setEnterpriseName(enterpriseNameMap.getOrDefault(borrowBackVO.getEnterpriseId(), StringConst.EMPTY));
            borrowBackVO.setDepartmentName(departmentNameMap.getOrDefault(borrowBackVO.getDepartmentId(), StringConst.EMPTY));
            borrowBackVO.setUserName(employeeNameMap.getOrDefault(borrowBackVO.getUserId(), StringConst.EMPTY));
            // 设置资产列表
            List<Long> borrowBackAssetIdList = borrowBackAssetIdMap.getOrDefault(borrowBackVO.getBorrowBackId(), CommonConst.EMPTY_LIST);
            List<AssetVO> assetList = assetVOList.stream().filter(asset -> borrowBackAssetIdList.contains(asset.getAssetId())).collect(Collectors.toList());
            borrowBackVO.setAssetList(assetList);
        }
    }

    /**
     * 添加借用
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addBorrow(BorrowBackAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        if (null == addForm.getBackTime()) {
            return ResponseDTO.userErrorParam("预计归还日期不能为空");
        }
        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }

        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.UNUSED.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能借用", assetEntity.getAssetName(), statusName));
            }
        }

        BorrowBackEntity borrowEntity = SmartBeanUtil.copy(addForm, BorrowBackEntity.class);
        borrowEntity.setType(BorrowBackTypeEnum.BORROW.getValue());
        borrowEntity.setBorrowBackNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_BORROW));
        borrowEntity.setStatus(BorrowStatusEnum.WAIT.getValue());
        borrowEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        borrowEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        borrowBackManager.saveBorrow(borrowEntity, assetList);
        BorrowBackVO borrowBackVO = getDetail(borrowEntity.getBorrowBackId());
        borrowBackDataTracerService.saveLog(addForm, borrowBackVO, dataTracerRequestForm);
        assetDataTracerService.applyBorrowLog(borrowEntity.getBorrowBackNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * @param borrowBackId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> completeBorrow(Long borrowBackId, DataTracerRequestForm dataTracerRequestForm) {
        BorrowBackEntity borrowEntity = borrowBackManager.getBaseMapper().selectById(borrowBackId);
        if (null == borrowEntity) {
            return ResponseDTO.userErrorParam("数据不存在");
        }
        List<BorrowBackItemEntity> itemList = borrowBackItemDao.queryByBorrowBackIdList(Arrays.asList(borrowBackId));
        List<Long> assetIdList = itemList.stream().map(BorrowBackItemEntity::getAssetId).collect(Collectors.toList());
        borrowBackManager.completeBorrow(borrowEntity, assetIdList);
        borrowBackDataTracerService.operateBorrowLog(borrowBackId, BorrowBackDataTracerOperateTypeEnum.BORROW_COMPLETE, dataTracerRequestForm);
        assetDataTracerService.passRejectBorrowBackLog(borrowEntity.getBorrowBackNo(), assetIdList, BorrowBackDataTracerOperateTypeEnum.BORROW_COMPLETE, AssetStatusEnum.BORROW, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 驳回借用申请
     *
     * @param borrowBackId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> rejectBorrow(Long borrowBackId, DataTracerRequestForm dataTracerRequestForm) {
        BorrowBackEntity borrowEntity = borrowBackManager.getBaseMapper().selectById(borrowBackId);
        if (null == borrowEntity) {
            return ResponseDTO.userErrorParam("单据不存在");
        }
        List<BorrowBackItemEntity> itemList = borrowBackItemDao.queryByBorrowBackIdList(Arrays.asList(borrowBackId));
        List<Long> assetIdList = itemList.stream().map(BorrowBackItemEntity::getAssetId).collect(Collectors.toList());
        borrowBackManager.rejectBorrow(borrowEntity, assetIdList);
        borrowBackDataTracerService.operateBorrowLog(borrowBackId, BorrowBackDataTracerOperateTypeEnum.BORROW_REJECT, dataTracerRequestForm);
        assetDataTracerService.passRejectBorrowBackLog(borrowEntity.getBorrowBackNo(), assetIdList, BorrowBackDataTracerOperateTypeEnum.BORROW_REJECT, AssetStatusEnum.UNUSED, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 添加归还
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addBack(BorrowBackAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }

        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.BORROW.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能归还", assetEntity.getAssetName(), statusName));
            }
        }

        BorrowBackEntity backEntity = SmartBeanUtil.copy(addForm, BorrowBackEntity.class);
        backEntity.setType(BorrowBackTypeEnum.BACK.getValue());
        backEntity.setBorrowBackNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_BACK));
        backEntity.setStatus(BorrowStatusEnum.COMPLETE.getValue());
        backEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        backEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        borrowBackManager.saveBack(backEntity, assetList);
        BorrowBackVO borrowBackVO = getDetail(backEntity.getBorrowBackId());
        borrowBackDataTracerService.saveLog(addForm, borrowBackVO, dataTracerRequestForm);
        assetDataTracerService.applyBackLog(backEntity.getBorrowBackNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param borrowBackId
     * @return
     */
    public BorrowBackVO getDetail(Long borrowBackId) {
        BorrowBackEntity borrowBackEntity = borrowBackManager.getBaseMapper().selectById(borrowBackId);
        if (null == borrowBackEntity) {
            return null;
        }
        BorrowBackVO borrowBackVO = SmartBeanUtil.copy(borrowBackEntity, BorrowBackVO.class);
        buildList(Arrays.asList(borrowBackVO));
        return borrowBackVO;
    }
}
