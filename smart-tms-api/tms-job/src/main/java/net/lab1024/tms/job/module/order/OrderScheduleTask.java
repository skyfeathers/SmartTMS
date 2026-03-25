package net.lab1024.tms.job.module.order;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单到达装货时间未派车提醒
 *
 * @author lidoudou
 * @date 2022/9/27 下午3:48
 */
@Slf4j
@Service
public class OrderScheduleTask {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MsgCommonService msgCommonService;

    /**
     * 订单装货日期不为空，且未分配运单，则提醒对应订单调度
     * 1小时执行一次
     * 发送消息提醒
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void updateStatus() {
        List<OrderBO> orderList = orderDao.selectByScheduleFlagAndLoadTime(LocalDateTime.now(), Boolean.FALSE, OrderStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }

        List<MsgSendDTO> msgSendList = Lists.newArrayList();
        orderList.forEach(order -> {
            MsgSendDTO sendDTO = new MsgSendDTO();
            sendDTO.setMsgSubType(MsgSubTypeEnum.ORDER_NOT_SCHEDULED);
            sendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            sendDTO.setReceiverId(order.getScheduleId());
            sendDTO.setEnterpriseId(order.getEnterpriseId());
            sendDTO.setDataId(order.getOrderId());
            // 参数
            Map<String, Object> contentParam = new HashMap<>(1);
            contentParam.put("orderId", order.getOrderNo());
            sendDTO.setContentParam(contentParam);
            msgSendList.add(sendDTO);
        });

        if (CollectionUtils.isNotEmpty(msgSendList)) {
            msgCommonService.send(msgSendList);
        }
    }
}
