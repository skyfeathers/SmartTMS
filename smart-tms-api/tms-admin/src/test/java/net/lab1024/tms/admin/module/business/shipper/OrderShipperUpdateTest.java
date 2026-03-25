package net.lab1024.tms.admin.module.business.shipper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderManage;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperEnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.manager.*;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.exception.BusinessException;
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
import java.util.stream.Collectors;

@Slf4j
class OrderShipperUpdateTest extends TmsAdminApplicationTest {
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
        Map<String, Long> shipperEnterpriseMap = shipperList.stream().collect(Collectors.toMap(e -> e.getConsignor() + "_" + e.getEnterpriseId(), ShipperEntity::getShipperId));

        List<OrderEntity> updateOrderList = Lists.newArrayList();
        for (OrderEntity order : orderList) {
            String shipperName = shipperNameMap.getOrDefault(order.getShipperId(), StringConst.EMPTY);
            String key = shipperName + "_" + order.getEnterpriseId();
            if(!shipperEnterpriseMap.containsKey(key)){
                throw new BusinessException("一场警告");
            }
            Long shipperId =  shipperEnterpriseMap.get(key);
            if(order.getShipperId().equals(shipperId)){
                continue;
            }
            OrderEntity updateEntity = new OrderEntity();
            updateEntity.setOrderId(order.getOrderId());
            updateEntity.setShipperId(shipperId);
            updateOrderList.add(updateEntity);
        }

        if (CollectionUtils.isNotEmpty(updateOrderList)) {
            System.out.println(updateOrderList.size());
            orderManager.updateBatchById(updateOrderList);
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