package net.lab1024.tms.admin.module.business.pay.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.custom.FlowTaskHandler;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderDetailVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/9/16 11:46 上午
 */
@FlowTaskHandler(value = "oilCardFlowScheduleHandleService", desc = "油卡充值-运单调度审批")
public class OilCardFlowScheduleHandleService implements IFlowCustomTaskHandlerService {

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Long> customHandler(Long initiatorId, Long enterpriseId, Object businessData) {
        PayOrderDetailVO payOrderDetailVO = (PayOrderDetailVO) businessData;
        Long waybillId = payOrderDetailVO.getWaybillId();

        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);

        OrderEntity orderEntity = orderDao.selectById(waybillEntity.getOrderId());
        return Lists.newArrayList(orderEntity.getScheduleId());
    }
}