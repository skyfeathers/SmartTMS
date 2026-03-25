package net.lab1024.tms.admin.module.business.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class OrderServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;

    @Test
    void updateOrderManagerId() {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("manager_id",0L);
        List<OrderEntity> orderList = orderManager.getBaseMapper().selectList(qw);
        Set<Long> shipperIdList = orderList.stream().map(OrderEntity::getShipperId).collect(Collectors.toSet());

        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, List<ShipperPrincipalDTO>> managerListMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId));

        List<OrderEntity> updateList = Lists.newArrayList();
        orderList.forEach(order -> {
            Long shipperId = order.getShipperId();
            if (!managerListMap.containsKey(shipperId)) {
//                throw new BusinessException(shipperId + "货主的业务负责人不能为空");
                System.out.println(shipperId);
                return;
            }
            List<ShipperPrincipalDTO> managerList = managerListMap.get(shipperId);
            if (managerList.size() > 1) {
                throw new BusinessException(shipperId + "货主的业务负责人只能为一个");
            }
            OrderEntity updateEntity = new OrderEntity();
            updateEntity.setOrderId(order.getOrderId());
            updateEntity.setManagerId(managerList.get(NumberConst.ZERO).getEmployeeId());
            updateList.add(updateEntity);
        });
        if (CollectionUtils.isNotEmpty(updateList)) {
            orderManager.updateBatchById(updateList);
        }
    }

}