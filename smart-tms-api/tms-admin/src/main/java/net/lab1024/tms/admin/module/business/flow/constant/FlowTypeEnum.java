package net.lab1024.tms.admin.module.business.flow.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 流程类型 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:27
 */
@Getter
@AllArgsConstructor
public enum FlowTypeEnum implements BaseEnum {

    WAY_BILL_AUDIT(1,"运单审批"),
    OIL_CARD_AUDIT(2,"油卡充值审批"),

    PAY_AUDIT(3,"应付款审批"),
    RECEIVE_AUDIT(4,"应收款审批"),
    ;

    private final Integer value;

    private final String desc;
}
