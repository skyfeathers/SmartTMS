package net.lab1024.tms.admin.module.business.pay.domain.bo;

import lombok.Data;

import java.math.BigDecimal;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/30 下午3:45
 */
@Data
public class PayOrderCostBO {
    /**
     * 审核状态
     * @see FlowAuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 付款单状态
     * @see PayOrderStatusEnum
     */
    private Integer payOrderStatus;

    /**
     * 付款单id
     */
    private Long payOrderId;
    /**
     * 运单id
     */
    private Long waybillId;

    /**
     * 运单费用项id
     */
    private Long waybillCostId;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

}