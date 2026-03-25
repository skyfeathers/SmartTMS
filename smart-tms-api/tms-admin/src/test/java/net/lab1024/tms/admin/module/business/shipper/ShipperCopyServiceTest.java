package net.lab1024.tms.admin.module.business.shipper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderManage;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperEnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.manager.*;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.common.module.business.shipper.domain.*;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
class ShipperCopyServiceTest extends TmsAdminApplicationTest {
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperEnterpriseDao shipperEnterpriseDao;
    @Autowired
    private ShipperManager shipperManager;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShipperContactManager shipperContactManager;
    @Autowired
    private ShipperInvoiceManager shipperInvoiceManager;
    @Autowired
    private ShipperMailAddressManager shipperMailAddressManager;
    @Autowired
    private ShipperPaymentWayManager shipperPaymentWayManager;
    @Autowired
    private ShipperPrincipalManager shipperPrincipalManager;
    @Autowired
    private ShipperTypeManager shipperTypeManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ReceiveOrderManage receiveOrderManage;
    @Autowired
    private WaybillManager waybillManager;

    // 查询订单列表，循环订单列表，根据货主名称以及所属公司查询货主是否存在，不存在则新建并更新订单当前所属货主ID。
    @Test
    void queryList() {
        List<OrderEntity> orderList = queryOrderList();
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }

        List<ShipperEntity> shipperList = queryShipperList();
        if (CollectionUtils.isEmpty(shipperList)) {
            return;
        }
        update(orderList, shipperList);
    }

    @Transactional(rollbackFor = Throwable.class)
    void update(List<OrderEntity> orderList, List<ShipperEntity> shipperList) {
        Map<Long, String> shipperNameMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
        Map<Long, ShipperEntity> shipperMap = shipperList.stream().collect(Collectors.toMap(e -> e.getShipperId(), Function.identity()));
        Map<String, ShipperEntity> shipperEnterpriseMap = shipperList.stream().collect(Collectors.toMap(e -> e.getConsignor() + "_" + e.getEnterpriseId(), Function.identity()));

        Map<Long, List<ShipperContactEntity>> contactMap = queryContactMap();
        Map<Long, List<ShipperPaymentWayEntity>> paymentWayMap = queryPaymentWayMap();
        Map<Long, List<ShipperInvoiceEntity>> invoiceMap = queryShipperInvoice();
        Map<Long, List<ShipperMailAddressEntity>> mailAddressMap = queryShipperMailAddress();
        Map<Long, List<ShipperTypeEntity>> shipperTypeMap = queryShipperType();
        Map<Long, List<ShipperPrincipalEntity>> shipperPrincipalMap = queryShipperPrincipals();

        Map<String, Long> originShipperNewIdMap = Maps.newHashMap();
        List<OrderEntity> updateOrderList = Lists.newArrayList();
        for (OrderEntity order : orderList) {
            Long originShipperId = order.getShipperId();
            Long enterpriseId = order.getEnterpriseId();
            if (!shipperNameMap.containsKey(originShipperId)) {
                throw new BusinessException(order.getOrderNo() + "的货主未找到对应的ID");
            }
            String shipperName = shipperNameMap.get(originShipperId);
            String shipperEnterpriseKey = shipperName + "_" + enterpriseId;
            // 如果货主表中存在 货主名称以及所属公司的数据，则不处理
            if (shipperEnterpriseMap.containsKey(shipperEnterpriseKey)) {
                continue;
            }

            ShipperEntity originShipperEntity = shipperMap.get(originShipperId);
            ShipperEntity shipperEntity = SmartBeanUtil.copy(originShipperEntity, ShipperEntity.class);
            shipperEntity.setEnterpriseId(enterpriseId);
            shipperManager.getBaseMapper().insert(shipperEntity);


            // 将新建的数据设置进map
            Long newShipperId = shipperEntity.getShipperId();
            shipperEnterpriseMap.put(shipperEnterpriseKey, shipperEntity);
            shipperMap.put(newShipperId, shipperEntity);
            shipperNameMap.put(newShipperId, shipperEntity.getConsignor());


            originShipperNewIdMap.put(originShipperId + "_" + enterpriseId, newShipperId);
            // 联系人
            List<ShipperContactEntity> contactList = contactMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(contactList)) {
                List<ShipperContactEntity> insertList = contactList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperContactManager.saveBatch(insertList);
            }


            // 付款信息
            List<ShipperPaymentWayEntity> paymentWayList = paymentWayMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(paymentWayList)) {
                List<ShipperPaymentWayEntity> insertList = paymentWayList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperPaymentWayManager.saveBatch(insertList);
            }


            // 发票
            List<ShipperInvoiceEntity> invoiceList = invoiceMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(invoiceList)) {
                List<ShipperInvoiceEntity> insertList = invoiceList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperInvoiceManager.saveBatch(insertList);
            }


            // 邮寄地址
            List<ShipperMailAddressEntity> mailAddressList = mailAddressMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(mailAddressList)) {
                List<ShipperMailAddressEntity> insertList = mailAddressList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperMailAddressManager.saveBatch(insertList);
            }


            // 类型
            List<ShipperTypeEntity> shipperTypeList = shipperTypeMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(shipperTypeList)) {
                List<ShipperTypeEntity> insertList = shipperTypeList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperTypeManager.saveBatch(insertList);
            }


            // 联系人
            List<ShipperPrincipalEntity> shipperPrincipalList = shipperPrincipalMap.getOrDefault(originShipperId, Lists.newArrayList());
            if (CollectionUtils.isNotEmpty(shipperPrincipalList)) {
                List<ShipperPrincipalEntity> insertList = shipperPrincipalList.stream().map(e -> {
                    e.setShipperId(newShipperId);
                    return e;
                }).collect(Collectors.toList());
                shipperPrincipalManager.saveBatch(insertList);
            }


            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(order.getOrderId());
            orderEntity.setShipperId(newShipperId);
            updateOrderList.add(orderEntity);

        }

        if (CollectionUtils.isNotEmpty(updateOrderList)) {
            orderManager.updateBatchById(updateOrderList);
        }

        // 更新运单
        List<WaybillEntity> waybillList = queryWaybillList();
        List<WaybillEntity> updateWaybillList = Lists.newArrayList();
        for (WaybillEntity waybill : waybillList) {
            Long originShipperId = waybill.getShipperId();

            String key = originShipperId + "_" + waybill.getEnterpriseId();
            if (!originShipperNewIdMap.containsKey(key)) {
                continue;
            }
            Long newShipperId = originShipperNewIdMap.get(key);
            WaybillEntity updateEntity = new WaybillEntity();
            updateEntity.setWaybillId(waybill.getWaybillId());
            updateEntity.setShipperId(newShipperId);
            updateWaybillList.add(updateEntity);
        }
        if (CollectionUtils.isNotEmpty(updateWaybillList)) {
            waybillManager.updateBatchById(updateWaybillList);
        }

        // 更新应收
        List<ReceiveOrderEntity> receiveOrderList = queryReceiveOrderList();
        List<ReceiveOrderEntity> updateReceiveOrderList = Lists.newArrayList();
        for (ReceiveOrderEntity receiveOrder : receiveOrderList) {
            Long originShipperId = receiveOrder.getShipperId();

            String key = originShipperId + "_" + receiveOrder.getEnterpriseId();
            if (!originShipperNewIdMap.containsKey(key)) {
                continue;
            }
            Long newShipperId = originShipperNewIdMap.get(key);
            ReceiveOrderEntity updateEntity = new ReceiveOrderEntity();
            updateEntity.setReceiveOrderId(receiveOrder.getReceiveOrderId());
            updateEntity.setShipperId(newShipperId);
            updateReceiveOrderList.add(updateEntity);
        }
        if (CollectionUtils.isNotEmpty(updateReceiveOrderList)) {
            receiveOrderManage.updateBatchById(updateReceiveOrderList);
        }

    }

    /**
     * 获取货主列表
     *
     * @return
     */
    private List<ShipperEntity> queryShipperList() {
        QueryWrapper qw = new QueryWrapper();
        List<ShipperEntity> shipperList = shipperManager.getBaseMapper().selectList(qw);
        return shipperList;
    }

    /**
     * 获取订单
     *
     * @return
     */
    private List<OrderEntity> queryOrderList() {
        QueryWrapper qw = new QueryWrapper();
        List<OrderEntity> orderList = orderManager.getBaseMapper().selectList(qw);
        return orderList;
    }

    /**
     * 获取运单列表
     *
     * @return
     */
    private List<WaybillEntity> queryWaybillList() {
        List<WaybillEntity> waybillList = waybillManager.getBaseMapper().selectList(null);
        return waybillList;
    }


    /**
     * 获取应收列表
     *
     * @return
     */
    private List<ReceiveOrderEntity> queryReceiveOrderList() {
        List<ReceiveOrderEntity> receiveOrderList = receiveOrderManage.getBaseMapper().selectList(null);
        return receiveOrderList;
    }

    /**
     * 联系人信息 shipperContact
     */
    private Map<Long, List<ShipperContactEntity>> queryContactMap() {
        List<ShipperContactEntity> shipperContactList = shipperContactManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperContactEntity>> shipperContactMap = shipperContactList.stream().collect(Collectors.groupingBy(ShipperContactEntity::getShipperId));
        return shipperContactMap;
    }

    /**
     * 付款信息 shipperPaymentWay
     */
    private Map<Long, List<ShipperPaymentWayEntity>> queryPaymentWayMap() {
        List<ShipperPaymentWayEntity> shipperContactList = shipperPaymentWayManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperPaymentWayEntity>> shipperContactMap = shipperContactList.stream().collect(Collectors.groupingBy(ShipperPaymentWayEntity::getShipperId));
        return shipperContactMap;
    }

    /**
     * 发票信息 shipperInvoice
     */
    private Map<Long, List<ShipperInvoiceEntity>> queryShipperInvoice() {
        List<ShipperInvoiceEntity> shipperInvoiceList = shipperInvoiceManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperInvoiceEntity>> shipperInvoiceMap = shipperInvoiceList.stream().collect(Collectors.groupingBy(ShipperInvoiceEntity::getShipperId));
        return shipperInvoiceMap;
    }

    /**
     * 邮寄地址 shipperMailAddress
     */
    private Map<Long, List<ShipperMailAddressEntity>> queryShipperMailAddress() {
        List<ShipperMailAddressEntity> shipperMailAddressList = shipperMailAddressManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperMailAddressEntity>> shipperInvoiceMap = shipperMailAddressList.stream().collect(Collectors.groupingBy(ShipperMailAddressEntity::getShipperId));
        return shipperInvoiceMap;
    }

    /**
     * 货主类型 shipperType
     */
    private Map<Long, List<ShipperTypeEntity>> queryShipperType() {
        List<ShipperTypeEntity> shipperTypeList = shipperTypeManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperTypeEntity>> shipperInvoiceMap = shipperTypeList.stream().collect(Collectors.groupingBy(ShipperTypeEntity::getShipperId));
        return shipperInvoiceMap;
    }

    /**
     * 客服、业务负责人 shipperPrincipal
     */
    private Map<Long, List<ShipperPrincipalEntity>> queryShipperPrincipals() {
        List<ShipperPrincipalEntity> shipperInvoiceList = shipperPrincipalManager.getBaseMapper().selectList(null);
        Map<Long, List<ShipperPrincipalEntity>> shipperInvoiceMap = shipperInvoiceList.stream().collect(Collectors.groupingBy(ShipperPrincipalEntity::getShipperId));
        return shipperInvoiceMap;
    }
}