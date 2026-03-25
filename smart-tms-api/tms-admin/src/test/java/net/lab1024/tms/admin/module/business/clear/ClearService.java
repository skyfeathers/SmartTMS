package net.lab1024.tms.admin.module.business.clear;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/31 8:50 下午
 */
@Service
public class ClearService {

    @Autowired
    private PayOrderClearService payOrderClearService;
    @Autowired
    private ReceiveOrderClearService receiveOrderClearService;
    @Autowired
    private CarCostClearService carCostClearService;
    @Autowired
    private WaybillOilCardRechargeApplyClearService waybillOilCardRechargeApplyClearService;
    @Autowired
    private ContractClearService contractClearService;
    @Autowired
    private EmployeeClearService employeeClearService;
    @Autowired
    private OilCardClearService oilCardClearService;
    @Autowired
    private OrderClearService orderClearService;
    @Autowired
    private WaybillClearService waybillClearService;
    @Autowired
    private ShipperClearService shipperClearService;
    @Autowired
    private VehicleDriverClearService vehicleDriverClearService;
    @Autowired
    private TransportRouteClearService transportRouteClearService;


    private final List<Long> NOT_IN_ENTERPRISE_ID = Lists.newArrayList(1L);

    @Transactional(rollbackFor = Throwable.class)
    public void clear() {
        // 应付
        payOrderClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 应收
        receiveOrderClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 油卡充值
        waybillOilCardRechargeApplyClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 车辆费用
        carCostClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 合同
        contractClearService.contractClear(NOT_IN_ENTERPRISE_ID);
        // 员工
        employeeClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 油卡
        oilCardClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 订单
        orderClearService.orderClear(NOT_IN_ENTERPRISE_ID);
        // 运单
        waybillClearService.waybillClear(NOT_IN_ENTERPRISE_ID);
        // 货主
        shipperClearService.clear(NOT_IN_ENTERPRISE_ID);
        // 车辆司机
        vehicleDriverClearService.vehicleDriverClear(NOT_IN_ENTERPRISE_ID);
        // 运输路线
        transportRouteClearService.clear(NOT_IN_ENTERPRISE_ID);
    }
}