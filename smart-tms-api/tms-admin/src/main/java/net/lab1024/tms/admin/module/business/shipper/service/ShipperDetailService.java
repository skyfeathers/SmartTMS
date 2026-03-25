package net.lab1024.tms.admin.module.business.shipper.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.dao.*;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.*;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperContactEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperInvoiceEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperMailAddressEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperTypeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/8/28 17:47
 */
@Component
@Slf4j
public class ShipperDetailService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperContactDao shipperContactDao;
    @Autowired
    private ShipperInvoiceDao shipperInvoiceDao;
    @Autowired
    private ShipperMailAddressDao shipperMailAddressDao;
    @Autowired
    private ShipperPaymentWayDao shipperPaymentWayDao;
    @Autowired
    private ShipperTypeDao shipperTypeDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private EnterpriseDao enterpriseDao;

    /**
     * 货主的基础信息列表
     *
     * @return
     */
    public List<ShipperBasicDTO> shipperBasic(Long enterpriseId) {
        List<ShipperBasicDTO> shipperList = shipperDao.queryList(enterpriseId, Boolean.FALSE);
        if (CollectionUtils.isEmpty(shipperList)) {
            return shipperList;
        }
        return shipperList;
    }

    /**
     * 货主的联系人列表
     *
     * @param shipperId
     * @return
     */
    public List<ShipperContactDTO> shipperContact(Long shipperId) {
        List<ShipperContactEntity> contactEntityList = shipperContactDao.selectByShipperId(shipperId, false);
        if (CollectionUtils.isNotEmpty(contactEntityList)) {
            return SmartBeanUtil.copyList(contactEntityList, ShipperContactDTO.class);
        }
        return Lists.newArrayList();
    }

    /**
     * 查询货主的付款方式
     *
     * @param shipperId
     * @return
     */
    public List<ShipperPaymentWayDTO> shipperPaymentWay(Long shipperId) {
        List<ShipperPaymentWayDTO> paymentTypeList = shipperPaymentWayDao.selectByShipperId(shipperId, false);
        if (CollectionUtils.isNotEmpty(paymentTypeList)) {
            return paymentTypeList;
        }
        return Lists.newArrayList();
    }

    /**
     * 查询货主下的开票信息
     *
     * @param shipperId
     * @return
     */
    public List<ShipperInvoiceDTO> shipperInvoice(Long shipperId) {
        return shipperInvoiceDao.selectByShipperId(shipperId, false);
    }

    /**
     * 根据货主开票ID查询开票信息
     *
     * @param invoiceId
     * @return
     */
    public ShipperInvoiceDTO selectByInvoiceId(Long invoiceId) {
        ShipperInvoiceEntity invoiceEntity = shipperInvoiceDao.selectById(invoiceId);
        if (null == invoiceEntity || invoiceEntity.getDeletedFlag()) {
            return null;
        }
        return SmartBeanUtil.copy(invoiceEntity, ShipperInvoiceDTO.class);
    }

    /**
     * 查询货主下的邮寄信息
     *
     * @param shipperId
     * @return
     */
    public List<ShipperMailAddressDTO> shipperMailAddress(Long shipperId) {
        List<ShipperMailAddressEntity> mailAddressEntityList = shipperMailAddressDao.selectByShipperId(shipperId, false);
        if (CollectionUtils.isNotEmpty(mailAddressEntityList)) {
            return SmartBeanUtil.copyList(mailAddressEntityList, ShipperMailAddressDTO.class);
        }
        return Lists.newArrayList();
    }

    /**
     * 缓存货主的业务类型
     *
     * @param shipperId
     * @return
     */
    public List<Integer> shipperType(Long shipperId) {
        List<ShipperTypeEntity> typeEntityList = shipperTypeDao.selectByShipperId(shipperId);
        if (CollectionUtils.isNotEmpty(typeEntityList)) {
            List<Integer> shipperTypeList = typeEntityList.stream().map(ShipperTypeEntity::getShipperType).collect(Collectors.toList());
            return shipperTypeList;
        }
        return Lists.newArrayList();
    }

    /**
     * 查询货主的负责人信息
     *
     * @param shipperId
     * @return
     */
    public List<ShipperPrincipalDTO> shipperPrincipal(Long shipperId) {
        List<ShipperPrincipalDTO> typeEntityList = shipperPrincipalDao.selectByShipperId(shipperId);
        return typeEntityList;
    }
}
