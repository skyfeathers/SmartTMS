package net.lab1024.tms.admin.module.business.receive.domain.dto;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/9/29 11:56 上午
 */
@Data
public class ReceiveOrderWaybillDTO {

    private Long receiveOrderId;

    /**
     * 收款单号
     */
    private String receiveOrderNumber;

    private Long waybillId;

    private String attachment;

    private Long checkUserId;

    private String checkUserName;
}