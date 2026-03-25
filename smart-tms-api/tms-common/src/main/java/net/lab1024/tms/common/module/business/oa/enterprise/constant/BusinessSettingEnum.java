package net.lab1024.tms.common.module.business.oa.enterprise.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * OA-企业的业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午2:48
 */
@Getter
@AllArgsConstructor
public enum BusinessSettingEnum implements BaseEnum {

    /**
     * 合同到期提醒人
     */
    CONTRACT_MESSAGE_RECEIVER("contract_message_receiver", "合同到期提醒人"),

    /**
     * 应收账款账期提醒
     */
    RECEIVE_ORDER_MESSAGE_RECEIVER("receive_order_message_receiver", "合同到期提醒人"),

    ;
    private final String value;

    private final String desc;
}
