package net.lab1024.tms.admin.module.business.bracket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.bracket.domain.form.*;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketDetailVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketExportVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketListVO;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketSimpleVO;
import net.lab1024.tms.admin.module.business.insurance.InsuranceDao;
import net.lab1024.tms.admin.module.business.insurance.InsuranceService;
import net.lab1024.tms.admin.module.business.repair.dao.RepairDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.business.insurance.domain.InsuranceEntity;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncryptUtil;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yandy
 */
@Service
public class BracketService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private RepairDao repairDao;
    @Autowired
    private InsuranceDao insuranceDao;
    @Autowired
    private BracketDataTracerService bracketDataTracerService;

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<BracketListVO>> queryByPage(BracketQueryForm queryForm, Long enterpriseId) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setDeletedFlag(Boolean.FALSE);

        List<BracketListVO> bracketList = bracketDao.queryByPage(page, queryForm);
        // 保险到期时间
        this.buildBracketInsuranceEnterprise(bracketList, enterpriseId);
        FieldEncryptUtil.injectList(bracketList);
        PageResult<BracketListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, bracketList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 构建保险到期时间
     *
     * @param bracketList
     */
    private void buildBracketInsuranceEnterprise(List<BracketListVO> bracketList, Long enterpriseId) {
        if (CollectionUtils.isEmpty(bracketList)) {
            return;
        }
        List<Long> bracketIdList = bracketList.stream().map(BracketListVO::getBracketId).collect(Collectors.toList());
        //保险
        Map<Long, List<InsuranceEntity>> insuranceEntityListMap = Maps.newHashMap();
        List<InsuranceEntity> insuranceEntityList = insuranceDao.selectByModuleIdListAndType(bracketIdList, InsuranceModuleTypeEnum.BRACKET.getValue());
        if (CollectionUtils.isNotEmpty(insuranceEntityList)) {
            insuranceEntityListMap = insuranceEntityList.stream().collect(groupingBy(InsuranceEntity::getModuleId));
        }
        for (BracketListVO bracketListVO : bracketList) {
            Long bracketId = bracketListVO.getBracketId();
            List<InsuranceEntity> insuranceList = insuranceEntityListMap.get(bracketId);
            if (CollectionUtils.isNotEmpty(insuranceList)) {
                // 商业险
                Optional<InsuranceEntity> commercialInsuranceOptional = insuranceList.stream().filter(e -> InsuranceTypeEnum.COMMERCIAL_INSURANCE.equalsValue(e.getInsuranceType())).findFirst();
                if (commercialInsuranceOptional.isPresent()) {
                    bracketListVO.setCommercialExpireTime(commercialInsuranceOptional.get().getExpireDate());
                }
                // 交强险
                Optional<InsuranceEntity> compulsoryTrafficInsuranceOptional = insuranceList.stream().filter(e -> InsuranceTypeEnum.COMPULSORY_TRAFFIC_INSURANCE.equalsValue(e.getInsuranceType())).findFirst();
                if (compulsoryTrafficInsuranceOptional.isPresent()) {
                    bracketListVO.setCompulsoryTrafficExpireTime(compulsoryTrafficInsuranceOptional.get().getExpireDate());
                }
            }

        }
    }

    /**
     * 查询不分页的列表
     *
     * @return
     */
    public ResponseDTO<List<BracketSimpleVO>> queryList(Long enterpriseId) {
        BracketQueryForm queryForm = new BracketQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<BracketListVO> bracketListVOList = bracketDao.queryByPage(null, queryForm);
        List<BracketSimpleVO> bracketList = SmartBeanUtil.copyList(bracketListVOList, BracketSimpleVO.class);
        return ResponseDTO.ok(bracketList);
    }

    /**
     * 查询详情
     *
     * @param bracketId
     * @return
     */
    public ResponseDTO<BracketDetailVO> getDetail(Long bracketId, Long enterpriseId) {
        BracketEntity bracketEntity = bracketDao.selectById(bracketId);
        if (null == bracketEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (bracketEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        BracketDetailVO detailVO = SmartBeanUtil.copy(bracketEntity, BracketDetailVO.class);
        detailVO.setInsuranceList(insuranceService.selectByModuleIdAndType(detailVO.getBracketId(), InsuranceModuleTypeEnum.BRACKET.getValue()));
        if (detailVO.getManagerId() != null) {
            EmployeeEntity employeeEntity = employeeDao.selectById(detailVO.getManagerId());
            detailVO.setManagerName(employeeEntity != null ? employeeEntity.getActualName() : StringConst.EMPTY);
        }
        return ResponseDTO.ok(detailVO);
    }

    /**
     * 保存
     *
     * @param addDTO
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> save(BracketAddForm addDTO, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        // 挂车车牌号重复校验
        List<BracketEntity> shipperBasicList = bracketDao.selectByNoExcludeIds(enterpriseId, addDTO.getBracketNo(), null, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "挂车车牌号已存在");
        }
        BracketEntity bracketEntity = SmartBeanUtil.copy(addDTO, BracketEntity.class);
        bracketEntity.setEnterpriseId(enterpriseId);
        bracketEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        bracketEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        bracketEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        bracketEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        bracketDao.insert(bracketEntity);

        bracketDataTracerService.saveLog(bracketEntity.getBracketId(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 保存
     *
     * @param updateDTO
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> update(BracketUpdateForm updateDTO, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        Long bracketId = updateDTO.getBracketId();
        BracketEntity dbBracketEntity = bracketDao.selectById(bracketId);
        if (null == dbBracketEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 挂车车牌号重复校验
        List<BracketEntity> shipperBasicList = bracketDao.selectByNoExcludeIds(enterpriseId, updateDTO.getBracketNo(), Lists.newArrayList(updateDTO.getBracketId()), Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "挂车车牌号已存在");
        }
        BracketDetailVO beforeData = this.getDetail(bracketId, enterpriseId).getData();

        BracketEntity bracketEntity = SmartBeanUtil.copy(updateDTO, BracketEntity.class);
        bracketEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        bracketEntity.setUpdateUserName(dataTracerRequestForm.getOperatorName());
        bracketEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        bracketDao.updateById(bracketEntity);

        BracketDetailVO afterData = this.getDetail(bracketId, enterpriseId).getData();

        bracketDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     *
     * @param bracketIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> deleteBracket(List<Long> bracketIdList, DataTracerRequestForm dataTracerRequestForm) {
        BracketService bracketService = (BracketService) AopContext.currentProxy();
        bracketService.deleteHandle(bracketIdList, dataTracerRequestForm.getOperatorId());

        bracketDataTracerService.batchDeleteLog(bracketIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除车辆相关的信息
     *
     * @param bracketIdList
     * @param requestUserId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteHandle(List<Long> bracketIdList, Long requestUserId) {
        bracketDao.updateDeletedFlagById(bracketIdList, Boolean.TRUE, requestUserId);
        repairDao.updateDeletedFlagByModuleIdAndType(bracketIdList, RepairModuleTypeEnum.BRACKET.getValue(), Boolean.TRUE);
        insuranceDao.updateDeletedFlagByModuleIdAndType(bracketIdList, InsuranceModuleTypeEnum.BRACKET.getValue(), requestUserId, Boolean.TRUE);
    }

    /**
     * 挂车批量审核
     *
     * @param batchAuditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchAudit(BracketBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(batchAuditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }

        List<Long> bracketIdList = batchAuditForm.getBracketIdList();
        List<BracketEntity> bracketList = bracketDao.selectList(bracketIdList, AuditStatusEnum.WAIT_AUDIT.getValue(), Boolean.FALSE);
        if (bracketList.size() != bracketIdList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "挂车数据异常，请刷新后重试");
        }
        bracketDao.batchUpdateAuditStatus(bracketIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark());

        bracketDataTracerService.batchAuditLog(bracketIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量修改负责人
     *
     * @param updateManagerForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateManager(BracketManagerUpdateForm updateManagerForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> bracketIdList = updateManagerForm.getBracketIdList();
        List<BracketEntity> bracketEntityList = bracketDao.selectBatchIds(bracketIdList);
        if (bracketEntityList.size() != bracketIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(updateManagerForm.getManagerId());
        if (employeeEntity == null) {
            return ResponseDTO.userErrorParam("负责人不存在");
        }
        bracketDao.batchUpdateManager(bracketIdList, updateManagerForm.getManagerId());
        bracketDataTracerService.batchUpdateManagerLog(bracketIdList, employeeEntity, dataTracerRequestForm);

        return ResponseDTO.ok();
    }

    /**
     * 挂车导出
     *
     * @param queryForm
     * @return
     */
    public List<BracketExportVO> queryByExport(BracketQueryForm queryForm, Long enterpriseId) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<BracketListVO> bracketList = bracketDao.queryByPage(null, queryForm);
        // 保险到期时间
        this.buildBracketInsuranceEnterprise(bracketList, enterpriseId);
        List<BracketExportVO> exportList = bracketList.stream().map(e -> {
            BracketExportVO exportVO = SmartBeanUtil.copy(e, BracketExportVO.class);
            exportVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getAuditStatus(), FlowAuditStatusEnum.class));
            return exportVO;
        }).collect(Collectors.toList());
        return exportList;
    }

    /**
     * 修改经营方式
     * @param updateForm
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<String> businessModeUpdate(BracketBusinessModeUpdateForm updateForm, Long enterpriseId) {
        BracketEntity updateEntity = new BracketEntity();
        updateEntity.setBracketId(updateForm.getBracketId());
        updateEntity.setBusinessMode(updateForm.getBusinessMode());
        bracketDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

}
