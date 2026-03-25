package net.lab1024.tms.job.module.order;

import lombok.Data;

@Data
public class OrderBO {

    /**
     * 订单ID
     */
    private Long orderId;

    private Long enterpriseId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 调度
     */
    private Long scheduleId;
}
