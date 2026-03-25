package net.lab1024.tms.admin.module.business.oilcard.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.service.EnterpriseBusinessSettingService;
import net.lab1024.tms.admin.module.business.oilcard.dao.BalanceRecordDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardEnterpriseDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardAmountDTO;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.SlaveOilCardImportDTO;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.*;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardEnterpriseVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardExportVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardSimpleVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardVO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillOilCardRechargeApplyDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.BusinessSettingEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseBusinessSettingEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 油卡管理
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:30
 */
@Service
public class OilCardService {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardDataTracerService oilCardDataTracerService;
    @Autowired
    private OilCardBalanceService oilCardBalanceService;
    @Autowired
    private OilCardManager oilCardManager;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private BalanceRecordDao balanceRecordDao;
    @Autowired
    private WaybillOilCardRechargeApplyDao waybillOilCardRechargeApplyDao;
    @Autowired
    private EnterpriseBusinessSettingService enterpriseBusinessSettingService;

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<OilCardVO>> queryByPage(OilCardQueryForm queryForm, Long enterpriseId, Long employeeId) {
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OilCardVO> oilCardList = oilCardDao.queryByPage(page, queryForm);
        this.buildListVO(oilCardList, queryForm.getBalanceRecordStartTime(), queryForm.getBalanceRecordEndTime(), queryForm.getType());
        PageResult<OilCardVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, oilCardList);
        return ResponseDTO.ok(pageResultDTO);
    }

    public ResponseDTO<BigDecimal> cardSummary(OilCardQueryForm queryForm, Long enterpriseId, Long employeeId) {
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setRecordType( OilCardBalanceRecordTypeEnum.RECHARGE.getValue());
        BigDecimal amount = oilCardDao.querySummary(queryForm);
        return ResponseDTO.ok(amount);
    }

    public List<OilCardExportVO> queryByExport(OilCardQueryForm queryForm, Long enterpriseId, Long employeeId) {
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setType(OilCardTypeEnum.MASTER.getValue());
        queryForm.setDeletedFlag(Boolean.FALSE);
        List<OilCardVO> oilCardList = oilCardDao.queryByPage(null, queryForm);
        if (CollectionUtils.isEmpty(oilCardList)) {
            return Lists.newArrayList();
        }
        List<DictValueVO> oilCardBrandList = dictCacheService.selectByKeyCode("OIL-CARD-BRAND");
        Map<String, String> brandMap = oilCardBrandList.stream().collect(Collectors.toMap(DictValueVO::getValueCode, DictValueVO::getValueName));

        List<DictValueVO> oilCardPurposeList = dictCacheService.selectByKeyCode("OIL-CARD-PURPOSE");
        Map<String, String> purposeMap = oilCardPurposeList.stream().collect(Collectors.toMap(DictValueVO::getValueCode, DictValueVO::getValueName));

        Set<Long> enterpriseIdList = oilCardList.stream().map(OilCardVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));


        this.buildListVO(oilCardList, queryForm.getBalanceRecordStartTime(), queryForm.getBalanceRecordEndTime(), OilCardTypeEnum.MASTER.getValue());
        List<OilCardExportVO> exportList = oilCardList.stream().map(e -> {
            OilCardExportVO exportVO = SmartBeanUtil.copy(e, OilCardExportVO.class);
            exportVO.setTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(exportVO.getType(), OilCardTypeEnum.class));
            exportVO.setFuelTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(exportVO.getFuelType(), OilCardFuelTypeEnum.class));
            exportVO.setBrand(brandMap.getOrDefault(e.getBrand(), StringConst.EMPTY));
            exportVO.setPurpose(purposeMap.getOrDefault(e.getBrand(), StringConst.EMPTY));
            exportVO.setEnterpriseName(enterpriseNameMap.getOrDefault(e.getEnterpriseId(), ""));
            return exportVO;
        }).collect(Collectors.toList());
        return exportList;
    }

    /**
     * 设置展示信息
     *
     * @param oilCardList
     */
    private void buildListVO(List<OilCardVO> oilCardList, LocalDate balanceRecordStartTime, LocalDate balanceRecordEndTime, Integer oilCardType) {
        if (CollectionUtils.isEmpty(oilCardList)) {
            return;
        }
        Set<Long> masterOilCardIdList = oilCardList.stream().filter(e -> e.getMasterOilCardId() != null && !e.getMasterOilCardId().equals(NumberUtils.LONG_ZERO)).map(e -> e.getMasterOilCardId()).collect(Collectors.toSet());
        Map<Long, String> masterOilCardNoMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(masterOilCardIdList)) {
            List<OilCardEntity> oilCardEntityList = oilCardDao.selectBatchIds(masterOilCardIdList);
            masterOilCardNoMap = oilCardEntityList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardId, OilCardEntity::getOilCardNo));
        }
        List<Long> oilCardIdList = oilCardList.stream().map(OilCardVO::getOilCardId).collect(Collectors.toList());

        Set<Long> enterpriseIdList = oilCardList.stream().map(OilCardVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 持卡司机
        Set<Long> driverIdList = oilCardList.stream().filter(e -> null != e.getUseDriverId() && !e.getUseDriverId().equals(NumberUtils.LONG_ZERO)).map(OilCardVO::getUseDriverId).collect(Collectors.toSet());
        Map<Long, String> driverNameMap = Maps.newHashMap();
        Map<Long, String> driverPhoneMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            List<DriverEntity> driverList = driverDao.selectBatchIds(driverIdList);
            driverNameMap = driverList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));
            driverPhoneMap = driverList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getTelephone));
        }

        // 持卡车
        Set<Long> vehicleIdList = oilCardList.stream().filter(e -> null != e.getUseVehicleId() && !e.getUseVehicleId().equals(NumberUtils.LONG_ZERO)).map(OilCardVO::getUseVehicleId).collect(Collectors.toSet());
        Map<Long, String> vehicleNumberMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(vehicleIdList)) {
            List<VehicleEntity> vehicleList = vehicleDao.selectBatchIds(vehicleIdList);
            vehicleNumberMap = vehicleList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, VehicleEntity::getVehicleNumber));
        }

        // 使用人
        Set<Long> receiveUserIdList = oilCardList.stream().filter(e -> null != e.getReceiveUserId() && !e.getReceiveUserId().equals(NumberUtils.LONG_ZERO)).map(OilCardVO::getReceiveUserId).collect(Collectors.toSet());
        Map<Long, String> receiveUserNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(receiveUserIdList)) {
            List<EmployeeEntity> receiveUserNameList = employeeDao.selectBatchIds(receiveUserIdList);
            receiveUserNameMap = receiveUserNameList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }

        // 查询主卡充值金额合计
        Map<Long, BigDecimal> rechargeAmountMap = Maps.newHashMap();
        if (OilCardTypeEnum.MASTER.equalsValue(oilCardType)) {
            rechargeAmountMap = queryOilCardRechargeApplyAmountByRecordType(oilCardIdList, OilCardBalanceRecordTypeEnum.RECHARGE, balanceRecordStartTime, balanceRecordEndTime);
        }

        // 查询副卡待分配金额合计
        Map<Long, BigDecimal> oilCardRechargeApplyAmountMap = Maps.newHashMap();
        if (OilCardTypeEnum.SLAVE.equalsValue(oilCardType)) {
            oilCardRechargeApplyAmountMap = queryOilCardRechargeApplyAmountByRecordType(oilCardIdList);
        }


        for (OilCardVO item : oilCardList) {
            item.setMasterOilCardNo(masterOilCardNoMap.getOrDefault(item.getMasterOilCardId(), StringConst.EMPTY));
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), ""));

            item.setUseDriverName(driverNameMap.getOrDefault(item.getUseDriverId(), StringConst.EMPTY));
            item.setUseDriverPhone(driverPhoneMap.getOrDefault(item.getUseDriverId(), StringConst.EMPTY));
            item.setUseVehicleNumber(vehicleNumberMap.getOrDefault(item.getUseVehicleId(), StringConst.EMPTY));
            item.setReceiveUserName(receiveUserNameMap.getOrDefault(item.getReceiveUserId(), StringConst.EMPTY));
            item.setRechargeBalance(rechargeAmountMap.getOrDefault(item.getOilCardId(), BigDecimal.ZERO));
            item.setDistributeBalance(oilCardRechargeApplyAmountMap.getOrDefault(item.getOilCardId(), BigDecimal.ZERO));
        }
    }

    private Map<Long, BigDecimal> queryOilCardRechargeApplyAmountByRecordType(List<Long> oilCardIdList, OilCardBalanceRecordTypeEnum balanceRecordTypeEnum, LocalDate balanceRecordStartTime, LocalDate balanceRecordEndTime) {
        Map<Long, BigDecimal> oilCardAmountMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(oilCardIdList)) {
            return oilCardAmountMap;
        }
        if (null == balanceRecordTypeEnum) {
            return oilCardAmountMap;
        }
        List<OilCardAmountDTO> oilCardAmountList = balanceRecordDao.selectAmountByOilCard(oilCardIdList, balanceRecordTypeEnum.getValue(), balanceRecordStartTime, balanceRecordEndTime);
        oilCardAmountMap = oilCardAmountList.stream().collect(Collectors.toMap(OilCardAmountDTO::getOilCardId, OilCardAmountDTO::getBalance));
        return oilCardAmountMap;
    }

    /**
     * 查询副卡待分配金额（油卡充值已审核通过未充值的金额）
     *
     * @param oilCardIdList
     * @return
     */
    private Map<Long, BigDecimal> queryOilCardRechargeApplyAmountByRecordType(List<Long> oilCardIdList) {
        Map<Long, BigDecimal> oilCardAmountMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(oilCardIdList)) {
            return oilCardAmountMap;
        }
        List<OilCardAmountDTO> oilCardAmountList = waybillOilCardRechargeApplyDao.selectAmountByOilCardList(oilCardIdList, FlowAuditStatusEnum.PASS.getValue(), Boolean.FALSE, Boolean.FALSE);
        oilCardAmountMap = oilCardAmountList.stream().collect(Collectors.toMap(OilCardAmountDTO::getOilCardId, OilCardAmountDTO::getBalance));
        return oilCardAmountMap;
    }

    /**
     * 查询油卡不分页的列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<OilCardVO>> queryList(OilCardQueryForm queryForm, Long enterpriseId, Long employeeId) {
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setDeletedFlag(Boolean.FALSE);
        List<OilCardVO> oilCardList = oilCardDao.queryByPage(null, queryForm);
        return ResponseDTO.ok(oilCardList);
    }

    /**
     * 查询详情
     *
     * @param oilCardId
     * @return
     */
    public OilCardVO getDetail(Long oilCardId) {
        if (oilCardId == null) {
            return null;
        }
        OilCardVO oilCardVO = oilCardDao.getDetail(oilCardId);
        if (oilCardVO == null) {
            return null;
        }
        this.buildListVO(Lists.newArrayList(oilCardVO), null, null, oilCardVO.getType());
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(oilCardVO.getEnterpriseId());
        if (enterpriseEntity == null) {
            oilCardVO.setEnterpriseName("");
        }
        return oilCardVO;
    }

    /**
     * 新建
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(OilCardAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        if (OilCardTypeEnum.SLAVE.equalsValue(addForm.getType()) && addForm.getMasterOilCardId() == null) {
            return ResponseDTO.userErrorParam("主卡不存在");
        }
        if (OilCardTypeEnum.SLAVE.equalsValue(addForm.getType()) && addForm.getMasterOilCardId() != null) {
            OilCardEntity masterEntity = oilCardDao.selectById(addForm.getMasterOilCardId());
            if (masterEntity == null) {
                return ResponseDTO.userErrorParam("主卡不存在");
            }
            if (!masterEntity.getEnterpriseId().equals(addForm.getEnterpriseId())) {
                return ResponseDTO.userErrorParam("您登录的企业不是该主卡的所属公司");
            }
        }
        List<OilCardEntity> existList = oilCardDao.selectByNoExcludeIds(addForm.getOilCardNo(), Lists.newArrayList(), false);
        if (CollectionUtils.isNotEmpty(existList)) {
            return ResponseDTO.userErrorParam("卡号重复");
        }
        OilCardEntity oilCardEntity = SmartBeanUtil.copy(addForm, OilCardEntity.class);
        oilCardEntity.setBalance(oilCardEntity.getBeginBalance());
        oilCardEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        oilCardEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        if (OilCardTypeEnum.MASTER.equalsValue(addForm.getType())) {
            oilCardEntity.setMasterOilCardId(NumberUtils.LONG_ZERO);
        }
        oilCardManager.addOilCard(oilCardEntity);

        oilCardDataTracerService.saveLog(oilCardEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新油卡
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(OilCardUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {

        OilCardEntity beforeData = oilCardDao.selectById(updateForm.getOilCardId());
        if (beforeData == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<OilCardEntity> existList = oilCardDao.selectByNoExcludeIds(updateForm.getOilCardNo(), Lists.newArrayList(updateForm.getOilCardId()), false);
        if (CollectionUtils.isNotEmpty(existList)) {
            return ResponseDTO.userErrorParam("卡号重复");
        }
        // 副卡的话，验证主卡是否禁用
        if (OilCardTypeEnum.SLAVE.equalsValue(beforeData.getType())) {
            Long masterOilCardId = beforeData.getMasterOilCardId();
            OilCardEntity masterOilCardEntity = oilCardDao.selectById(masterOilCardId);
            if (null == masterOilCardEntity || masterOilCardEntity.getDeletedFlag()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "关联主卡不存在，请修改主卡");
            }
            if (masterOilCardEntity.getDisabledFlag() && !updateForm.getDisabledFlag()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "主卡已被禁用，不能修改状态");
            }
        }

        OilCardEntity oilCardEntity = SmartBeanUtil.copy(updateForm, OilCardEntity.class);
        oilCardManager.updateOilCard(oilCardEntity, beforeData.getDisabledFlag().equals(oilCardEntity.getDisabledFlag()), dataTracerRequestForm);
        //操作日志
        OilCardEntity afterData = oilCardDao.selectById(updateForm.getOilCardId());
        oilCardDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     *
     * @param oilCardIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateDeletedFlag(List<Long> oilCardIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(oilCardIdList)) {
            return ResponseDTO.ok();
        }
        List<OilCardEntity> oilCardEntityList = oilCardDao.selectBatchIds(oilCardIdList);
        if (CollectionUtils.isEmpty(oilCardEntityList)) {
            return ResponseDTO.ok();
        }
        oilCardDao.updateDeletedFlag(oilCardIdList, Boolean.TRUE);
        //操作记录
        oilCardDataTracerService.batchDeleteLog(oilCardIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 充值
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> recharge(OilCardBalanceChangeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(rechargeForm.getOilCardId());
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (!OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有主卡允许充值");
        }
        if (ObjectUtils.isEmpty(rechargeForm.getTransactionTime())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "充值时间不能为空");
        }
        //变动余额
        rechargeForm.setRecordType(OilCardBalanceRecordTypeEnum.RECHARGE.getValue());

        oilCardBalanceService.balanceChange(rechargeForm);
        //操作记录
        String content = OilCardDataTracerOperateTypeEnum.RECHARGE.getDesc() + "，变动余额：" + rechargeForm.getChangeAmount();
        oilCardDataTracerService.balanceChangeLog(rechargeForm.getOilCardId(), content, OilCardDataTracerOperateTypeEnum.RECHARGE, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 分配余额到副卡
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<Long> slaveDistribute(OilCardBalanceChangeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(rechargeForm.getOilCardId());
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "主卡不允许进行分配");
        }
        //变动余额
        rechargeForm.setRecordType(OilCardBalanceRecordTypeEnum.DISTRIBUTE.getValue());
        Long recordId = oilCardBalanceService.balanceChange(rechargeForm);
        //操作记录
        String slaveContent = OilCardDataTracerOperateTypeEnum.DISTRIBUTE.getDesc() + "，变动余额：" + rechargeForm.getChangeAmount().toString();
        oilCardDataTracerService.balanceChangeLog(rechargeForm.getOilCardId(), slaveContent, OilCardDataTracerOperateTypeEnum.DISTRIBUTE, dataTracerRequestForm);

        String masterContent = OilCardDataTracerOperateTypeEnum.DISTRIBUTE.getDesc() + ", 副卡【" + oilCardEntity.getOilCardNo() + "】，变动余额：" + rechargeForm.getChangeAmount().negate();
        oilCardDataTracerService.balanceChangeLog(oilCardEntity.getMasterOilCardId(), masterContent, OilCardDataTracerOperateTypeEnum.DISTRIBUTE, dataTracerRequestForm);
        return ResponseDTO.ok(recordId);
    }

    /**
     * 手动消耗
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> expend(OilCardBalanceChangeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardId = rechargeForm.getOilCardId();
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (!OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有主卡允许手动消耗");
        }
        //请求金额
        BigDecimal requestAmount = rechargeForm.getChangeAmount();
        // 增加金额消耗记录
        rechargeForm.setRecordType(OilCardBalanceRecordTypeEnum.EXPEND.getValue());
        rechargeForm.setChangeAmount(requestAmount.negate());
        oilCardBalanceService.balanceChange(rechargeForm);

        //操作记录
        String content = OilCardDataTracerOperateTypeEnum.EXPEND.getDesc() + "，变动余额：" + rechargeForm.getChangeAmount();
        oilCardDataTracerService.balanceChangeLog(rechargeForm.getOilCardId(), content, OilCardDataTracerOperateTypeEnum.EXPEND, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 挂失圈回
     * 副卡作废后后，副卡增加作废转存记录;主卡增加充值记录，影响副卡为当前作废卡ID
     *
     * @param changeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancel(OilCardBalanceChangeForm changeForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardId = changeForm.getOilCardId();
        OilCardEntity slaveOilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == slaveOilCardEntity || slaveOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (slaveOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (OilCardTypeEnum.MASTER.equalsValue(slaveOilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有副卡允许挂失圈回");
        }
        // 查询主卡信息
        OilCardEntity dbMasterOilCardEntity = oilCardDao.selectById(slaveOilCardEntity.getMasterOilCardId());
        if (null == dbMasterOilCardEntity || dbMasterOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (dbMasterOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "主卡已被禁用");
        }
        //请求金额
        BigDecimal requestAmount = changeForm.getChangeAmount();

        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CANCEL_RECHARGE.getValue());
        changeForm.setChangeAmount(requestAmount.negate());
        //作废操作的卡
        changeForm.setDisabledOperateCardFlag(Boolean.TRUE);
        oilCardBalanceService.balanceChange(changeForm);
        //操作记录
        String slaveContent = OilCardDataTracerOperateTypeEnum.CANCEL_RECHARGE.getDesc() + "，变动余额：" + changeForm.getChangeAmount().toString();
        oilCardDataTracerService.balanceChangeLog(slaveOilCardEntity.getOilCardId(), slaveContent, OilCardDataTracerOperateTypeEnum.CANCEL_RECHARGE, dataTracerRequestForm);

        String masterContent = OilCardDataTracerOperateTypeEnum.CANCEL_RECHARGE.getDesc() + "，副卡【" + slaveOilCardEntity.getOilCardNo() + "】，变动余额：" + changeForm.getChangeAmount().negate();
        oilCardDataTracerService.balanceChangeLog(dbMasterOilCardEntity.getOilCardId(), masterContent, OilCardDataTracerOperateTypeEnum.CANCEL_RECHARGE, dataTracerRequestForm);

        return ResponseDTO.ok();
    }

    /**
     * 圈回
     *
     * @param changeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> circumflex(OilCardBalanceChangeForm changeForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardId = changeForm.getOilCardId();
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "");
        }
        if (OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "主卡没有圈回功能");
        }

        OilCardEntity masterOilCardEntity = oilCardDao.selectById(oilCardEntity.getMasterOilCardId());
        if (null == masterOilCardEntity || masterOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        //请求金额
        BigDecimal requestAmount = changeForm.getChangeAmount();

        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CIRCUMFLEX.getValue());
        changeForm.setChangeAmount(requestAmount.negate());
        oilCardBalanceService.balanceChange(changeForm);
        //操作记录
        String slaveContent = OilCardDataTracerOperateTypeEnum.CIRCUMFLEX.getDesc() + "，变动余额：" + changeForm.getChangeAmount().toString();
        oilCardDataTracerService.balanceChangeLog(oilCardEntity.getOilCardId(), slaveContent, OilCardDataTracerOperateTypeEnum.CIRCUMFLEX, dataTracerRequestForm);

        String masterContent = OilCardDataTracerOperateTypeEnum.CIRCUMFLEX.getDesc() + "，副卡【" + oilCardEntity.getOilCardNo() + "】，变动余额：" + changeForm.getChangeAmount().negate();
        oilCardDataTracerService.balanceChangeLog(masterOilCardEntity.getOilCardId(), masterContent, OilCardDataTracerOperateTypeEnum.CIRCUMFLEX, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 分配副卡余额
     *
     * @param changeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> distributeSlaveCard(OilCardBalanceChangeForm changeForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardId = changeForm.getOilCardId();
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "");
        }
        if (OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "主卡没有分配副卡功能");
        }

        OilCardEntity masterOilCardEntity = oilCardDao.selectById(oilCardEntity.getMasterOilCardId());
        if (null == masterOilCardEntity || masterOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        //请求金额
        BigDecimal requestAmount = changeForm.getChangeAmount();
        if (!SmartBigDecimalUtil.isGreaterOrEqual(masterOilCardEntity.getBalance(), requestAmount)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "主卡余额不足，主卡余额为：" + masterOilCardEntity.getBalance());
        }

        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.DISTRIBUTE_SLAVE_CARD.getValue());
        changeForm.setChangeAmount(requestAmount);
        oilCardBalanceService.balanceChange(changeForm);
        //操作记录
        String slaveContent = OilCardDataTracerOperateTypeEnum.DISTRIBUTE_SLAVE_CARD.getDesc() + "，变动余额：" + changeForm.getChangeAmount();
        oilCardDataTracerService.balanceChangeLog(oilCardEntity.getOilCardId(), slaveContent, OilCardDataTracerOperateTypeEnum.DISTRIBUTE_SLAVE_CARD, dataTracerRequestForm);

        String masterContent = OilCardDataTracerOperateTypeEnum.DISTRIBUTE_SLAVE_CARD.getDesc() + "，副卡【" + oilCardEntity.getOilCardNo() + "】，变动余额：" + changeForm.getChangeAmount().negate();
        oilCardDataTracerService.balanceChangeLog(masterOilCardEntity.getOilCardId(), masterContent, OilCardDataTracerOperateTypeEnum.DISTRIBUTE_SLAVE_CARD, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量更新油卡公司
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateEnterprise(SlaveOilCardEnterpriseUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> oilCardIdList = updateForm.getOilCardIdList();
        List<Long> enterpriseIdList = updateForm.getOilCardIdList();

        return ResponseDTO.ok();
    }

    /**
     * 油卡充值作废
     * 作废后，副卡增加油卡充值作废扣钱记录;主卡增加油卡充值作废还钱记录
     *
     * @param oilCardId             副卡ID
     * @param requestAmount         作废金额
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelOilCardRecharge(Long oilCardId, BigDecimal requestAmount, DataTracerRequestForm dataTracerRequestForm) {
        OilCardEntity slaveOilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == slaveOilCardEntity || slaveOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (slaveOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (OilCardTypeEnum.MASTER.equalsValue(slaveOilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有副卡允许挂失圈回");
        }
        // 查询主卡信息
        OilCardEntity dbMasterOilCardEntity = oilCardDao.selectById(slaveOilCardEntity.getMasterOilCardId());
        if (null == dbMasterOilCardEntity || dbMasterOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (dbMasterOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "主卡已被禁用");
        }
        //请求金额
        OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
        changeForm.setOilCardId(oilCardId);
        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CANCEL_OIL_CARD_RECHARGE.getValue());
        changeForm.setChangeAmount(requestAmount.negate());
        changeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        changeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        oilCardBalanceService.balanceChange(changeForm);
        //操作记录
        String slaveContent = OilCardDataTracerOperateTypeEnum.CANCEL_OIL_CARD_RECHARGE.getDesc() + "，变动余额：" + changeForm.getChangeAmount().toString();
        oilCardDataTracerService.balanceChangeLog(slaveOilCardEntity.getOilCardId(), slaveContent, OilCardDataTracerOperateTypeEnum.CANCEL_OIL_CARD_RECHARGE, dataTracerRequestForm);

        String masterContent = OilCardDataTracerOperateTypeEnum.CANCEL_OIL_CARD_RECHARGE.getDesc() + "，副卡【" + slaveOilCardEntity.getOilCardNo() + "】，变动余额：" + changeForm.getChangeAmount().negate();
        oilCardDataTracerService.balanceChangeLog(dbMasterOilCardEntity.getOilCardId(), masterContent, OilCardDataTracerOperateTypeEnum.CANCEL_OIL_CARD_RECHARGE, dataTracerRequestForm);

        return ResponseDTO.ok();
    }

    /**
     * 导入油卡副卡
     *
     * @return
     */
    public ResponseDTO<String> excelImport(SlaveOilCardImportForm importForm, MultipartFile uploadFile, DataTracerRequestForm dataTracerRequestForm) throws Exception {
        // 文件最大 1M
        long maxSize = 1 * 1024 * 1024L;
        if (uploadFile.getSize() > maxSize) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "上传文件最大1M，如果超过1M，请删除Excel无用Sheet或对Excel进行拆分");
        }
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        List<SlaveOilCardImportDTO> excelImportFormList = ExcelImportUtil.importExcel(uploadFile.getInputStream(), SlaveOilCardImportDTO.class, params);
        if (CollectionUtils.isEmpty(excelImportFormList)) {
            return ResponseDTO.userErrorParam("没有需要导入的数据");
        }
        List<SlaveOilCardImportDTO> filterList = excelImportFormList.stream().filter(e -> StringUtils.isNotBlank(e.getOilCardNo())).collect(Collectors.toList());


        Optional<SlaveOilCardImportDTO> importFormOptional = filterList.stream().filter(e ->
                StringUtils.isBlank(e.getOilCardNo()) || StringUtils.isBlank(e.getMasterCardNo()) || StringUtils.isBlank(e.getBrand())
                        || StringUtils.isBlank(e.getPassword()) || StringUtils.isBlank(e.getCreateUserName())
        ).findFirst();
        if (importFormOptional.isPresent()) {
            return ResponseDTO.userErrorParam("副卡全称、主卡全称、油卡品牌、密码、办卡人不能为空");
        }
        //清理空格
        this.clearExcelDataBlank(filterList);
        //剔除重复ETC
        Set<String> oilCardNoList = filterList.stream().map(SlaveOilCardImportDTO::getOilCardNo).collect(Collectors.toSet());
        if (oilCardNoList.size() != filterList.size()) {
            return ResponseDTO.userErrorParam("EXCEL中存在重复的副卡");
        }
        List<OilCardEntity> existEntityList = oilCardDao.selectByOilCardNo(oilCardNoList, Boolean.FALSE, null, NumberConst.DEFAULT_PARENT_ID);
        if (CollectionUtils.isNotEmpty(existEntityList)) {
            Set<String> existList = existEntityList.stream().map(OilCardEntity::getOilCardNo).collect(Collectors.toSet());
            return ResponseDTO.userErrorParam("副卡【" + StringUtils.join(existList, ",") + "】已存在");
        }

        Set<String> masterOilCardNoList = filterList.stream().map(SlaveOilCardImportDTO::getMasterCardNo).collect(Collectors.toSet());
        List<OilCardEntity> existMasterEntityList = oilCardDao.selectByOilCardNo(masterOilCardNoList, Boolean.FALSE, NumberConst.DEFAULT_PARENT_ID, null);
        List<String> existMaterCardNoList = existMasterEntityList.stream().map(OilCardEntity::getOilCardNo).collect(Collectors.toList());
        // 查询数据库不存在的主卡数据
        List<String> noExistsMasterCardNoList = masterOilCardNoList.stream().filter(e -> !existMaterCardNoList.contains(e)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(noExistsMasterCardNoList)) {
            return ResponseDTO.userErrorParam("主卡【" + StringUtils.join(noExistsMasterCardNoList, ",") + "】不存在");
        }

        Set<String> createUserNameList = filterList.stream().map(SlaveOilCardImportDTO::getCreateUserName).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectByActualNameList(importForm.getEnterpriseId(), createUserNameList, Boolean.FALSE, Boolean.FALSE);
        if (employeeList.size() != createUserNameList.size()) {
            List<String> actualNameList = employeeList.stream().map(EmployeeEntity::getActualName).collect(Collectors.toList());
            Set<String> notExistList = createUserNameList.stream().filter(e -> !actualNameList.contains(e)).collect(Collectors.toSet());
            return ResponseDTO.userErrorParam("办卡人【" + StringUtils.join(notExistList, ",") + "】不存在");
        }

        List<DictValueVO> dictValueList = dictCacheService.selectByKeyCode("OIL-CARD-BRAND");
        Map<String, String> dictCodeMap = dictValueList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));
        Set<String> brandSet = filterList.stream().map(SlaveOilCardImportDTO::getBrand).collect(Collectors.toSet());
        Set<String> notExistsBrand = brandSet.stream().filter(e -> !dictCodeMap.containsKey(e)).collect(Collectors.toSet());
        if (notExistsBrand.size() > 0) {
            return ResponseDTO.userErrorParam("油卡品牌【" + StringUtils.join(notExistsBrand, ",") + "】不存在");
        }

        // 主卡信息
        Map<String, Long> masterMap = existMasterEntityList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, OilCardEntity::getOilCardId));
        // 创建人
        Map<String, Long> employeeMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));
        // 品牌
        List<OilCardEntity> oilCardEntityList = buildImportEntityList(importForm.getEnterpriseId(), filterList, masterMap, employeeMap, dictCodeMap, importForm.getFuelType());
        oilCardManager.saveBatch(oilCardEntityList);
        oilCardDataTracerService.batchImportLog(oilCardEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private List<OilCardEntity> buildImportEntityList(Long enterpriseId, List<SlaveOilCardImportDTO> excelImportFormList, Map<String, Long> masterMap, Map<String, Long> employeeMap, Map<String, String> dictCodeMap, @NotNull(message = "燃料类型不能为空") Integer fuelType) {
        return excelImportFormList.stream().map(importDTO -> {
            OilCardEntity oilCardEntity = new OilCardEntity();
            oilCardEntity.setEnterpriseId(enterpriseId);
            oilCardEntity.setOilCardNo(importDTO.getOilCardNo());
            oilCardEntity.setType(OilCardTypeEnum.SLAVE.getValue());
            oilCardEntity.setFuelType(fuelType);
            oilCardEntity.setMasterOilCardId(masterMap.get(importDTO.getMasterCardNo()));
            oilCardEntity.setBrand(dictCodeMap.get(importDTO.getBrand()));
            oilCardEntity.setBeginBalance(importDTO.getBeginBalance());
            oilCardEntity.setPassword(importDTO.getPassword());
            oilCardEntity.setCreateUserId(employeeMap.get(importDTO.getCreateUserName()));
            oilCardEntity.setCreateUserName(importDTO.getCreateUserName());
            return oilCardEntity;
        }).collect(Collectors.toList());
    }

    private List<OilCardEnterpriseEntity> buildOilCardEnterprise(List<OilCardEntity> oilCardEntityList, Long enterpriseId) {
        List<OilCardEnterpriseEntity> oilCardEnterpriseEntityList = Lists.newArrayList();
        for (OilCardEntity oilCardEntity : oilCardEntityList) {
            OilCardEnterpriseEntity oilCardEnterpriseEntity = new OilCardEnterpriseEntity();
            oilCardEnterpriseEntity.setOilCardId(oilCardEntity.getOilCardId());
            oilCardEnterpriseEntity.setEnterpriseId(enterpriseId);
            oilCardEnterpriseEntityList.add(oilCardEnterpriseEntity);
        }
        return oilCardEnterpriseEntityList;
    }

    private void clearExcelDataBlank(List<SlaveOilCardImportDTO> excelImportFormList) {
        for (SlaveOilCardImportDTO importForm : excelImportFormList) {
            importForm.setOilCardNo(importForm.getOilCardNo().trim());
            importForm.setMasterCardNo(importForm.getMasterCardNo().trim());
            importForm.setBrand(importForm.getBrand().trim());
            importForm.setPassword(importForm.getPassword().trim());
            importForm.setCreateUserName(importForm.getCreateUserName().trim());
            if (null == importForm.getBeginBalance()) {
                importForm.setBeginBalance(BigDecimal.ZERO);
            }
        }
    }

    /**
     * 查询油卡不分页的列表
     *
     * @param vehicleId
     * @return
     */
    public ResponseDTO<List<OilCardSimpleVO>> queryListByVehicleId(Long vehicleId) {
        List<OilCardSimpleVO> oilCardList = oilCardDao.queryListByVehicleId(vehicleId, OilCardTypeEnum.SLAVE.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(oilCardList);
    }

    /**
     * changeForm 必须设置 oilCardId,changeAmount,recordType,
     *
     * @param changeForm
     * @param operateType
     * @param dataTracerRequestForm
     */
    public void carCostChangeBalance(OilCardBalanceChangeForm changeForm, OilCardDataTracerOperateTypeEnum operateType, DataTracerRequestForm dataTracerRequestForm) {
        if (null == changeForm.getTransactionTime()) {
            changeForm.setTransactionTime(LocalDateTime.now());
        }
        changeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        changeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        changeForm.setDisabledOperateCardFlag(Boolean.FALSE);
        oilCardBalanceService.balanceChange(changeForm);

        //操作记录
        OilCardBalanceRecordTypeEnum recordTypeEnum = SmartBaseEnumUtil.getEnumByValue(changeForm.getRecordType(), OilCardBalanceRecordTypeEnum.class);
        String content = recordTypeEnum.getDesc() + "，变动余额：" + changeForm.getChangeAmount();
        oilCardDataTracerService.balanceChangeLog(changeForm.getOilCardId(), content, operateType, dataTracerRequestForm);
    }

    /**
     * 更新计划充值金额
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updatePreRechargeAmount(OilCardPreRechargeAmountUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(updateForm.getOilCardId());
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (!OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有主卡允许充值");
        }
        // 更新预计充值金额
        OilCardEntity updateOilCardEntity = new OilCardEntity();
        updateOilCardEntity.setOilCardId(updateForm.getOilCardId());
        updateOilCardEntity.setPreRechargeAmount(updateForm.getPreRechargeAmount());
        oilCardDao.updateById(updateOilCardEntity);
        //操作记录
        oilCardDataTracerService.updatePreRechargeAmountLog(updateForm.getOilCardId(), updateForm.getPreRechargeAmount(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新计划充值金额
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> setPreRechargeAmount(OilCardPreRechargeAmountUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(updateForm.getOilCardId());
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡已被禁用");
        }
        if (!OilCardTypeEnum.MASTER.equalsValue(oilCardEntity.getType())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只有主卡允许充值");
        }
        // 更新预计充值金额
        OilCardEntity updateOilCardEntity = new OilCardEntity();
        updateOilCardEntity.setOilCardId(updateForm.getOilCardId());
        updateOilCardEntity.setPreRechargeAmount(SmartBigDecimalUtil.Amount.add(oilCardEntity.getPreRechargeAmount(), updateForm.getPreRechargeAmount()));
        oilCardDao.updateById(updateOilCardEntity);
        //操作记录
        oilCardDataTracerService.setPreRechargeAmountLog(updateForm.getOilCardId(), updateForm.getPreRechargeAmount(), oilCardEntity.getPreRechargeAmount(), updateOilCardEntity.getPreRechargeAmount(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 根据计划充值金额充值
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> rechargeByPreRechargeAmount(OilCardBalanceChangeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardId = rechargeForm.getOilCardId();
        BigDecimal rechargeAmount = rechargeForm.getChangeAmount();
        OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
        if (null == oilCardEntity || oilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (oilCardEntity.getPreRechargeAmount().compareTo(rechargeAmount) < 0) {
            return ResponseDTO.userErrorParam("充值金额不能大于计划充值金额");
        }
        ResponseDTO<String> rechargeResponse =  this.recharge(rechargeForm, dataTracerRequestForm);
        if(!rechargeResponse.getOk()){
            return rechargeResponse;
        }
        OilCardEntity updateEntity = new OilCardEntity();
        updateEntity.setOilCardId(oilCardId);
        updateEntity.setPreRechargeAmount(SmartBigDecimalUtil.Amount.subtract(oilCardEntity.getPreRechargeAmount(), rechargeAmount));
        oilCardDao.updateById(updateEntity);
        oilCardDataTracerService.rechargeByPreRechargeAmountLog(oilCardId, oilCardEntity.getPreRechargeAmount(), updateEntity.getPreRechargeAmount(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
