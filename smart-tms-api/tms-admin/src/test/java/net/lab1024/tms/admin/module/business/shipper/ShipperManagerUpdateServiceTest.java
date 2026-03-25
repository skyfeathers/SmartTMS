package net.lab1024.tms.admin.module.business.shipper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperManager;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperPrincipalManager;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPrincipalEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ShipperManagerUpdateServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private ShipperManager shipperManager;
    @Autowired
    private ShipperPrincipalManager shipperPrincipalManager;

    @Test
    public void updateManager() {
        QueryWrapper qw = new QueryWrapper();
        qw.isNotNull("manager_id");
        List<ShipperEntity> shipperEntityList = shipperManager.getBaseMapper().selectList(qw);
        if (CollectionUtils.isEmpty(shipperEntityList)) {
            return;
        }
        List<ShipperPrincipalEntity> managerList = shipperEntityList.stream().map(shipper -> {
            ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
            shipperPrincipalEntity.setShipperId(shipper.getShipperId());
//            shipperPrincipalEntity.setEmployeeId(shipper.getManagerId());
            shipperPrincipalEntity.setType(PrincipalTypeEnum.MANAGER.getValue());
            return shipperPrincipalEntity;
        }).collect(Collectors.toList());
        shipperPrincipalManager.saveBatch(managerList);
    }
}
