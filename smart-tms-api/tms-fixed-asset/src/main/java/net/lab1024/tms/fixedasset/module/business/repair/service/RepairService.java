package net.lab1024.tms.fixedasset.module.business.repair.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.lang.Collections;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.repair.constant.AssetRepairStatusEnum;
import net.lab1024.tms.fixedasset.module.business.repair.dao.RepairDao;
import net.lab1024.tms.fixedasset.module.business.repair.domain.entity.RepairEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairAddForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairQueryForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairAssetVO;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairDetailVO;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 固定资产-维修登记 Service
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Service
public class RepairService {

    @Autowired
    private RepairDao repairDao;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private CommonEmployeeDao commonEmployeeDao;

    @Autowired
    private AssetService assetService;
    @Autowired
    private RepairDataTracerService repairDataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private AssetDao assetDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<RepairVO> queryPage(RepairQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RepairVO> list = repairDao.queryPage(page, queryForm);
        buildList(list);
        PageResult<RepairVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<RepairVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> enterpriseIdSet = list.stream().map(RepairVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
        list.forEach(item -> {
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
        });
    }

    /**
     * 添加
     */

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(RepairAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        RepairEntity repairEntity = SmartBeanUtil.copy(addForm, RepairEntity.class);
        repairEntity.setStatus(AssetRepairStatusEnum.AUDIT.getValue());

        String repairCode = serialNumberService.generate(SerialNumberIdEnum.ASSET_REPAIR);
        repairEntity.setRepairCode(repairCode);

        repairDao.insert(repairEntity);

        if (!Collections.isEmpty(addForm.getAssetIdList())) {
            List<AssetEntity> assetList = assetDao.selectBatchIds(addForm.getAssetIdList());
            Long repairId = repairEntity.getRepairId();
            repairDao.batchInsertRepairAsset(repairId, assetList);
            assetService.batchUpdateRepair(assetList, repairCode, dataTracerRequestForm);
        }
        RepairDetailVO repairDetailVO = getDetail(repairEntity.getRepairId());
        repairDataTracerService.saveLog(addForm, repairDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 批量审核
     *
     * @return
     */
    public ResponseDTO<String> batchAudit(List<Long> repairIdList, AssetRepairStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<RepairEntity> repairEntityList = repairDao.selectBatchIds(repairIdList);
        List<Long> filterIdList = repairEntityList.stream()
                .filter(e -> AssetRepairStatusEnum.AUDIT.getValue().equals(e.getStatus()))
                .map(RepairEntity::getRepairId)
                .collect(Collectors.toList());

        if (filterIdList.size() > 0) {
            repairDao.batchUpdateStatus(filterIdList, status.getValue());
            repairDataTracerService.batchAuditLog(filterIdList, status, dataTracerRequestForm);
            if (AssetRepairStatusEnum.REJECT.equalsValue(status.getValue())) {
                List<RepairAssetVO> assetList = repairDao.selectRelateAssetList(filterIdList);
                assetService.finishRepair(assetList, repairEntityList, "审批驳回", dataTracerRequestForm);
            }
        }
        return ResponseDTO.ok();
    }

    /**
     * 批量完成
     *
     * @return
     */
    public ResponseDTO<String> batchFinish(List<Long> repairIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<RepairEntity> repairEntityList = repairDao.selectBatchIds(repairIdList);
        List<Long> filterIdList = repairEntityList.stream()
                .filter(e -> AssetRepairStatusEnum.REPAIRING.getValue().equals(e.getStatus()))
                .map(RepairEntity::getRepairId)
                .collect(Collectors.toList());

        if (filterIdList.size() > 0) {
            repairDao.batchUpdateStatus(filterIdList, AssetRepairStatusEnum.REPAIR_FINISH.getValue());
            repairDataTracerService.finishLog(repairIdList, dataTracerRequestForm);
            List<RepairAssetVO> assetList = repairDao.selectRelateAssetList(filterIdList);
            assetService.finishRepair(assetList, repairEntityList, "维修完成", dataTracerRequestForm);
        }
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param repairId
     * @return
     */
    public RepairDetailVO getDetail(Long repairId) {
        RepairEntity repairEntity = repairDao.selectById(repairId);
        RepairDetailVO detailVO = SmartBeanUtil.copy(repairEntity, RepairDetailVO.class);

        EmployeeEntity employeeEntity = commonEmployeeDao.selectById(detailVO.getApplyUserId());
        detailVO.setApplyUserName(employeeEntity != null ? employeeEntity.getActualName() : "");

        List<Long> assetIdList = repairDao.selectRelateAssetIdList(repairId);
        List<AssetVO> assetList = assetService.queryDetailByIdList(assetIdList);
        detailVO.setAssetList(assetList);
        EnterpriseEntity enterpriseEntity = commonEnterpriseDao.selectById(repairEntity.getEnterpriseId());
        detailVO.setEnterpriseName(null != enterpriseEntity ? enterpriseEntity.getEnterpriseName() : StringConst.EMPTY);
        return detailVO;
    }
}
