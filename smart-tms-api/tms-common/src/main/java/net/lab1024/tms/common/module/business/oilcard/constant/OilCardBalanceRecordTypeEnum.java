package net.lab1024.tms.common.module.business.oilcard.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 油卡交易记录类型
 *
 * @author lidoudou
 * @date 2022/7/18 下午4:10
 */
@AllArgsConstructor
@Getter
public enum OilCardBalanceRecordTypeEnum implements BaseEnum {

    /**
     * 手动充值  主卡功能
     */
    RECHARGE(1, "充值"),

    /**
     * 2 分配
     */
    DISTRIBUTE(2, "分配"),

    /**
     * 3 主卡消费  主卡功能
     */
    EXPEND(3, "主卡消费"),

    /**
     * 4 挂失圈回
     */
    CANCEL_RECHARGE(4, "挂失圈回"),

    /**
     * 5 圈回
     */
    CIRCUMFLEX(5, "圈回"),

    /**
     * 分配副卡
     */
    DISTRIBUTE_SLAVE_CARD(6, "分配副卡"),

    /**
     * 油卡充值作废
     */
    CANCEL_OIL_CARD_RECHARGE(7, "油卡充值作废"),

    CAR_COST_OIL_CARD_RECEIVE(8, "自有车-油卡充值"),

    CAR_COST_OIL_CARD_RECEIVE_RETREAT(9, "自有车-油卡充值退回"),

    CAR_COST_OIL_PAY(10, "自有车-油卡加油"),

    CAR_COST_OIL_PAY_RETREAT(11, "自有车-油卡加油退回"),

    CAR_COST_UREA_PAY(12, "自有车-尿素消费"),

    CAR_COST_UREA_PAY_RETREAT(13, "自有车-尿素消费退回"),

    ;

    private Integer value;

    private String desc;

}
