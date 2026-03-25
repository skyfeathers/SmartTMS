package net.lab1024.tms.admin.module.business.waybill.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WaybillRouteNameServiceTest  extends TmsAdminApplicationTest {

    @Resource
    private OrderPathDao orderPathDao;

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private WaybillManager waybillManager;

    @Test
    public void updateRouteName() {
        List<WaybillEntity> waybillEntityList = waybillDao.selectList(null);
        List<Long> orderIdList = waybillEntityList.stream()
                .map(WaybillEntity::getOrderId)
                .distinct()
                .collect(Collectors.toList());
        List<OrderPathEntity> orderPathEntityList = orderPathDao.selectByOrderIdList(orderIdList);
        // orderPath 订单ID和订单路线Map
        Map<Long, List<OrderPathEntity>> orderPathMap = orderPathEntityList.stream().collect(Collectors.groupingBy(OrderPathEntity::getOrderId));

        List<WaybillEntity> updateWaybillList = Lists.newArrayList();
        for (WaybillEntity waybillEntity : waybillEntityList) {
            Long orderId = waybillEntity.getOrderId();
            List<OrderPathEntity> pathEntityList = orderPathMap.get(orderId);
            if (ObjectUtil.isEmpty(pathEntityList)) {
                continue;
            }

            // 获取运输路线名字
            Optional<OrderPathEntity> loadPath= pathEntityList.stream().filter(e -> PathTypeEnum.PLACING_LOADING.equalsValue(e.getType()))
                    .min(Comparator.comparingLong(OrderPathEntity::getOrderPathId));
            Optional<OrderPathEntity> unloadPath = pathEntityList.stream().filter(e -> PathTypeEnum.UNLOADING_LOCATION.equalsValue(e.getType()))
                    .max(Comparator.comparingLong(OrderPathEntity::getOrderPathId));
            if (!loadPath.isPresent() || !unloadPath.isPresent()) {
                continue;
            }
            String routeName = loadPath.get().getCityName() + "-" + unloadPath.get().getCityName();

            WaybillEntity updateEntity = new WaybillEntity();
            updateEntity.setWaybillId(waybillEntity.getWaybillId());
            updateEntity.setRouteName(routeName);
            updateWaybillList.add(updateEntity);
        }
        waybillManager.updateBatchById(updateWaybillList);
    }

}
