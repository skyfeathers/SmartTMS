package net.lab1024.tms.admin.module.business.receive.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.oa.bank.BankDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderInvoiceDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderVerificationDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderVerificationAmountDTO;
import net.lab1024.tms.admin.module.business.receive.domain.form.*;
import net.lab1024.tms.admin.module.business.receive.domain.vo.*;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderManage;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderVerificationManage;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperMailAddressDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperService;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.*;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillCostAmountUpdateBO;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillDataTracerService;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillPathService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.oa.bank.BankEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderInvoiceEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderVerificationEntity;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillPathEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 应收帐款Service
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:26
 */
@Service
public class ReceiveOrderService {

    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillPathDao waybillPathDao;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private WaybillPathService waybillPathService;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private BankDao bankDao;
    @Autowired
    private ReceiveOrderManage receiveOrderManage;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ReceiveOrderCommonService receiveOrderCommonService;
    @Autowired
    private WaybillDataTracerService waybillDataTracerService;
    @Autowired
    private ReceiveOrderDataTracerService receiveOrderDataTracerService;
    @Autowired
    private ReceiveOrderVerificationManage receiveOrderVerificationManage;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private WaybillReceiveCostDao waybillReceiveCostDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;

    /**
     * 提交应收对账展示的货主信息
     *
     * @param waybillIdList
     * @return
     */
    public ResponseDTO<ReceiveOrderSubmitVO> getShipperCheckInfo(List<Long> waybillIdList, Long enterpriseId) {
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        waybillEntityList = waybillEntityList.stream().filter(e -> WaybillStatusEnum.COMPLETE.equalsValue(e.getWaybillStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(waybillEntityList) || waybillIdList.size() != waybillEntityList.size()) {
            return ResponseDTO.userErrorParam("存在尚未运输完成的运单");
        }
        ResponseDTO<String> submitCheck = this.submitCheck(waybillEntityList);
        if (!submitCheck.getOk()) {
            return ResponseDTO.error(submitCheck);
        }
        //随便一个
        WaybillEntity waybillEntity = waybillEntityList.get(NumberConst.ZERO);
        Long shipperId = waybillEntity.getShipperId();
        //应收总金额
        BigDecimal receiveTotalAmount = waybillDao.sumReceiveAmount(waybillIdList, WaybillStatusEnum.CANCEL.getValue());
        //货主信息
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);

        ReceiveOrderSubmitVO checkVO = SmartBeanUtil.copy(shipperEntity, ReceiveOrderSubmitVO.class);
        checkVO.setInvoiceAmount(receiveTotalAmount);
        checkVO.setEnterpriseId(enterpriseId);
        checkVO.setBusinessDate(waybillEntity.getBusinessDate());
        return ResponseDTO.ok(checkVO);
    }

    /**
     * 提交核算
     *
     * @param submitForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> submit(ReceiveOrderSubmitForm submitForm, DataTracerRequestForm dataTracerRequestForm) {
        ReceiveOrderForm addForm = new ReceiveOrderForm();
        addForm.setWaybillIdList(submitForm.getWaybillIdList());
        addForm.setCreateUserId(submitForm.getCreateUserId());
        addForm.setCreateUserName(submitForm.getCreateUserName());
        addForm.setMakeInvoiceFlag(Boolean.FALSE);
        addForm.setBusinessDate(submitForm.getBusinessDate());
        addForm.setAttachment(submitForm.getAttachment());
        addForm.setRemark(submitForm.getRemark());
        addForm.setEnterpriseId(submitForm.getEnterpriseId());
        return this.save(addForm, dataTracerRequestForm);
    }

    /**
     * 提交核算并申请开票
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> submitInvoice(ReceiveOrderForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        addForm.setMakeInvoiceFlag(Boolean.TRUE);
        return this.save(addForm, dataTracerRequestForm);
    }

    /**
     * 提交收款校验
     *
     * @param waybillEntityList
     * @return
     */
    public ResponseDTO<String> submitCheck(List<WaybillEntity> waybillEntityList) {
        Set<Long> shipperIdSet = waybillEntityList.stream().map(WaybillEntity::getShipperId).collect(Collectors.toSet());
        if (shipperIdSet.size() > 1) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只能选择同一货主的运单");
        }
        Set<Long> enterpriseIdSet = waybillEntityList.stream().map(WaybillEntity::getEnterpriseId).collect(Collectors.toSet());
        if (enterpriseIdSet.size() > 1) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只能选择同一企业的运单");
        }
        List<String> submitReceiveList = waybillEntityList.stream().filter(e -> e.getSubmitReceiveFlag()).map(WaybillEntity::getWaybillNumber).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(submitReceiveList)) {
            String orderNo = SmartStringUtil.join("、", submitReceiveList);
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, String.format("【%s】已经提交财务核算，不能重复提交", orderNo));
        }
        return ResponseDTO.ok();
    }

    /**
     * 客服提交对账信息
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(ReceiveOrderForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> waybillIdList = addForm.getWaybillIdList();
        // 验证提交参数
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        waybillEntityList = waybillEntityList.stream().filter(e -> WaybillStatusEnum.COMPLETE.equalsValue(e.getWaybillStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(waybillEntityList) || waybillIdList.size() != waybillEntityList.size()) {
            return ResponseDTO.userErrorParam("存在尚未运输完成的运单");
        }
        ResponseDTO<String> submitCheck = this.submitCheck(waybillEntityList);
        if (!submitCheck.getOk()) {
            return ResponseDTO.error(submitCheck);
        }
        //随便一个
        WaybillEntity waybillEntity = waybillEntityList.get(0);
        Long shipperId = waybillEntity.getShipperId();
        Long enterpriseId = waybillEntity.getEnterpriseId();
        //单号
        String receiveOrderNumber = serialNumberService.generate(SerialNumberIdEnum.RECEIVE_ORDER);
        //应收总金额
        BigDecimal receiveTotalAmount = waybillDao.sumReceiveAmount(waybillIdList, WaybillStatusEnum.CANCEL.getValue());

        ReceiveOrderEntity receiveOrderEntity = SmartBeanUtil.copy(addForm, ReceiveOrderEntity.class);
        receiveOrderEntity.setShipperId(shipperId);
        receiveOrderEntity.setEnterpriseId(enterpriseId);
        receiveOrderEntity.setReceiveOrderNumber(receiveOrderNumber);
        receiveOrderEntity.setTotalAmount(receiveTotalAmount);
        receiveOrderEntity.setBusinessDate(addForm.getBusinessDate());
        receiveOrderEntity.setAttachment(addForm.getAttachment());
        receiveOrderEntity.setCheckStatus(CheckStatusEnum.WAIT_CHECK.getValue());
        receiveOrderManage.submit(receiveOrderEntity, addForm);
        // 增加订单操作记录
        waybillDataTracerService.submitReceiveLog(waybillIdList, addForm, dataTracerRequestForm, receiveOrderEntity.getReceiveOrderId(), addForm);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReceiveOrderVO>> queryByPage(ReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReceiveOrderVO> receiveOrderList = receiveOrderDao.queryByPage(page, queryForm);
        //已销金额 未销金额
        receiveOrderCommonService.buildAmount(receiveOrderList);
        // 设置货主负责人和逾期天数
        this.buildShipperManager(receiveOrderList);
        this.buildOverdueDays(receiveOrderList);
        PageResult<ReceiveOrderVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, receiveOrderList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 设置逾期天数
     *
     * @param receiveOrderList
     */
    public void buildOverdueDays(List<ReceiveOrderVO> receiveOrderList) {
        if (CollectionUtil.isEmpty(receiveOrderList)) {
            return;
        }

        LocalDate now = LocalDate.now();
        for (ReceiveOrderVO receiveOrderVO : receiveOrderList) {
            LocalDate accountPeriodDate = receiveOrderVO.getAccountPeriodDate();
            if (accountPeriodDate == null) {
                continue;
            }
            Long overDueDays = ChronoUnit.DAYS.between(accountPeriodDate, now);
            // 未逾期设置逾期天数为0
            if (overDueDays < 0) {
                overDueDays = 0L;
            }
            receiveOrderVO.setOverdueDays(overDueDays.intValue());
        }
    }

    /**
     * 设置货主负责人
     *
     * @param receiveOrderList
     */
    public void buildShipperManager(List<ReceiveOrderVO> receiveOrderList) {
        if (CollectionUtil.isEmpty(receiveOrderList)) {
            return;
        }

        List<Long> shipperIdList = receiveOrderList.stream().map(ReceiveOrderVO::getShipperId).distinct().collect(Collectors.toList());
        List<ShipperPrincipalDTO> principalDTOList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, ShipperPrincipalDTO> shipperMap = principalDTOList.stream().collect(Collectors.toMap(ShipperPrincipalDTO::getShipperId, Function.identity()));
        for (ReceiveOrderVO receiveOrderVO : receiveOrderList) {
            ShipperPrincipalDTO shipperPrincipalDTO = shipperMap.get(receiveOrderVO.getShipperId());
            if (shipperPrincipalDTO == null) {
                continue;
            }
            receiveOrderVO.setManagerId(shipperPrincipalDTO.getEmployeeId());
            receiveOrderVO.setManagerName(shipperPrincipalDTO.getEmployeeName());
        }
    }

    /**
     * 统计应收金额、已销金额、未销金额
     *
     * @return
     */
    public ResponseDTO<ReceiveAmountStatisticsVO> amountStatistics(ReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        BigDecimal receivableAmount = receiveOrderDao.getReceivableTotalAmount(queryForm);
        BigDecimal verificationAmount = receiveOrderDao.getVerificationTotalAmount(queryForm);
        BigDecimal overdueTotalAmount = BigDecimal.ZERO;
        if (!BooleanUtils.isFalse(queryForm.getOverdue())){
            queryForm.setOverdue(true);
            BigDecimal overdueReceivableTotalAmount = receiveOrderDao.getReceivableTotalAmount(queryForm);
            BigDecimal overdueVerificationTotalAmount = receiveOrderDao.getVerificationTotalAmount(queryForm);
            overdueTotalAmount = overdueReceivableTotalAmount.subtract(overdueVerificationTotalAmount);
        }
        ReceiveAmountStatisticsVO amountStatisticsVO = new ReceiveAmountStatisticsVO();
        amountStatisticsVO.setReceivableTotalAmount(ObjectUtil.isEmpty(receivableAmount) ? BigDecimal.ZERO : receivableAmount);
        amountStatisticsVO.setVerificationTotalAmount(ObjectUtil.isEmpty(verificationAmount) ? BigDecimal.ZERO : verificationAmount);
        amountStatisticsVO.setUnpaidTotalAmount(amountStatisticsVO.getReceivableTotalAmount().subtract(amountStatisticsVO.getVerificationTotalAmount()));
        amountStatisticsVO.setOverdueTotalAmount(overdueTotalAmount);
        return ResponseDTO.ok(amountStatisticsVO);
    }

    /**
     * 查询导出数据
     *
     * @param queryForm
     * @return
     */
    public List<ReceiveOrderVO> query4Export(ReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        List<ReceiveOrderVO> receiveOrderList = receiveOrderDao.queryByPage(null, queryForm);
        //已销金额 未销金额
        receiveOrderCommonService.buildAmount(receiveOrderList);
        receiveOrderList.forEach(item -> {
            String cancelRemark = "";
            if (CheckStatusEnum.CANCEL.equalsValue(item.getCheckStatus())) {
                cancelRemark = "已作废";
                if (StringUtils.isNotEmpty(item.getCheckRemark())) {
                    cancelRemark += "(" + item.getCheckRemark() + ")";
                }
            }
            if (InvoiceStatusEnum.CANCEL.equalsValue(item.getInvoiceStatus()) && StringUtils.isNotEmpty(item.getInvoiceRemark())) {
                cancelRemark += "开票申请被作废(" + item.getInvoiceRemark() + ")";
            }
            item.setCancelRemark(cancelRemark);
        });
        return receiveOrderList;
    }

    /**
     * 查询对账单详情
     *
     * @param receiveOrderId
     * @return
     */
    public ResponseDTO<ReceiveOrderDetailVO> getDetail(Long receiveOrderId) {
        ReceiveOrderEntity dbReceiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (null == dbReceiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单不存在");
        }
        ReceiveOrderDetailVO receiveOrderDetailVO = receiveOrderDao.getDetail(receiveOrderId);
        //核销
        List<ReceiveOrderVerificationVO> verificationVOList = receiveOrderVerificationDao.selectByReceiveOrderId(receiveOrderId);
        receiveOrderDetailVO.setRecordList(verificationVOList);
        //运单
        List<ReceiveOrderWaybillVO> waybillList = receiveOrderCommonService.getWaybillByReceive(receiveOrderId);
        receiveOrderDetailVO.setWaybillList(waybillList);
        //发票
        List<ReceiveOrderInvoiceVO> invoiceVOList = receiveOrderCommonService.getInvoiceList(Arrays.asList(receiveOrderId));
        receiveOrderDetailVO.setInvoiceList(invoiceVOList);
        //已销金额 未销金额
        receiveOrderCommonService.buildAmount(Arrays.asList(receiveOrderDetailVO));
        return ResponseDTO.ok(receiveOrderDetailVO);
    }


    /**
     * 作废对账单
     *
     * @param cancelForm
     * @return
     */
    public ResponseDTO<String> cancel(ReceiveOrderCancelForm cancelForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderId = cancelForm.getReceiveOrderId();
        ReceiveOrderEntity receiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (null == receiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单不存在");
        }
        if (!CheckStatusEnum.WAIT_CHECK.equalsValue(receiveOrderEntity.getCheckStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "收款单已核算或已作废");
        }
        receiveOrderEntity.setCheckStatus(CheckStatusEnum.CANCEL.getValue());
        receiveOrderEntity.setCheckRemark(cancelForm.getRemark());
        receiveOrderEntity.setCheckUserId(cancelForm.getReceiveOrderId());
        receiveOrderEntity.setInvoiceStatus(InvoiceStatusEnum.CANCEL.getValue());
        receiveOrderManage.cancel(receiveOrderEntity, cancelForm.getOperateUserId(), null);
        receiveOrderDataTracerService.cancelCheckLog(cancelForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 确认对账单
     *
     * @param checkForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> confirmCheck(ReceiveCheckForm checkForm, DataTracerRequestForm dataTracerRequestForm) {
        ReceiveOrderEntity dbReceiveOrderEntity = receiveOrderDao.selectById(checkForm.getReceiveOrderId());
        if (null == dbReceiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单不存在");
        }
        if (CheckStatusEnum.CANCEL.equalsValue(dbReceiveOrderEntity.getCheckStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单已作废，不能核算哦");
        }
        if (CheckStatusEnum.CHECK.equalsValue(dbReceiveOrderEntity.getCheckStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "不能重复确认核算哦");
        }
        ReceiveOrderEntity receiveOrderEntity = SmartBeanUtil.copy(checkForm, ReceiveOrderEntity.class);
        receiveOrderEntity.setCheckStatus(CheckStatusEnum.CHECK.getValue());
        // 不需要开票，直接生成账期,核算完 默认已开票
        if (!dbReceiveOrderEntity.getMakeInvoiceFlag()) {
            receiveOrderEntity.setInvoiceStatus(InvoiceStatusEnum.INVOICE.getValue());
            LocalDate accountPeriod = receiveOrderCommonService.generateAccountPeriod(checkForm.getCheckTime(), dbReceiveOrderEntity.getShipperId());
            receiveOrderEntity.setAccountPeriodDate(accountPeriod);
        }
        receiveOrderDao.updateById(receiveOrderEntity);
        receiveOrderDataTracerService.checkLog(checkForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 核销
     *
     * @param verificationForm
     * @return
     */
    public ResponseDTO<String> verification(ReceiveVerificationForm verificationForm, DataTracerRequestForm dataTracerRequestForm) {
        ReceiveOrderEntity dbReceiveOrderEntity = receiveOrderDao.selectById(verificationForm.getReceiveOrderId());
        if (null == dbReceiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单不存在");
        }
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(verificationForm.getEnterpriseId());
        if (null == enterpriseEntity || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款企业不存在");
        }
        if (!dbReceiveOrderEntity.getEnterpriseId().equals(verificationForm.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("收款单所属企业和当前登录企业不一致");
        }
        BankEntity bankEntity = bankDao.selectById(verificationForm.getBankId());
        if (null == bankEntity || bankEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款银行不存在");
        }
        Boolean verificationFlag = Boolean.FALSE;
        BigDecimal verificationAmount = receiveOrderVerificationDao.sumByReceiveOrderId(verificationForm.getReceiveOrderId());
        BigDecimal totalVerificationAmount = SmartBigDecimalUtil.Amount.add(verificationAmount, verificationForm.getVerificationAmount());
        if (SmartBigDecimalUtil.isGreaterOrEqual(totalVerificationAmount, dbReceiveOrderEntity.getTotalAmount())) {
            verificationFlag = Boolean.TRUE;
        }

        ReceiveOrderEntity updateReceiveOrderEntity = new ReceiveOrderEntity();
        updateReceiveOrderEntity.setReceiveOrderId(verificationForm.getReceiveOrderId());
        updateReceiveOrderEntity.setVerificationFlag(verificationFlag);

        ReceiveOrderVerificationEntity recordEntity = SmartBeanUtil.copy(verificationForm, ReceiveOrderVerificationEntity.class);
        recordEntity.setBankName(bankEntity.getBankName());
        recordEntity.setAccountName(bankEntity.getAccountName());
        recordEntity.setAccountNumber(bankEntity.getAccountNumber());
        receiveOrderManage.verification(updateReceiveOrderEntity, recordEntity);
        receiveOrderDataTracerService.verificationLog(recordEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量核销接口
     *
     * @param batchVerificationForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> batchVerification(ReceiveBatchVerificationForm batchVerificationForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> receiveOrderIdList = batchVerificationForm.getReceiveOrderIdList();
        List<ReceiveOrderEntity> dbReceiveOrderEntityList = receiveOrderDao.selectBatchIds(receiveOrderIdList);
        if (CollectionUtils.isEmpty(dbReceiveOrderEntityList) || dbReceiveOrderEntityList.size() != receiveOrderIdList.size()) {
            return ResponseDTO.userErrorParam("收款单不存在");
        }
        Map<Long, List<ReceiveOrderEntity>> shipperReceiveMap = dbReceiveOrderEntityList.stream().collect(Collectors.groupingBy(ReceiveOrderEntity::getShipperId));
        if (shipperReceiveMap.size() > 1) {
            return ResponseDTO.userErrorParam("只能选择同一客户");
        }
        Set<Long> receiveOrderEnterpriseIdSet = dbReceiveOrderEntityList.stream().map(ReceiveOrderEntity::getEnterpriseId).collect(Collectors.toSet());
        if (receiveOrderEnterpriseIdSet.size() > 1 || !receiveOrderEnterpriseIdSet.contains(batchVerificationForm.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("收款单所属企业和当前登录企业不一致");
        }
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(batchVerificationForm.getEnterpriseId());
        if (null == enterpriseEntity || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("收款企业不存在");
        }
        BankEntity bankEntity = bankDao.selectById(batchVerificationForm.getBankId());
        if (null == bankEntity || bankEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("收款银行不存在");
        }

        // 获取应收总金额
        Map<Long, BigDecimal> receiveAmountMap = dbReceiveOrderEntityList.stream().collect(Collectors.toMap(ReceiveOrderEntity::getReceiveOrderId, ReceiveOrderEntity::getTotalAmount));
        // 获取已核销金额
        List<ReceiveOrderVerificationAmountDTO> verificationAmountList = receiveOrderVerificationDao.sumByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, BigDecimal> verificationAmountMap = verificationAmountList.stream().collect(Collectors.toMap(ReceiveOrderVerificationAmountDTO::getReceiveOrderId, ReceiveOrderVerificationAmountDTO::getVerificationAmount));

        List<ReceiveOrderEntity> updateEntityList = receiveOrderIdList.stream().map(receiveOrderId -> {
            ReceiveOrderEntity updateReceiveOrderEntity = new ReceiveOrderEntity();
            updateReceiveOrderEntity.setReceiveOrderId(receiveOrderId);
            updateReceiveOrderEntity.setVerificationFlag(Boolean.TRUE);
            return updateReceiveOrderEntity;
        }).collect(Collectors.toList());

        List<ReceiveOrderVerificationEntity> recordList = receiveOrderIdList.stream().map(receiveOrderId -> {
            ReceiveOrderVerificationEntity recordEntity = SmartBeanUtil.copy(batchVerificationForm, ReceiveOrderVerificationEntity.class);
            recordEntity.setReceiveOrderId(receiveOrderId);
            recordEntity.setBankName(bankEntity.getBankName());
            recordEntity.setAccountName(bankEntity.getAccountName());
            recordEntity.setAccountNumber(bankEntity.getAccountNumber());

            BigDecimal totalAmount = receiveAmountMap.getOrDefault(receiveOrderId, BigDecimal.ZERO);
            BigDecimal verificationAmount = verificationAmountMap.getOrDefault(receiveOrderId, BigDecimal.ZERO);
            recordEntity.setVerificationAmount(SmartBigDecimalUtil.Amount.subtract(totalAmount, verificationAmount));
            return recordEntity;
        }).collect(Collectors.toList());

        receiveOrderVerificationManage.saveBatch(recordList);
        receiveOrderManage.updateBatchById(updateEntityList);
        receiveOrderDataTracerService.batchVerificationLog(recordList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量核销获取金额统计
     *
     * @param receiveOrderIdList
     * @return
     */
    public ResponseDTO<ReceiveOrderVerificationAmountVO> batchVerificationAmount(List<Long> receiveOrderIdList) {
        List<ReceiveOrderEntity> receiveOrderEntityList = receiveOrderDao.selectBatchIds(receiveOrderIdList);
        if (receiveOrderEntityList.size() != receiveOrderIdList.size()) {
            return ResponseDTO.userErrorParam("数据异常，请刷新后再试");
        }
        List<ReceiveOrderVerificationAmountDTO> verificationAmountList = receiveOrderVerificationDao.sumByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, List<ReceiveOrderEntity>> receiveOrderMap = receiveOrderEntityList.stream().collect(Collectors.groupingBy(ReceiveOrderEntity::getShipperId));
        if (receiveOrderMap.size() > 1) {
            return ResponseDTO.userErrorParam("只能选择同一客户进行核销");
        }


        BigDecimal totalAmount = receiveOrderEntityList.stream().map(ReceiveOrderEntity::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal verificationAmount = verificationAmountList.stream().map(ReceiveOrderVerificationAmountDTO::getVerificationAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        ReceiveOrderVerificationAmountVO receiveOrderVerificationAmountVO = new ReceiveOrderVerificationAmountVO();
        receiveOrderVerificationAmountVO.setTotalAmount(totalAmount);
        receiveOrderVerificationAmountVO.setVerificationAmount(verificationAmount);
        receiveOrderVerificationAmountVO.setUnpaidAmount(SmartBigDecimalUtil.Amount.subtract(totalAmount, verificationAmount));
        return ResponseDTO.ok(receiveOrderVerificationAmountVO);
    }

    /**
     * 分页查询待收款的订单信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReceiveOrderAmountVO>> queryWaitReceiveByPage(WaitReceiveQueryForm queryForm) {
        queryForm.setVerificationFlag(Boolean.FALSE);
        queryForm.setCheckStatus(CheckStatusEnum.CHECK.getValue());
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        queryForm.setInvoiceStatusList(Lists.newArrayList(InvoiceStatusEnum.PORTION_INVOICE.getValue(), InvoiceStatusEnum.INVOICE.getValue()));

        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReceiveOrderAmountVO> receiveOrderAmountVOList = receiveOrderDao.queryWaitReceiveList(page, queryForm);

        this.buildVerificationAmount(receiveOrderAmountVOList);
        // 设置货主负责人和逾期天数
        this.buildOrderAmountShipperManager(receiveOrderAmountVOList);
        this.buildOrderAmountOverdueDays(receiveOrderAmountVOList);
        PageResult<ReceiveOrderAmountVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, receiveOrderAmountVOList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 设置货主负责人
     *
     * @param receiveOrderAmountVOList
     */
    public void buildOrderAmountShipperManager(List<ReceiveOrderAmountVO> receiveOrderAmountVOList) {
        if (CollectionUtil.isEmpty(receiveOrderAmountVOList)) {
            return;
        }

        List<Long> shipperIdList = receiveOrderAmountVOList.stream().map(ReceiveOrderAmountVO::getShipperId).distinct().collect(Collectors.toList());
        List<ShipperPrincipalDTO> principalDTOList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, ShipperPrincipalDTO> shipperManagerMap = principalDTOList.stream().collect(Collectors.toMap(ShipperPrincipalDTO::getShipperId, Function.identity()));
        for (ReceiveOrderAmountVO orderAmountVO : receiveOrderAmountVOList) {
            ShipperPrincipalDTO shipperPrincipalDTO = shipperManagerMap.get(orderAmountVO.getShipperId());
            if (shipperPrincipalDTO == null) {
                continue;
            }
            orderAmountVO.setManagerId(shipperPrincipalDTO.getEmployeeId());
            orderAmountVO.setManagerName(shipperPrincipalDTO.getEmployeeName());
        }
    }

    /**
     * 设置逾期天数
     *
     * @param receiveOrderAmountVOList
     */
    public void buildOrderAmountOverdueDays(List<ReceiveOrderAmountVO> receiveOrderAmountVOList) {
        if (CollectionUtil.isEmpty(receiveOrderAmountVOList)) {
            return;
        }

        LocalDate now = LocalDate.now();
        for (ReceiveOrderAmountVO orderAmountVO : receiveOrderAmountVOList) {
            LocalDate accountPeriodDate = orderAmountVO.getAccountPeriodDate();
            if (accountPeriodDate == null) {
                continue;
            }
            Long overDueDays = ChronoUnit.DAYS.between(accountPeriodDate, now);
            // 未逾期设置逾期天数为0
            if (overDueDays < 0) {
                overDueDays = 0L;
            }
            orderAmountVO.setOverdueDays(overDueDays.intValue());
        }
    }

    /**
     * 根据应收ID设置已销金额
     *
     * @param receiveOrderAmountVOList
     */
    private void buildVerificationAmount(List<ReceiveOrderAmountVO> receiveOrderAmountVOList) {
        if (CollectionUtils.isEmpty(receiveOrderAmountVOList)) {
            return;
        }
        List<Long> receiveOrderIdList = receiveOrderAmountVOList.stream().map(ReceiveOrderAmountVO::getReceiveOrderId).collect(Collectors.toList());
        List<ReceiveOrderVerificationVO> receiveOrderVerificationVOList = receiveOrderVerificationDao.selectByReceiveOrderIdList(receiveOrderIdList);
        // 已销金额Map
        Map<Long, List<ReceiveOrderVerificationVO>> verificationAmountMap = receiveOrderVerificationVOList.stream().collect(Collectors.groupingBy(ReceiveOrderVerificationVO::getReceiveOrderId));
        for (ReceiveOrderAmountVO receiveOrderAmountVO : receiveOrderAmountVOList) {
            List<ReceiveOrderVerificationVO> verificationList = verificationAmountMap.getOrDefault(receiveOrderAmountVO.getReceiveOrderId(), Lists.newArrayList());
            receiveOrderAmountVO.setVerificationAmount(verificationList.stream().map(ReceiveOrderVerificationVO::getVerificationAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            receiveOrderAmountVO.setWaitReceiveAmount(receiveOrderAmountVO.getTotalAmount().subtract(receiveOrderAmountVO.getVerificationAmount()));
            // 取此次应收第一次的核销信息进行设置
            if (CollectionUtils.isNotEmpty(verificationList)) {
                ReceiveOrderVerificationVO verification = verificationList.get(NumberConst.ZERO);
                receiveOrderAmountVO.setVerificationTime(verification.getVerificationTime());
                receiveOrderAmountVO.setVerificationRemark(verification.getRemark());
            }
        }
    }

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReceiveOrderVO>> queryPageByShipper(ShipperReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReceiveOrderVO> receiveOrderList = receiveOrderDao.queryPageByShipper(page, queryForm);
        //已销金额 未销金额
        receiveOrderCommonService.buildAmount(receiveOrderList);
        // 设置货主负责人和逾期天数
        this.buildShipperManager(receiveOrderList);
        this.buildOverdueDays(receiveOrderList);
        PageResult<ReceiveOrderVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, receiveOrderList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 导出查询待收款的信息
     *
     * @param queryForm
     * @return
     */
    public List<ReceiveOrderAmountReportExportVO> exportWaitReceive(WaitReceiveQueryForm queryForm) {
        queryForm.setCheckStatus(CheckStatusEnum.CHECK.getValue());
        queryForm.setInvoiceStatusList(Lists.newArrayList(InvoiceStatusEnum.PORTION_INVOICE.getValue(), InvoiceStatusEnum.INVOICE.getValue()));
        queryForm.setVerificationFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());

        List<ReceiveOrderAmountVO> receiveOrderAmountVOList = receiveOrderDao.queryWaitReceiveList(null, queryForm);
        List<ReceiveOrderAmountReportExportVO> exportList = buildWaitExportList(receiveOrderAmountVOList);
        return exportList;
    }

    /**
     * 构建应收账款报表信息
     *
     * @param receiveOrderAmountVOList
     * @return
     */
    private List<ReceiveOrderAmountReportExportVO> buildWaitExportList(List<ReceiveOrderAmountVO> receiveOrderAmountVOList) {
        if (CollectionUtils.isEmpty(receiveOrderAmountVOList)) {
            return Lists.newArrayList();
        }
        // 设置核销金额，未销金额信息
        this.buildVerificationAmount(receiveOrderAmountVOList);

        List<Long> receiveOrderIdList = receiveOrderAmountVOList.stream().map(ReceiveOrderAmountVO::getReceiveOrderId).collect(Collectors.toList());
        // 获取开票信息
        List<ReceiveOrderInvoiceEntity> invoiceList = receiveOrderInvoiceDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, List<ReceiveOrderInvoiceEntity>> invoiceMap = invoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderInvoiceEntity::getReceiveOrderId));

        // 获取业务人员
        Set<Long> shipperIdList = receiveOrderAmountVOList.stream().map(ReceiveOrderAmountVO::getShipperId).collect(Collectors.toSet());
        Map<Long, List<String>> shipperPrincipalNameMap = shipperService.queryPrincipalByShipperIdType(shipperIdList, PrincipalTypeEnum.MANAGER);

        List<ReceiveOrderAmountReportExportVO> exportList = Lists.newArrayList();
        for (ReceiveOrderAmountVO receiveOrderAmountVO : receiveOrderAmountVOList) {
            Long receiveOrderId = receiveOrderAmountVO.getReceiveOrderId();
            List<ReceiveOrderInvoiceEntity> receiveOrderInvoiceList = invoiceMap.get(receiveOrderId);
            ReceiveOrderAmountReportExportVO exportVO = SmartBeanUtil.copy(receiveOrderAmountVO, ReceiveOrderAmountReportExportVO.class);
            if (ObjectUtil.isNotEmpty(receiveOrderInvoiceList)) {
                ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity = receiveOrderInvoiceList.get(NumberConst.ZERO);
                exportVO.setInvoiceName(receiveOrderInvoiceEntity.getInvoiceName());
                exportVO.setInvoiceTime(receiveOrderInvoiceEntity.getInvoiceTime());
                exportVO.setInvoiceNumber(receiveOrderInvoiceEntity.getInvoiceNumber());
            }
            buildWaitExportVO(exportVO, shipperPrincipalNameMap);
            exportList.add(exportVO);
        }
        return exportList;
    }

    private void buildWaitExportVO(ReceiveOrderAmountReportExportVO exportVO, Map<Long, List<String>> shipperPrincipalNameMap) {
        // 计算逾期天数
        LocalDateTime now = LocalDateTime.now();
        if (null != exportVO.getAccountPeriodDate()) {
            LocalDateTime accountPeriodDateTime = exportVO.getAccountPeriodDate().atStartOfDay();
            Long overdueDays = Duration.between(accountPeriodDateTime, now).toDays();
            exportVO.setOverdueDays(overdueDays.intValue() > 0 ? overdueDays.intValue() : NumberConst.ZERO);
        }
        // 设置业务人员
        Long shipperId = exportVO.getShipperId();
        List<String> managerNameList = shipperPrincipalNameMap.getOrDefault(shipperId, Lists.newArrayList());
        exportVO.setManagerName(SmartStringUtil.join(StringConst.SEPARATOR, managerNameList));
    }

    /**
     * 设置导出内容
     *
     * @param exportVO                导出信息
     * @param enterpriseNameMap       公司名称
     * @param verificationMap         核销信息
     * @param shipperAreaCodeMap      地区
     * @param shipperPrincipalNameMap 负责人
     */
    private void buildExportVO(ReceiveOrderReportExportVO exportVO, Map<Long, String> enterpriseNameMap, Map<Long, List<ReceiveOrderVerificationVO>> verificationMap, Map<Long, BigDecimal> verificationAmountMap, Map<Long, String> shipperAreaCodeMap, Map<Long, List<String>> shipperPrincipalNameMap) {
        Long receiveOrderId = exportVO.getReceiveOrderId();

        exportVO.setEnterpriseName(enterpriseNameMap.getOrDefault(exportVO.getEnterpriseId(), StringConst.EMPTY));
        // 设置核销信息
        List<ReceiveOrderVerificationVO> receiveOrderVerificationVOList = verificationMap.getOrDefault(receiveOrderId, Lists.newArrayList());
        BigDecimal totalVerificationAmount = verificationAmountMap.getOrDefault(receiveOrderId, BigDecimal.ZERO);

        exportVO.setVerificationAmount(BigDecimal.ZERO);
        if (totalVerificationAmount.compareTo(exportVO.getTotalAmount()) > 0) {
            exportVO.setVerificationAmount(exportVO.getTotalAmount());
        } else {
            exportVO.setVerificationAmount(totalVerificationAmount);
        }
        verificationAmountMap.put(receiveOrderId, SmartBigDecimalUtil.Amount.subtract(totalVerificationAmount, exportVO.getVerificationAmount()));

        if (CollectionUtils.isNotEmpty(receiveOrderVerificationVOList)) {
            receiveOrderVerificationVOList.sort((Comparator.comparing(ReceiveOrderVerificationVO::getCreateTime)));
            ReceiveOrderVerificationVO verificationVO = receiveOrderVerificationVOList.get(NumberConst.ZERO);
            exportVO.setVerificationTime(verificationVO.getCreateTime());
            exportVO.setVerificationRemark(verificationVO.getRemark());
        }
        // 计算逾期天数
        LocalDateTime now = LocalDateTime.now();
        if (null != exportVO.getAccountPeriodDate()) {
            LocalDateTime accountPeriodDateTime = exportVO.getAccountPeriodDate().atStartOfDay();
            Long overdueDays = Duration.between(accountPeriodDateTime, now).toDays();
            exportVO.setOverdueDays(overdueDays.intValue());
        }
        //
        // 设置业务人员
        Long shipperId = exportVO.getShipperId();
        List<String> managerNameList = shipperPrincipalNameMap.getOrDefault(shipperId, Lists.newArrayList());
        exportVO.setManagerName(SmartStringUtil.join(StringConst.SEPARATOR, managerNameList));
        // 设置货主地区
        String areaCode = shipperAreaCodeMap.getOrDefault(shipperId, StringConst.EMPTY);
        DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(areaCode);
        if (null != dictValueVO) {
            exportVO.setAreaName(dictValueVO.getValueName());
        }

        // 设置应收款金额
        exportVO.setWaitReceiveAmount(SmartBigDecimalUtil.Amount.subtract(exportVO.getTotalAmount(), exportVO.getVerificationAmount()));
    }


    private void buildExportItemVO(ReceiveOrderReportExportItemVO exportVO, Map<Long, String> enterpriseNameMap, Map<Long, String> shipperAreaCodeMap, Map<Long, List<String>> shipperPrincipalNameMap) {
        exportVO.setEnterpriseName(enterpriseNameMap.getOrDefault(exportVO.getEnterpriseId(), StringConst.EMPTY));
        // 计算逾期天数
        LocalDateTime now = LocalDateTime.now();
        if (null != exportVO.getAccountPeriodDate()) {
            LocalDateTime accountPeriodDateTime = exportVO.getAccountPeriodDate().atStartOfDay();
            Long overdueDays = Duration.between(accountPeriodDateTime, now).toDays();
            exportVO.setOverdueDays(overdueDays.intValue());
        }
        // 设置业务人员
        Long shipperId = exportVO.getShipperId();
        List<String> managerNameList = shipperPrincipalNameMap.getOrDefault(shipperId, Lists.newArrayList());
        exportVO.setManagerName(SmartStringUtil.join(StringConst.SEPARATOR, managerNameList));
        // 设置货主地区
        String areaCode = shipperAreaCodeMap.getOrDefault(shipperId, StringConst.EMPTY);
        DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(areaCode);
        if (null != dictValueVO) {
            exportVO.setAreaName(dictValueVO.getValueName());
        }
        // 设置应收款金额
        exportVO.setWaitReceiveAmount(SmartBigDecimalUtil.Amount.subtract(exportVO.getTotalAmount(), exportVO.getVerificationAmount()));
    }

    /**
     * 根据运单id查收款单列表
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<ReceiveOrderVO>> queryByWaybill(Long waybillId) {
        List<ReceiveOrderEntity> list = receiveOrderDao.selectValidByWaybillId(waybillId, CheckStatusEnum.CANCEL.getValue(), InvoiceStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<ReceiveOrderVO> receiveOrderVOList = SmartBeanUtil.copyList(list, ReceiveOrderVO.class);
        //已销金额 未销金额
        receiveOrderCommonService.buildAmount(receiveOrderVOList);
        return ResponseDTO.ok(receiveOrderVOList);
    }

    /**
     * 导出应收核销列表
     *
     * @param queryForm
     * @return
     */
    public List<ReceiveOrderReportExportVO> queryVerification4Export(ReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        List<ReceiveOrderVO> receiveOrderList = receiveOrderDao.queryByPage(null, queryForm);
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return Lists.newArrayList();
        }
        List<ReceiveOrderReportExportVO> exportList = buildExportList(receiveOrderList);
        return exportList;
    }

    public List<ReceiveOrderReportExportItemVO> queryVerificationItemExport(ReceiveQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setShipperSaleType(PrincipalTypeEnum.MANAGER.getValue());
        List<ReceiveOrderVO> receiveOrderList = receiveOrderDao.queryByPage(null, queryForm);
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return Lists.newArrayList();
        }
        List<ReceiveOrderReportExportItemVO> exportList = buildVerificationItemExportList(receiveOrderList);
        return exportList;
    }

    /**
     * 构建导出列表
     *
     * @param receiveOrderList
     * @return
     */
    private List<ReceiveOrderReportExportVO> buildExportList(List<ReceiveOrderVO> receiveOrderList) {
        Set<Long> enterpriseIdList = receiveOrderList.stream().map(ReceiveOrderVO::getEnterpriseId).collect(Collectors.toSet());
        // 获取公司信息
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        List<Long> receiveOrderIdList = receiveOrderList.stream().map(ReceiveOrderVO::getReceiveOrderId).collect(Collectors.toList());
        // 获取核销信息
        List<ReceiveOrderVerificationVO> verificationList = receiveOrderVerificationDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, BigDecimal> verificationAmountMap = Maps.newHashMap();
        Map<Long, List<ReceiveOrderVerificationVO>> verificationMap = verificationList.stream().collect(Collectors.groupingBy(ReceiveOrderVerificationVO::getReceiveOrderId));

        for (Map.Entry<Long, List<ReceiveOrderVerificationVO>> entry : verificationMap.entrySet()) {
            List<BigDecimal> verificationAmountList = entry.getValue().stream().map(ReceiveOrderVerificationVO::getVerificationAmount).collect(Collectors.toList());
            BigDecimal totalVerificationAmount = verificationAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            verificationAmountMap.put(entry.getKey(), totalVerificationAmount);
        }
        // 获取开票信息
        List<ReceiveOrderInvoiceEntity> invoiceList = receiveOrderInvoiceDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, List<ReceiveOrderInvoiceEntity>> invoiceMap = invoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderInvoiceEntity::getReceiveOrderId));

        // 获取货主的地区信息
        Set<Long> shipperIdList = receiveOrderList.stream().map(ReceiveOrderVO::getShipperId).collect(Collectors.toSet());
        List<ShipperEntity> shipperList = shipperDao.selectBatchIds(shipperIdList);
        Map<Long, String> shipperAreaCodeMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, e -> {
            return SmartStringUtil.isBlank(e.getArea()) ? StringConst.EMPTY : e.getArea();
        }));
        Map<Long, String> consignorMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
        // 获取业务人员
        Map<Long, List<String>> shipperPrincipalNameMap = shipperService.queryPrincipalByShipperIdType(shipperIdList, PrincipalTypeEnum.MANAGER);

        List<ReceiveOrderReportExportVO> exportList = Lists.newArrayList();

        for (ReceiveOrderVO item : receiveOrderList) {
            Long receiveOrderId = item.getReceiveOrderId();
            // 是空的话，是不开票业务
            List<ReceiveOrderInvoiceEntity> receiveOrderInvoiceList = invoiceMap.getOrDefault(receiveOrderId, Lists.newArrayList());
            if (CollectionUtils.isEmpty(receiveOrderInvoiceList)) {
                ReceiveOrderReportExportVO exportVO = SmartBeanUtil.copy(item, ReceiveOrderReportExportVO.class);
                exportVO.setInvoiceName(consignorMap.getOrDefault(item.getShipperId(), StringConst.EMPTY));
                buildExportVO(exportVO, enterpriseNameMap, verificationMap, verificationAmountMap, shipperAreaCodeMap, shipperPrincipalNameMap);
                exportList.add(exportVO);
            } else {
                for (ReceiveOrderInvoiceEntity invoice : receiveOrderInvoiceList) {
                    ReceiveOrderReportExportVO exportVO = SmartBeanUtil.copy(item, ReceiveOrderReportExportVO.class);
                    exportVO.setInvoiceName(invoice.getInvoiceName());
                    exportVO.setInvoiceTime(invoice.getInvoiceTime());
                    exportVO.setInvoiceNumber(invoice.getInvoiceNumber());
                    buildExportVO(exportVO, enterpriseNameMap, verificationMap, verificationAmountMap, shipperAreaCodeMap, shipperPrincipalNameMap);
                    exportList.add(exportVO);
                }

            }
        }
        return exportList;
    }

    private List<ReceiveOrderReportExportItemVO> buildVerificationItemExportList(List<ReceiveOrderVO> receiveOrderList) {
        Set<Long> enterpriseIdList = receiveOrderList.stream().map(ReceiveOrderVO::getEnterpriseId).collect(Collectors.toSet());
        // 获取公司信息
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 获取核销信息
        List<Long> receiveOrderIdList = receiveOrderList.stream().map(ReceiveOrderVO::getReceiveOrderId).collect(Collectors.toList());
        List<ReceiveOrderVerificationVO> verificationList = receiveOrderVerificationDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, BigDecimal> verificationAmountMap = Maps.newHashMap();
        Map<Long, List<ReceiveOrderVerificationVO>> verificationMap = verificationList.stream().collect(Collectors.groupingBy(ReceiveOrderVerificationVO::getReceiveOrderId));

        for (Map.Entry<Long, List<ReceiveOrderVerificationVO>> entry : verificationMap.entrySet()) {
            List<BigDecimal> verificationAmountList = entry.getValue().stream().map(ReceiveOrderVerificationVO::getVerificationAmount).collect(Collectors.toList());
            BigDecimal totalVerificationAmount = verificationAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            verificationAmountMap.put(entry.getKey(), totalVerificationAmount);
        }

        // 获取开票信息
        List<ReceiveOrderInvoiceEntity> invoiceList = receiveOrderInvoiceDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, List<ReceiveOrderInvoiceEntity>> invoiceMap = invoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderInvoiceEntity::getReceiveOrderId));

        // 获取货主的地区信息
        Set<Long> shipperIdList = receiveOrderList.stream().map(ReceiveOrderVO::getShipperId).collect(Collectors.toSet());
        List<ShipperEntity> shipperList = shipperDao.selectBatchIds(shipperIdList);
        Map<Long, String> shipperAreaCodeMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, e -> {
            return SmartStringUtil.isBlank(e.getArea()) ? StringConst.EMPTY : e.getArea();
        }));
        Map<Long, String> consignorMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
        // 获取业务人员
        Map<Long, List<String>> shipperPrincipalNameMap = shipperService.queryPrincipalByShipperIdType(shipperIdList, PrincipalTypeEnum.MANAGER);

        List<ReceiveOrderReportExportItemVO> exportList = Lists.newArrayList();

        for (ReceiveOrderVO item : receiveOrderList) {
            Long receiveOrderId = item.getReceiveOrderId();
            // 核销明细信息
            List<ReceiveOrderVerificationVO> receiveOrderVerificationVOS = verificationMap.get(receiveOrderId);
            BigDecimal totalVerificationAmount = verificationAmountMap.getOrDefault(receiveOrderId, BigDecimal.ZERO);

            // 是空的话，是不开票业务
            List<ReceiveOrderInvoiceEntity> receiveOrderInvoiceList = invoiceMap.getOrDefault(receiveOrderId, Lists.newArrayList());
            ReceiveOrderInvoiceEntity orderInvoiceEntity = null;
            if (CollectionUtils.isNotEmpty(receiveOrderInvoiceList)) {
                orderInvoiceEntity = receiveOrderInvoiceList.get(NumberConst.ZERO);
            }
            // 判断是否存在核销明细 如果存在按明细展示
            if (CollectionUtils.isNotEmpty(receiveOrderVerificationVOS)) {
                // 按核销明细展示
                for (ReceiveOrderVerificationVO verificationVO : receiveOrderVerificationVOS) {
                    ReceiveOrderReportExportItemVO exportVO = SmartBeanUtil.copy(item, ReceiveOrderReportExportItemVO.class);
                    if (null != orderInvoiceEntity) {
                        exportVO.setInvoiceTime(orderInvoiceEntity.getInvoiceTime());
                        exportVO.setInvoiceNumber(orderInvoiceEntity.getInvoiceNumber());
                        exportVO.setInvoiceName(orderInvoiceEntity.getInvoiceName());
                    } else {
                        exportVO.setInvoiceName(consignorMap.getOrDefault(item.getShipperId(), StringConst.EMPTY));
                    }
                    // 设置核销信息
                    buildVerificationVO(verificationVO, exportVO, totalVerificationAmount);
                    verificationAmountMap.put(receiveOrderId, SmartBigDecimalUtil.Amount.subtract(totalVerificationAmount, exportVO.getVerificationAmount()));
                    buildExportItemVO(exportVO, enterpriseNameMap, shipperAreaCodeMap, shipperPrincipalNameMap);
                    exportList.add(exportVO);
                }
            } else {
                ReceiveOrderReportExportItemVO exportVO = SmartBeanUtil.copy(item, ReceiveOrderReportExportItemVO.class);
                if (null != orderInvoiceEntity) {
                    exportVO.setInvoiceTime(orderInvoiceEntity.getInvoiceTime());
                    exportVO.setInvoiceNumber(orderInvoiceEntity.getInvoiceNumber());
                    exportVO.setInvoiceName(orderInvoiceEntity.getInvoiceName());
                } else {
                    exportVO.setInvoiceName(consignorMap.getOrDefault(item.getShipperId(), StringConst.EMPTY));
                }
                exportVO.setVerificationAmount(totalVerificationAmount);
                exportVO.setVerificationItemAmount(BigDecimal.ZERO);
                buildExportItemVO(exportVO, enterpriseNameMap, shipperAreaCodeMap, shipperPrincipalNameMap);
                exportList.add(exportVO);
            }

        }
        return exportList;
    }

    private void buildVerificationVO(ReceiveOrderVerificationVO verificationVO, ReceiveOrderReportExportItemVO exportVO, BigDecimal totalVerificationAmount) {
        BigDecimal verificationAmount = verificationVO.getVerificationAmount();
        exportVO.setVerificationItemAmount(ObjectUtils.isEmpty(verificationAmount) ? BigDecimal.ZERO : verificationAmount);
        exportVO.setSequenceCode(verificationVO.getSequenceCode());
        exportVO.setVerificationAmount(totalVerificationAmount);
        exportVO.setVerificationTime(verificationVO.getVerificationTime());
        exportVO.setVerificationRemark(verificationVO.getRemark());
    }


    public ResponseDTO<String> cancelVerification(ReceiveOrderCancelForm cancelForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderId = cancelForm.getReceiveOrderId();
        ReceiveOrderEntity receiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (null == receiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "收款单不存在");
        }
        if (receiveOrderEntity.getVerificationFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "收款单已核销");
        }
        receiveOrderEntity.setCheckStatus(CheckStatusEnum.CANCEL.getValue());
        receiveOrderEntity.setCheckRemark(cancelForm.getRemark());
        receiveOrderEntity.setCheckUserId(cancelForm.getReceiveOrderId());
        receiveOrderEntity.setInvoiceStatus(InvoiceStatusEnum.CANCEL.getValue());
        receiveOrderManage.cancel(receiveOrderEntity, cancelForm.getOperateUserId(), null);
        receiveOrderDataTracerService.cancelCheckLog(cancelForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 财务开票导出
     *
     * @param receiveOrderId
     * @return
     */
    public List<ReceiveOrderMakeInvoiceExcelVO> makeInvoiceExport(Long receiveOrderId) {
        ReceiveOrderEntity receiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (receiveOrderEntity == null) {
            return Lists.newArrayList();
        }
        //开票税率
        BigDecimal tax = receiveOrderEntity.getTaxPoint();
        if (tax == null) {
            tax = BigDecimal.ZERO;
        }
        BigDecimal nftRate = tax.divide(new BigDecimal(100));

        List<Long> waybillIdList = receiveOrderWaybillDao.selectWaybillIdByReceiveOrderId(receiveOrderId);
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Lists.newArrayList();
        }
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        //路线
        List<WaybillPathEntity> waybillPathEntityList = waybillPathDao.selectByWaybillIdList(waybillIdList);
        Map<Long, List<WaybillPathEntity>> pathListMap = waybillPathEntityList.stream().collect(groupingBy(WaybillPathEntity::getWaybillId));
        //车辆
        Set<Long> vehicleIdList = waybillEntityList.stream().map(WaybillEntity::getVehicleId).collect(Collectors.toSet());
        Map<Long, VehicleEntity> vehicleMap = this.getVehicleMap(vehicleIdList);
        //货物
        List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByWaybillIdList(waybillIdList);
        Map<Long,WaybillGoodsEntity> waybillGoodsListMap = waybillGoodsEntityList.stream().collect(Collectors.toMap(WaybillGoodsEntity::getWaybillId, Function.identity()));

        List<ReceiveOrderMakeInvoiceExcelVO> excelVOList = Lists.newArrayList();
        for (WaybillEntity waybillEntity : waybillEntityList) {
            Long waybillId = waybillEntity.getWaybillId();
            //装卸货地址
            List<WaybillPathEntity> pathList = pathListMap.getOrDefault(waybillId, Lists.newArrayList());
            WaybillPathEntity start = waybillPathService.getStartPath(pathList);
            String loadAddress = waybillPathService.getProvinceCityAddress(start);
            WaybillPathEntity end = waybillPathService.getEndPath(pathList);
            String unloadAddress = waybillPathService.getProvinceCityAddress(end);
            // 单价
            WaybillGoodsEntity waybillGoodsEntity = waybillGoodsListMap.get(waybillId);
            BigDecimal num = BigDecimal.ONE;
            String goodsName = StringConst.EMPTY;
            if (null != waybillGoodsEntity) {
                goodsName = waybillGoodsEntity.getGoodsName();
            }
            // 税额
            BigDecimal taxAmount = SmartBigDecimalUtil.Amount.multiply(waybillEntity.getReceiveAmount(), nftRate);
            // 车
            VehicleEntity vehicleEntity = vehicleMap.get(waybillEntity.getVehicleId());
            String vehicleNumber = "";
            if (vehicleEntity != null) {
                vehicleNumber = vehicleEntity.getVehicleNumber();
            }
            ReceiveOrderMakeInvoiceExcelVO excelVO = new ReceiveOrderMakeInvoiceExcelVO();
            excelVO.setWaybillNumber(waybillEntity.getWaybillNumber());
            excelVO.setLoadAddress(loadAddress);
            excelVO.setUnloadAddress(unloadAddress);
            excelVO.setTransportType("公路运输");
            excelVO.setVehicleNumber(vehicleNumber);
            excelVO.setGoodsName(goodsName);
            excelVO.setFreightSettleType("车");
            excelVO.setNum(num);
            excelVO.setFreightTotalAmount(waybillEntity.getReceiveAmount());
            excelVO.setTax(tax.toString() + "%");
            excelVO.setTaxAmount(taxAmount);
            excelVOList.add(excelVO);
        }
        return excelVOList;
    }

    /**
     * 司机信息
     *
     * @param driverIdList
     * @return
     */
    public Map<Long, DriverEntity> getDriverMap(Collection<Long> driverIdList) {
        Map<Long, DriverEntity> driverMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(driverIdList)) {
            return driverMap;
        }
        List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
        if (CollectionUtils.isNotEmpty(driverEntityList)) {
            driverMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, Function.identity()));

        }
        return driverMap;
    }

    /**
     * 车辆信息
     *
     * @param vehicleIdList
     * @return
     */
    public Map<Long, VehicleEntity> getVehicleMap(Collection<Long> vehicleIdList) {
        Map<Long, VehicleEntity> vehicleMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return vehicleMap;
        }
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (CollectionUtils.isNotEmpty(vehicleEntityList)) {
            vehicleMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, Function.identity()));
        }
        return vehicleMap;
    }

    /**
     * 车队信息
     *
     * @param fleetIdList
     * @return
     */
    private Map<Long, FleetEntity> getFleetMap(Collection<Long> fleetIdList) {
        Map<Long, FleetEntity> fleetMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(fleetIdList)) {
            return fleetMap;
        }
        List<FleetEntity> fleetEntityList = fleetDao.selectBatchIds(fleetIdList);
        if (CollectionUtils.isNotEmpty(fleetEntityList)) {
            fleetMap = fleetEntityList.stream().collect(Collectors.toMap(FleetEntity::getFleetId, Function.identity()));
        }
        return fleetMap;
    }

    /**
     * 对账明细导出
     *
     * @param receiveOrderId
     * @return
     */
    public List<ReceiveOrderWaybillExcelVO> waybillExport(Long receiveOrderId) {
        ReceiveOrderEntity receiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (receiveOrderEntity == null) {
            return Lists.newArrayList();
        }
        BigDecimal tax = receiveOrderEntity.getTaxPoint();
        if (tax == null) {
            tax = BigDecimal.ZERO;
        }
        BigDecimal taxRate = tax.divide(new BigDecimal(100));
        //运单
        List<Long> waybillIdList = receiveOrderWaybillDao.selectWaybillIdByReceiveOrderId(receiveOrderId);
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Lists.newArrayList();
        }
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        //路线
        List<WaybillPathEntity> waybillPathEntityList = waybillPathDao.selectByWaybillIdList(waybillIdList);
        Map<Long, List<WaybillPathEntity>> pathListMap = waybillPathEntityList.stream().collect(groupingBy(WaybillPathEntity::getWaybillId));
        //司机信息
        Set<Long> driverIdList = waybillEntityList.stream().map(WaybillEntity::getDriverId).collect(Collectors.toSet());
        Map<Long, DriverEntity> driverMap = this.getDriverMap(driverIdList);
        //车队
        Set<Long> fleetIdList = waybillEntityList.stream().filter(e -> e.getFleetId() != null).map(WaybillEntity::getFleetId).collect(Collectors.toSet());
        Map<Long, FleetEntity> fleetMap = this.getFleetMap(fleetIdList);
        //车辆
        Set<Long> vehicleIdList = waybillEntityList.stream().map(WaybillEntity::getVehicleId).collect(Collectors.toSet());
        Map<Long, VehicleEntity> vehicleMap = this.getVehicleMap(vehicleIdList);
        //货物
        List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByWaybillIdList(waybillIdList);
        Map<Long,WaybillGoodsEntity> waybillGoodsListMap = waybillGoodsEntityList.stream().collect(Collectors.toMap(WaybillGoodsEntity::getWaybillId, Function.identity()));

        List<ReceiveOrderWaybillExcelVO> excelVOList = Lists.newArrayList();
        for (WaybillEntity waybillEntity : waybillEntityList) {
            Long waybillId = waybillEntity.getWaybillId();
            //装卸货地址
            List<WaybillPathEntity> pathList = pathListMap.getOrDefault(waybillId, Lists.newArrayList());
            WaybillPathEntity start = waybillPathService.getStartPath(pathList);
            String loadAddress = waybillPathService.getProvinceCityAddress(start);
            WaybillPathEntity end = waybillPathService.getEndPath(pathList);
            String unloadAddress = waybillPathService.getProvinceCityAddress(end);
            // 单价
            WaybillGoodsEntity waybillGoodsEntity = waybillGoodsListMap.getOrDefault(waybillId, null);
            BigDecimal num = BigDecimal.ONE;
            String goodsName = StringConst.EMPTY;
            if (null != waybillGoodsEntity) {
                goodsName = waybillGoodsEntity.getGoodsName();
            }
            // 车
            VehicleEntity vehicleEntity = vehicleMap.get(waybillEntity.getVehicleId());
            String vehicleNumber = "";
            if (vehicleEntity != null) {
                vehicleNumber = vehicleEntity.getVehicleNumber();
            }
            DriverEntity driverEntity = driverMap.get(waybillEntity.getDriverId());
            String driverName = driverEntity == null ? "" : driverEntity.getDriverName();

            FleetEntity fleetEntity = fleetMap.get(waybillEntity.getFleetId());
            String fleetName = fleetEntity == null ? "" : fleetEntity.getFleetName();

            ReceiveOrderWaybillExcelVO excelVO = new ReceiveOrderWaybillExcelVO();
            excelVO.setWaybillNumber(waybillEntity.getWaybillNumber());
            excelVO.setLoadAddress(loadAddress);
            excelVO.setUnloadAddress(unloadAddress);
            excelVO.setVehicleNumber(vehicleNumber);
            excelVO.setGoodsName(goodsName);
            excelVO.setFreightSettleType("车");
            excelVO.setNum(num);
            excelVO.setFreightTotalAmount(waybillEntity.getReceiveAmount());
            excelVO.setTax(tax.toString() + "%");
            excelVO.setTaxAmount(SmartBigDecimalUtil.Amount.multiply(waybillEntity.getReceiveAmount(), taxRate));
            excelVO.setDriverName(driverName);
            excelVO.setFleetName(fleetName);
            excelVO.setLoadTime(waybillEntity.getDeliverGoodsTime());
            excelVO.setUnloadTime(waybillEntity.getReceiveGoodsTime());
            excelVO.setCreateTime(waybillEntity.getCreateTime());
            excelVO.setCreateUserName(waybillEntity.getCreateUserName());
            excelVOList.add(excelVO);
        }
        return excelVOList;
    }
}
