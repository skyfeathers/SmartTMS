package net.lab1024.tms.admin.module.business.receive.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderInvoiceDao;
import net.lab1024.tms.admin.module.business.receive.domain.form.*;
import net.lab1024.tms.admin.module.business.receive.domain.vo.ReceiveOrderInvoiceListVO;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderInvoiceManage;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderManage;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.msg.MsgCommonManager;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgEntity;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderInvoiceEntity;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应收帐款Service
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:26
 */
@Service
public class ReceiveOrderInvoiceService {

    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ReceiveOrderManage receiveOrderManage;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private ReceiveOrderCommonService receiveOrderCommonService;
    @Autowired
    private ReceiveOrderDataTracerService receiveOrderDataTracerService;
    @Autowired
    private ReceiveOrderInvoiceManage receiveOrderInvoiceManage;
    @Autowired
    private ShipperDao shipperDao;

    @Autowired
    private MsgCommonService msgCommonService;
    @Resource
    private ShipperPrincipalDao shipperPrincipalDao;


    /**
     * 查询申请开票列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ReceiveOrderInvoiceListVO>> queryInvoiceByPage(ReceiveInvoiceQueryForm queryForm) {
        queryForm.setVerificationFlag(Boolean.TRUE);
        queryForm.setCheckStatus(CheckStatusEnum.CHECK.getValue());
        if (InvoiceStatusEnum.CANCEL.equalsValue(queryForm.getInvoiceStatus())) {
            queryForm.setCheckStatus(CheckStatusEnum.CANCEL.getValue());
        }
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ReceiveOrderInvoiceListVO> receiveOrderInvoiceList = receiveOrderDao.queryInvoiceByPage(page, queryForm);
        this.buildEnterpriseId(receiveOrderInvoiceList);
        PageResult<ReceiveOrderInvoiceListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, receiveOrderInvoiceList);
        return ResponseDTO.ok(pageResultDTO);
    }


    private void buildEnterpriseId(List<ReceiveOrderInvoiceListVO> receiveOrderList) {
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return;
        }
        Set<Long> enterpriseId = receiveOrderList.stream().map(ReceiveOrderInvoiceListVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> shipperList = enterpriseDao.selectBatchIds(enterpriseId);
        Map<Long, String> enterpriseNameMap = shipperList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
        receiveOrderList.forEach(item -> {
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
        });
    }


    /**
     * 财务开票
     *
     * @param applyInvoiceForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> invoice(ApplyInvoiceForm applyInvoiceForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderInvoiceId = applyInvoiceForm.getReceiveOrderInvoiceId();
        ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity = receiveOrderInvoiceDao.selectById(receiveOrderInvoiceId);
        if (null == receiveOrderInvoiceEntity || !InvoiceStatusEnum.WAIT_INVOICE.equalsValue(receiveOrderInvoiceEntity.getInvoiceStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Long receiveOrderId = receiveOrderInvoiceEntity.getReceiveOrderId();
        ReceiveOrderEntity dbReceiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (null == dbReceiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Long shipperId = dbReceiveOrderEntity.getShipperId();

        ReceiveOrderInvoiceEntity updateInvoiceEntity = SmartBeanUtil.copy(applyInvoiceForm, ReceiveOrderInvoiceEntity.class);
        updateInvoiceEntity.setInvoiceStatus(InvoiceStatusEnum.INVOICE.getValue());
        updateInvoiceEntity.setInvoiceNumber(SmartStringUtil.join(CommonConst.SEPARATOR, applyInvoiceForm.getInvoiceNumberList()));
        receiveOrderInvoiceDao.updateById(updateInvoiceEntity);

        // 更新收款单为 开票完成 或者 部分开票
        ReceiveOrderEntity updateReceiveOrderEntity = new ReceiveOrderEntity();
        updateReceiveOrderEntity.setReceiveOrderId(receiveOrderId);
        updateReceiveOrderEntity.setInvoiceStatus(getInvoiceStatus(receiveOrderId));
        updateReceiveOrderEntity.setBillAttachment(applyInvoiceForm.getBillAttachment());
        // 如果存在多条开票数据，仅在第一次开票时计算账期
        if (null == dbReceiveOrderEntity.getAccountPeriodDate()) {
            LocalDate accountPeriod = receiveOrderCommonService.generateAccountPeriod(applyInvoiceForm.getInvoiceTime(), shipperId);
            updateReceiveOrderEntity.setAccountPeriodDate(accountPeriod);
        }
        receiveOrderDao.updateById(updateReceiveOrderEntity);
        List<MsgSendDTO> msgSendList = this.getMsgList(shipperId, dbReceiveOrderEntity.getEnterpriseId(), dbReceiveOrderEntity.getCreateUserId(), receiveOrderId);
        if (CollectionUtils.isNotEmpty(msgSendList)) {
            msgCommonService.send(msgSendList);
        }
        receiveOrderDataTracerService.invoiceLog(receiveOrderId, applyInvoiceForm.getInvoiceUserName(), receiveOrderInvoiceEntity.getInvoiceAmount(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量开票
     *
     * @param batchApplyInvoiceForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> batchInvoice(BatchApplyInvoiceForm batchApplyInvoiceForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> receiveOrderInvoiceIdList = batchApplyInvoiceForm.getReceiveOrderInvoiceIdList();
        List<ReceiveOrderInvoiceEntity> dbReceiveOrderInvoiceEntityList = receiveOrderInvoiceDao.selectBatchIds(receiveOrderInvoiceIdList);
        if (CollectionUtils.isEmpty(dbReceiveOrderInvoiceEntityList)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        for (ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity : dbReceiveOrderInvoiceEntityList) {
            if (!InvoiceStatusEnum.WAIT_INVOICE.equalsValue(receiveOrderInvoiceEntity.getInvoiceStatus())) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
            }
        }

        Set<Long> receiveOrderIdList = dbReceiveOrderInvoiceEntityList.stream().map(ReceiveOrderInvoiceEntity::getReceiveOrderId).collect(Collectors.toSet());
        List<ReceiveOrderEntity> dbReceiveOrderEntityList = receiveOrderDao.selectBatchIds(receiveOrderIdList);
        if (CollectionUtils.isEmpty(dbReceiveOrderEntityList)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        Set<Long> shipperIdSet = dbReceiveOrderEntityList.stream().map(ReceiveOrderEntity::getShipperId).collect(Collectors.toSet());
        if (shipperIdSet.size() > NumberConst.ONE) {
            return ResponseDTO.userErrorParam("只能选择同一货主进行开票");
        }

        // 更新开票状态
        List<ReceiveOrderInvoiceEntity> updateEntityList = receiveOrderInvoiceIdList.stream().map(receiveOrderInvoiceId -> {
            ReceiveOrderInvoiceEntity updateInvoiceEntity = SmartBeanUtil.copy(batchApplyInvoiceForm, ReceiveOrderInvoiceEntity.class);
            updateInvoiceEntity.setReceiveOrderInvoiceId(receiveOrderInvoiceId);
            updateInvoiceEntity.setInvoiceStatus(InvoiceStatusEnum.INVOICE.getValue());
            updateInvoiceEntity.setInvoiceNumber(SmartStringUtil.join(CommonConst.SEPARATOR, batchApplyInvoiceForm.getInvoiceNumberList()));
            return updateInvoiceEntity;
        }).collect(Collectors.toList());
        receiveOrderInvoiceManage.updateBatchById(updateEntityList);

        // 查询货主账期
        Map<Long, LocalDate> accountPeriodMap = queryShipperAccountPeriod(shipperIdSet, batchApplyInvoiceForm.getInvoiceTime());

        // 查询开票状态
        Map<Long, Integer> receiveOrderInvoiceStatusMap = getInvoiceStatusByReceiveOrderIdList(receiveOrderIdList);
        // 更新收款单为 开票完成 或者 部分开票
        List<ReceiveOrderEntity> receiveOrderUpdateList = Lists.newArrayList();
        for (ReceiveOrderEntity dbReceiveOrderEntity : dbReceiveOrderEntityList) {
            Long receiveOrderId = dbReceiveOrderEntity.getReceiveOrderId();
            ReceiveOrderEntity updateReceiveOrderEntity = new ReceiveOrderEntity();
            updateReceiveOrderEntity.setReceiveOrderId(receiveOrderId);
            updateReceiveOrderEntity.setInvoiceStatus(receiveOrderInvoiceStatusMap.getOrDefault(receiveOrderId, InvoiceStatusEnum.PORTION_INVOICE.getValue()));
            updateReceiveOrderEntity.setBillAttachment(batchApplyInvoiceForm.getBillAttachment());
            // 如果存在多条开票数据，仅在第一次开票时计算账期
            if (null == dbReceiveOrderEntity.getAccountPeriodDate()) {
                updateReceiveOrderEntity.setAccountPeriodDate(accountPeriodMap.get(dbReceiveOrderEntity.getShipperId()));
            }
            receiveOrderUpdateList.add(updateReceiveOrderEntity);
        }
        receiveOrderManage.updateBatchById(receiveOrderUpdateList);

        ReceiveOrderEntity receiveOrderEntity = dbReceiveOrderEntityList.get(NumberConst.ZERO);
        List<MsgSendDTO> msgSendList = this.getMsgList(receiveOrderEntity.getShipperId(), receiveOrderEntity.getEnterpriseId(), receiveOrderEntity.getCreateUserId(), receiveOrderEntity.getReceiveOrderId());
        if (CollectionUtils.isNotEmpty(msgSendList)) {
            msgCommonService.send(msgSendList);
        }
        receiveOrderDataTracerService.batchInvoiceLog(dbReceiveOrderInvoiceEntityList, batchApplyInvoiceForm.getInvoiceUserName(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取发送到客服的消息列表
     *
     * @param shipperId
     * @param receiveOrderId
     * @return
     */
    private List<MsgSendDTO> getMsgList(Long shipperId, Long enterpriseId, Long createUserId, Long receiveOrderId) {
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        List<ShipperPrincipalDTO> shipperPrincipalDTOList = shipperPrincipalDao.selectByShipperIdAndType(shipperId, PrincipalTypeEnum.CUSTOMER_SERVICE.getValue());

        Set<Long> employeeIdSet = Sets.newHashSet(createUserId);
        if (ObjectUtil.isNotEmpty(shipperPrincipalDTOList)) {
            employeeIdSet.addAll(shipperPrincipalDTOList.stream().map(ShipperPrincipalDTO::getEmployeeId).collect(Collectors.toSet()));
        }
        String consignor = shipperEntity.getConsignor();
        Map<String, Object> contentParam = Maps.newHashMap();
        contentParam.put("name", consignor);

        return employeeIdSet.stream().map(employee -> {
            MsgSubTypeEnum msgSubTypeEnum = MsgSubTypeEnum.SHIPPER_FA_PIAO_ZHANG_DAN_SHANG_CHUAN;
            MsgSendDTO msgSendDTO = new MsgSendDTO();
            msgSendDTO.setMsgSubType(msgSubTypeEnum);
            msgSendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            msgSendDTO.setReceiverId(employee);
            msgSendDTO.setEnterpriseId(enterpriseId);
            msgSendDTO.setDataId(String.valueOf(receiveOrderId));
            msgSendDTO.setContentParam(contentParam);
            return msgSendDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取货主的账期时间
     *
     * @param shipperIdSet
     * @return
     */
    private Map<Long, LocalDate> queryShipperAccountPeriod(Collection<Long> shipperIdSet, LocalDate invoiceDate) {
        List<ShipperEntity> shipperList = shipperDao.selectBatchIds(shipperIdSet);
        Map<Long, LocalDate> accountPeriodMap = Maps.newHashMap();
        for (ShipperEntity shipper : shipperList) {
            Integer accountPeriod = shipper.getAccountPeriod();
            if (null == accountPeriod) {
                accountPeriod = NumberConst.ZERO;
            }

            accountPeriodMap.put(shipper.getShipperId(), invoiceDate.plusDays(accountPeriod));
        }
        return accountPeriodMap;
    }

    /**
     * 判断应收关联的开票数据是否全部已开票，未全部开票返回部分开票状态
     *
     * @param receiveOrderId
     * @return
     */
    private Integer getInvoiceStatus(Long receiveOrderId) {
        List<ReceiveOrderInvoiceEntity> invoiceList = receiveOrderInvoiceDao.selectByReceiveOrderId(receiveOrderId);
        Integer waitInvoiceCount = invoiceList.stream().filter(e -> InvoiceStatusEnum.WAIT_INVOICE.equalsValue(e.getInvoiceStatus())).collect(Collectors.toList()).size();
        if (waitInvoiceCount == NumberConst.ZERO) {
            return InvoiceStatusEnum.INVOICE.getValue();
        }
        return InvoiceStatusEnum.PORTION_INVOICE.getValue();
    }

    /**
     * 判断应收关联的开票数据是否全部已开票，未全部开票返回部分开票状态
     *
     * @param receiveOrderIdList
     * @return
     */
    private Map<Long, Integer> getInvoiceStatusByReceiveOrderIdList(Collection<Long> receiveOrderIdList) {
        Map<Long, Integer> invoiceStatusMap = Maps.newHashMap();
        List<ReceiveOrderInvoiceEntity> invoiceList = receiveOrderInvoiceDao.selectByReceiveOrderIdList(receiveOrderIdList);
        Map<Long, List<ReceiveOrderInvoiceEntity>> invoiceMap = invoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderInvoiceEntity::getReceiveOrderId));

        for (Map.Entry<Long, List<ReceiveOrderInvoiceEntity>> item : invoiceMap.entrySet()) {
            List<ReceiveOrderInvoiceEntity> list = item.getValue();
            Integer invoiceStatus = InvoiceStatusEnum.PORTION_INVOICE.getValue();
            Integer waitInvoiceCount = list.stream().filter(e -> InvoiceStatusEnum.WAIT_INVOICE.equalsValue(e.getInvoiceStatus())).collect(Collectors.toList()).size();
            if (waitInvoiceCount == NumberConst.ZERO) {
                invoiceStatus = InvoiceStatusEnum.INVOICE.getValue();
            }
            invoiceStatusMap.put(item.getKey(), invoiceStatus);
        }
        return invoiceStatusMap;
    }

    /**
     * 作废发票申请
     *
     * @param cancelForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelInvoice(ReceiveOrderInvoiceCancelForm cancelForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderInvoiceId = cancelForm.getReceiveOrderInvoiceId();
        ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity = receiveOrderInvoiceDao.selectById(receiveOrderInvoiceId);
        if (null == receiveOrderInvoiceEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "开票申请不存在");
        }
        ReceiveOrderEntity receiveOrderEntity = receiveOrderDao.selectById(receiveOrderInvoiceEntity.getReceiveOrderId());
        receiveOrderEntity.setInvoiceStatus(InvoiceStatusEnum.CANCEL.getValue());
        receiveOrderEntity.setCheckStatus(CheckStatusEnum.CANCEL.getValue());
        receiveOrderManage.cancel(receiveOrderEntity, cancelForm.getOperateUserId(), cancelForm.getRemark());
        receiveOrderDataTracerService.cancelInvoiceLog(receiveOrderEntity.getReceiveOrderId(), cancelForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 上传对账单
     *
     * @param uploadBillForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> uploadBill(InvoiceUploadBillForm uploadBillForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderId = uploadBillForm.getReceiveOrderId();
        ReceiveOrderEntity dbReceiveOrderEntity = receiveOrderDao.selectById(receiveOrderId);
        if (null == dbReceiveOrderEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ReceiveOrderEntity updateEntity = SmartBeanUtil.copy(uploadBillForm, ReceiveOrderEntity.class);
        receiveOrderDao.updateById(updateEntity);
        List<MsgSendDTO> msgSendList = this.getMsgList(dbReceiveOrderEntity.getShipperId(), dbReceiveOrderEntity.getEnterpriseId(), dbReceiveOrderEntity.getCreateUserId(), receiveOrderId);
        if (CollectionUtils.isNotEmpty(msgSendList)) {
            msgCommonService.send(msgSendList);
        }
        receiveOrderDataTracerService.uploadBillLog(receiveOrderId, uploadBillForm.getBillAttachment(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> updateInvoice(UpdateInvoiceForm updateInvoiceForm, DataTracerRequestForm dataTracerRequestForm) {
        Long receiveOrderInvoiceId = updateInvoiceForm.getReceiveOrderInvoiceId();
        ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity = receiveOrderInvoiceDao.selectById(receiveOrderInvoiceId);
        if (null == receiveOrderInvoiceEntity || !InvoiceStatusEnum.INVOICE.equalsValue(receiveOrderInvoiceEntity.getInvoiceStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ReceiveOrderInvoiceEntity updateInvoiceEntity = SmartBeanUtil.copy(updateInvoiceForm, ReceiveOrderInvoiceEntity.class);
        updateInvoiceEntity.setInvoiceNumber(SmartStringUtil.join(CommonConst.SEPARATOR, updateInvoiceForm.getInvoiceNumberList()));
        receiveOrderInvoiceDao.updateById(updateInvoiceEntity);
        ReceiveOrderEntity receiveOrderEntity = new ReceiveOrderEntity();
        receiveOrderEntity.setReceiveOrderId(receiveOrderInvoiceEntity.getReceiveOrderId());
        receiveOrderEntity.setBillAttachment(updateInvoiceForm.getBillAttachment());
        receiveOrderDao.updateById(receiveOrderEntity);
        ReceiveOrderInvoiceEntity afterData = receiveOrderInvoiceDao.selectById(receiveOrderInvoiceId);
        receiveOrderDataTracerService.updateInvoiceLog(receiveOrderInvoiceEntity.getReceiveOrderId(), dataTracerRequestForm, receiveOrderInvoiceEntity, afterData);
        return ResponseDTO.ok();
    }
}
