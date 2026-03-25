package net.lab1024.tms.admin.module.business.waybill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.order.service.OrderGoodsService;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.domain.vo.WaybillBusinessDateVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillGoodsDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillReceiveCostDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

class WaybillServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private WaybillReceiveCostDao waybillReceiveCostDao;
    @Autowired
    private Map<String, IFlowCustomTaskHandlerService> flowCustomTaskHandlerServiceMap;

    @Test
    void flowTest() {

    }

    /**
     * 批量更新利润
     */
    @Test
    void updateProfitAmount() {

    }

    /**
     * 更新网络货运运单的应付总金额，需去掉网络货运费用
     */
    @Test
    void updatePayableAmount() {

    }

    /**
     * 更新运单的业务时间
     */
    @Test
    void updateBusinessDate() {

    }

    @Test
    void updateScheduleFlag() {

    }
}