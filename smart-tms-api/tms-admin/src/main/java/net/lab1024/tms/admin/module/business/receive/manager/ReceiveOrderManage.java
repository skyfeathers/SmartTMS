package net.lab1024.tms.admin.module.business.receive.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.receive.dao.*;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderInvoiceForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderMailForm;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.receive.domain.*;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @date 2022/7/26 上午11:24
 */
@Service
public class ReceiveOrderManage extends ServiceImpl<ReceiveOrderDao, ReceiveOrderEntity> {

    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;
    @Autowired
    private ReceiveOrderWaybillManage receiveOrderWaybillManage;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private ReceiveOrderMailAddressDao receiveOrderMailAddressDao;
    @Autowired
    private ReceiveOrderInvoiceManage receiveOrderInvoiceManage;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;
    @Autowired
    private WaybillDao waybillDao;

    /**
     * 保存应收帐款信息
     *
     * @param receiveOrderEntity
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public void submit(ReceiveOrderEntity receiveOrderEntity, ReceiveOrderForm addForm) {
        receiveOrderDao.insert(receiveOrderEntity);

        Long receiveOrderId = receiveOrderEntity.getReceiveOrderId();
        List<Long> waybillIdList = addForm.getWaybillIdList();
        //运单
        this.saveReceiveOrderWaybill(waybillIdList, receiveOrderId);
        this.updateWaybillSubmitReceiveFlag(waybillIdList);
        // 更新运单的业务时间
        this.updateBusinessDate(waybillIdList, receiveOrderEntity.getBusinessDate());
        if (!addForm.getMakeInvoiceFlag()) {
            return;
        }
        //受票、开票信息
        this.saveMailAddress(addForm.getReceiveOrderMailForm(), receiveOrderId);
        this.saveReceiveOrderShipperInvoice(addForm.getReceiveOrderInvoiceForm(), receiveOrderId);
    }

    /**
     * 更新业务时间
     *
     * @param waybillIdList
     */
    private void updateBusinessDate(List<Long> waybillIdList, LocalDate businessDate) {
        List<WaybillEntity> updateList = waybillIdList.stream().map(waybillId -> {
            WaybillEntity updateEntity = new WaybillEntity();
            updateEntity.setWaybillId(waybillId);
            updateEntity.setBusinessDate(businessDate);
            return updateEntity;
        }).collect(Collectors.toList());
        waybillManager.updateBatchById(updateList);
    }

    /**
     * 保存应收费用项
     *
     * @param receiveOrderId
     * @param waybillIdList
     */
    private void saveReceiveOrderWaybill(List<Long> waybillIdList, Long receiveOrderId)  {
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        List<ReceiveOrderWaybillEntity> receiveOrderCostItemList = waybillEntityList.stream().map(e -> {
            ReceiveOrderWaybillEntity receiveOrderWaybillEntity = new ReceiveOrderWaybillEntity();
            receiveOrderWaybillEntity.setReceiveOrderId(receiveOrderId);
            receiveOrderWaybillEntity.setWaybillId(e.getWaybillId());
            receiveOrderWaybillEntity.setReceiveAmount(e.getReceiveAmount());
            return receiveOrderWaybillEntity;
        }).collect(Collectors.toList());
        receiveOrderWaybillManage.saveBatch(receiveOrderCostItemList);
    }

    /**
     * 更新运单提交应收状态
     *
     * @param waybillIdList
     */
    private void updateWaybillSubmitReceiveFlag(List<Long> waybillIdList) {
        List<WaybillEntity> updateWaybillList = Lists.newArrayList();
        for (Long waybillId : waybillIdList) {
            WaybillEntity waybillEntity = new WaybillEntity();
            waybillEntity.setWaybillId(waybillId);
            waybillEntity.setSubmitReceiveFlag(Boolean.TRUE);
            updateWaybillList.add(waybillEntity);
        }
        if (CollectionUtils.isNotEmpty(updateWaybillList)) {
            waybillManager.updateBatchById(updateWaybillList);
        }
    }

    /**
     * 更新运单提交应收状态
     *
     * @param mailForm
     * @param receiveOrderId
     */
    private void saveMailAddress(ReceiveOrderMailForm mailForm, Long receiveOrderId) {
        ReceiveOrderMailAddressEntity receiveOrderMailAddressEntity = SmartBeanUtil.copy(mailForm, ReceiveOrderMailAddressEntity.class);
        receiveOrderMailAddressEntity.setReceiveOrderId(receiveOrderId);
        receiveOrderMailAddressDao.insert(receiveOrderMailAddressEntity);
    }



    /**
     * 保存受票方信息
     *
     * @param invoiceForm
     * @param receiveOrderId
     */
    private void saveReceiveOrderShipperInvoice(ReceiveOrderInvoiceForm invoiceForm, Long receiveOrderId) {
        // 保存受票方
        ReceiveOrderInvoiceEntity receiveOrderInvoiceEntity = new ReceiveOrderInvoiceEntity();
        receiveOrderInvoiceEntity.setInvoiceNo(invoiceForm.getInvoiceNo());
        receiveOrderInvoiceEntity.setInvoiceName(invoiceForm.getInvoiceName());
        receiveOrderInvoiceEntity.setInvoicePhone(invoiceForm.getInvoicePhone());
        receiveOrderInvoiceEntity.setInvoiceBankName(invoiceForm.getInvoiceBankName());
        receiveOrderInvoiceEntity.setInvoiceBankNo(invoiceForm.getInvoiceBankNo());
        receiveOrderInvoiceEntity.setInvoiceAddress(invoiceForm.getInvoiceAddress());

        receiveOrderInvoiceEntity.setInvoiceType(invoiceForm.getInvoiceType());
        receiveOrderInvoiceEntity.setInvoiceKindType(invoiceForm.getInvoiceKindType());
        receiveOrderInvoiceEntity.setInvoiceAmount(invoiceForm.getInvoiceAmount());
        receiveOrderInvoiceEntity.setReceiveOrderId(receiveOrderId);
        receiveOrderInvoiceManage.getBaseMapper().insert(receiveOrderInvoiceEntity);
    }


    /**
     * 核销
     *
     * @param receiveOrderEntity
     * @param verificationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void verification(ReceiveOrderEntity receiveOrderEntity, ReceiveOrderVerificationEntity verificationEntity) {
        receiveOrderVerificationDao.insert(verificationEntity);
        receiveOrderDao.updateById(receiveOrderEntity);
    }


    /**
     * 作废应收账单、删除费用、更新订单提交状态、删除开票申请
     *
     * @param receiveOrderEntity
     * @param requestUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> cancel(ReceiveOrderEntity receiveOrderEntity, Long requestUserId, String cancelInvoiceRemark) {
        Long receiveOrderId = receiveOrderEntity.getReceiveOrderId();
        receiveOrderDao.updateById(receiveOrderEntity);

        // 如果操作为作废开票申请，则设置开票状态为已作废
        receiveOrderInvoiceDao.batchUpdateInvoiceStatusByReceiveOrderId(receiveOrderId, InvoiceStatusEnum.CANCEL.getValue(), requestUserId, cancelInvoiceRemark);

        List<Long> waybillIdList = receiveOrderWaybillDao.selectWaybillIdByReceiveOrderId(receiveOrderId);
        // 根据已提交金额，修改运单提交状态
        updateWaybillSubmitReceiveFlag(waybillIdList);
        return ResponseDTO.ok();
    }
}
