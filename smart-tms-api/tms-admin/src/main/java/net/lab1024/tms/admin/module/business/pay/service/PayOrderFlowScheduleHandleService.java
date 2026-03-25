package net.lab1024.tms.admin.module.business.pay.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.custom.FlowTaskHandler;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderCostDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/9/16 11:46 上午
 */
@FlowTaskHandler(value = "payOrderFlowScheduleHandleService", desc = "付款单审批-运单调度审批")
public class PayOrderFlowScheduleHandleService implements IFlowCustomTaskHandlerService {

    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Long> customHandler(Long initiatorId, Long enterpriseId, Object businessData) {

        PayOrderDetailVO payOrderDetailVO = (PayOrderDetailVO) businessData;
        Long payOrderId = payOrderDetailVO.getPayOrderId();

        List<PayOrderCostEntity> payOrderCostEntityList = payOrderCostDao.selectByPayOrderId(payOrderId);
        //运单信息
        List<Long> waybillIdList = payOrderCostEntityList.stream().map(PayOrderCostEntity::getWaybillId).collect(Collectors.toList());
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        //订单
        List<Long> orderIdList = waybillEntityList.stream().map(WaybillEntity::getOrderId).collect(Collectors.toList());
        List<OrderEntity> orderEntityList = orderDao.selectBatchIds(orderIdList);
        //调度
        Set<Long> orderScheduleIdList = orderEntityList.stream().map(OrderEntity::getScheduleId).collect(Collectors.toSet());

        return Lists.newArrayList(orderScheduleIdList);
    }
}