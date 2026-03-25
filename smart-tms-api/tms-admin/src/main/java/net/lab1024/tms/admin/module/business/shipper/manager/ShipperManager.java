package net.lab1024.tms.admin.module.business.shipper.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.domain.bo.ShipperRecordParseBO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperUpdateForm;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/22 11:44
 */
@Slf4j
@Service
public class ShipperManager extends ServiceImpl<ShipperDao, ShipperEntity> {
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperTypeManager shipperTypeManager;
    @Autowired
    private ShipperPrincipalManager shipperPrincipalManager;
    @Autowired
    private ShipperInvoiceManager shipperInvoiceManager;
    @Autowired
    private ShipperMailAddressManager shipperMailAddressManager;
    @Autowired
    private ShipperPaymentWayManager shipperPaymentWayManager;
    @Autowired
    private ShipperContactManager shipperContactManager;

    /**
     * 保存
     *
     * @param addDTO
     * @param shipperEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void shipperSave(ShipperAddForm addDTO, ShipperEntity shipperEntity) {
        shipperDao.insert(shipperEntity);
        ShipperRecordParseBO recordParseBO = new ShipperRecordParseBO();
        recordParseBO.setNewShipperEntity(shipperEntity);
        this.saveOrUpdateRelateInfo(addDTO, shipperEntity.getShipperId());
    }

    /**
     * 更新
     *
     * @param updateDTO
     */
    @Transactional(rollbackFor = Throwable.class)
    public void shipperUpdate(ShipperUpdateForm updateDTO) {
        //待提交更新的实体对象
        ShipperEntity waitUpdateEntity = SmartBeanUtil.copy(updateDTO, ShipperEntity.class);
        Long shipperId = updateDTO.getShipperId();
        waitUpdateEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        //进行数据操作
        shipperDao.updateById(waitUpdateEntity);
        //删除原关联联系人、付款信息信息
        shipperContactManager.getBaseMapper().deleteByShipperId(shipperId);
        shipperPaymentWayManager.getBaseMapper().deleteByShipperId(shipperId);
        shipperInvoiceManager.getBaseMapper().deleteByShipperId(shipperId);
        shipperMailAddressManager.getBaseMapper().deleteByShipperId(shipperId);
        shipperTypeManager.getBaseMapper().deleteByShipperId(shipperId);
        // 删除货主的负责人信息
        shipperPrincipalManager.getBaseMapper().deleteByShipperId(shipperId);
        //保存新的关联信息
        this.saveOrUpdateRelateInfo(updateDTO, shipperId);
    }


    /**
     * 删除
     *
     * @param shipperIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void shipperBatchDelete(List<Long> shipperIdList) {
        shipperDao.updateDeletedFlagById(shipperIdList, Boolean.TRUE);
        shipperContactManager.getBaseMapper().updateDeletedFlag(shipperIdList, Boolean.TRUE);
        shipperInvoiceManager.getBaseMapper().updateDeletedFlag(shipperIdList, Boolean.TRUE);
        shipperMailAddressManager.getBaseMapper().updateDeletedFlag(shipperIdList, Boolean.TRUE);
        shipperPaymentWayManager.getBaseMapper().updateDeletedFlag(shipperIdList, Boolean.TRUE);
//        shipperEnterpriseManager.getBaseMapper().deleteByShipperId(shipperIdList);
    }

    /**
     * 修改客服
     *
     * @param shipperPrincipalEntityList
     * @param shipperIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updatePrincipal(List<ShipperPrincipalEntity> shipperPrincipalEntityList, List<Long> shipperIdList, Integer principalType) {
        shipperPrincipalManager.getBaseMapper().batchDeleteByShipperIdList(shipperIdList, principalType);
        if (CollectionUtils.isNotEmpty(shipperPrincipalEntityList)) {
            shipperPrincipalManager.saveBatch(shipperPrincipalEntityList);
        }
    }

    /**
     * 保存关联信息
     *
     * @param addDTO
     * @param shipperId
     */
    private void saveOrUpdateRelateInfo(ShipperAddForm addDTO, Long shipperId) {
        //保存关联联系人信息
        if (CollectionUtils.isNotEmpty(addDTO.getContactList())) {
            List<ShipperContactEntity> contactEntityList = SmartBeanUtil.copyList(addDTO.getContactList(), ShipperContactEntity.class);
            contactEntityList.forEach(e -> e.setShipperId(shipperId));
            shipperContactManager.saveBatch(contactEntityList);
        }
        //保存关联付款信息
        if (CollectionUtils.isNotEmpty(addDTO.getPaymentWayList())) {
            List<ShipperPaymentWayEntity> paymentTypeEntityList = SmartBeanUtil.copyList(addDTO.getPaymentWayList(), ShipperPaymentWayEntity.class);
            paymentTypeEntityList.forEach(e -> e.setShipperId(shipperId));
            shipperPaymentWayManager.saveBatch(paymentTypeEntityList);
        }
        //发票信息
        if (CollectionUtils.isNotEmpty(addDTO.getInvoiceList())) {
            List<ShipperInvoiceEntity> invoiceEntityList = SmartBeanUtil.copyList(addDTO.getInvoiceList(), ShipperInvoiceEntity.class);
            invoiceEntityList.forEach(e -> e.setShipperId(shipperId));
            shipperInvoiceManager.saveBatch(invoiceEntityList);
        }

        //保存邮寄地址
        if (CollectionUtils.isNotEmpty(addDTO.getMailAddressList())) {
            List<ShipperMailAddressEntity> mailAddressEntityList = SmartBeanUtil.copyList(addDTO.getMailAddressList(), ShipperMailAddressEntity.class);
            // 如果有一个为true 则将其他的设置为false
            Boolean defaultFlag = Boolean.FALSE;
            for (ShipperMailAddressEntity e : mailAddressEntityList) {
                if (defaultFlag) {
                    e.setDefaultFlag(Boolean.FALSE);
                } else {
                    defaultFlag = e.getDefaultFlag();
                }
                e.setShipperId(shipperId);
            }
            shipperMailAddressManager.saveBatch(mailAddressEntityList);
        }

        //保存货主类型
        if (CollectionUtils.isNotEmpty(addDTO.getShipperTypeList())) {
            List<ShipperTypeEntity> shipperTypeEntityList = Lists.newArrayList();
            for (Integer type : addDTO.getShipperTypeList()) {
                ShipperTypeEntity typeEntity = new ShipperTypeEntity();
                typeEntity.setShipperId(shipperId);
                typeEntity.setShipperType(type);
                shipperTypeEntityList.add(typeEntity);
            }
            shipperTypeManager.saveBatch(shipperTypeEntityList);
        }

        // 保存客服负责人
        if (CollectionUtils.isNotEmpty(addDTO.getCustomerServiceIdList())) {
            List<ShipperPrincipalEntity> shipperPrincipalEntityList = Lists.newArrayList();
            for (Long employeeId : addDTO.getCustomerServiceIdList()) {
                ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
                shipperPrincipalEntity.setShipperId(shipperId);
                shipperPrincipalEntity.setEmployeeId(employeeId);
                shipperPrincipalEntity.setType(PrincipalTypeEnum.CUSTOMER_SERVICE.getValue());
                shipperPrincipalEntityList.add(shipperPrincipalEntity);
            }
            shipperPrincipalManager.saveBatch(shipperPrincipalEntityList);
        }

        // 保存业务负责人
        if (CollectionUtils.isNotEmpty(addDTO.getManagerIdList())) {
            List<ShipperPrincipalEntity> shipperPrincipalEntityList = Lists.newArrayList();
            for (Long employeeId : addDTO.getManagerIdList()) {
                ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
                shipperPrincipalEntity.setShipperId(shipperId);
                shipperPrincipalEntity.setEmployeeId(employeeId);
                shipperPrincipalEntity.setType(PrincipalTypeEnum.MANAGER.getValue());
                shipperPrincipalEntityList.add(shipperPrincipalEntity);
            }
            shipperPrincipalManager.saveBatch(shipperPrincipalEntityList);
        }
    }

}
