package net.lab1024.tms.admin.module.business.shipper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperEnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.domain.ShipperEnterpriseEntity;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperManager;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
class ShipperServiceTest extends TmsAdminApplicationTest {
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperEnterpriseDao shipperEnterpriseDao;
    @Autowired
    private ShipperManager shipperManager;

    @Test
    void update() {
        List<ShipperEntity> shipperList = queryShipperList();
        if (CollectionUtils.isEmpty(shipperList)) {
            return;
        }
        List<ShipperEnterpriseEntity> shipperEnterpriseList = queryShipperEnterpriseList();
        if (CollectionUtils.isEmpty(shipperEnterpriseList)) {
            return;
        }
        Map<Long, List<Long>> shipperEnterpriseMap = shipperEnterpriseList.stream().collect(Collectors.groupingBy(ShipperEnterpriseEntity::getShipperId, Collectors.mapping(ShipperEnterpriseEntity::getEnterpriseId, Collectors.toList())));


        List<ShipperEntity> updateList = Lists.newArrayList();
        for (ShipperEntity shipper : shipperList) {
            Long shipperId = shipper.getShipperId();
            List<Long> enterpriseIdList = shipperEnterpriseMap.get(shipper.getShipperId());
            if (CollectionUtils.isEmpty(enterpriseIdList)) {
                log.error(shipper.getConsignor() + "未找到对应的所属公司");
                break;
//                continue;
            }
            if (enterpriseIdList.size() > 1) {
                log.error(shipper.getConsignor() + "所属公司数量大于0");
//                break;
            }
            ShipperEntity updateEntity = new ShipperEntity();
            updateEntity.setShipperId(shipperId);
            updateEntity.setEnterpriseId(enterpriseIdList.get(NumberConst.ZERO));
            updateList.add(updateEntity);
        }

        if (CollectionUtils.isNotEmpty(updateList)) {
            shipperManager.updateBatchById(updateList);
        }
    }

    /**
     * 获取货主列表
     *
     * @return
     */
    private List<ShipperEntity> queryShipperList() {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("deleted_flag", Boolean.FALSE);
        List<ShipperEntity> shipperList = shipperDao.selectList(qw);
        return shipperList;
    }

    /**
     * 查询货主、公司关联关系
     */
    private List<ShipperEnterpriseEntity> queryShipperEnterpriseList() {
        List<ShipperEnterpriseEntity> shipperList = shipperEnterpriseDao.selectList(null);
        return shipperList;
    }

    /**
     * 联系人信息 shipperConta
     */

    /**
     * 付款信息 shipperPaymentWay
     */

    /**
     * 发票信息 shipperInvoice
     */

    /**
     * 邮寄地址 shipperMailAddress
     */

    /**
     * 货主类型 shipperType
     */

    /**
     * 客服负责人 shipperPrincipal
     */

    /**
     * 业务负责人 shipperPrincipal
     */
}