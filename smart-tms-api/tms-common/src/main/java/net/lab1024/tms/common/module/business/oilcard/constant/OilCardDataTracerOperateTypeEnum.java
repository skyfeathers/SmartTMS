package net.lab1024.tms.common.module.business.oilcard.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum OilCardDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    RECHARGE(INIT_CODE + 1, "充值"),

    DISTRIBUTE(INIT_CODE + 2, "分配"),

    EXPEND(INIT_CODE + 3, "主卡消费"),

    CANCEL_RECHARGE(INIT_CODE + 4, "挂失圈回"),

    CIRCUMFLEX(INIT_CODE + 5, "圈回"),

    MASTER_DISABLED(INIT_CODE + 6, "主卡禁用"),

    DISTRIBUTE_SLAVE_CARD(INIT_CODE + 7, "分配副卡"),

    CANCEL_OIL_CARD_RECHARGE(INIT_CODE + 8, "油卡充值作废"),

    BATCH_IMPORT(INIT_CODE + 9, "批量导入"),

    CAR_COST_UREA_PAY(INIT_CODE + 10, "自有车-尿素消费"),

    CAR_COST_UREA_PAY_RETREAT(INIT_CODE + 11, "自有车-尿素消费退回"),

    CAR_COST_OIL_PAY(INIT_CODE + 12, "自有车-油费支出"),

    CAR_COST_OIL_PAY_RETREAT(INIT_CODE + 13, "自有车-油费支出退回"),

    CAR_COST_OIL_CARD_RECEIVE(INIT_CODE + 14, "自有车-油卡收入"),

    CAR_COST_OIL_CARD_RECEIVE_RETREAT(INIT_CODE + 15, "自有车-油卡收入退回"),

    UPDATE_PRE_RECHARGE_AMOUNT(INIT_CODE + 16, "更新计划充值金额"),

    RECHARGE_BY_PRE_RECHARGE_AMOUNT(INIT_CODE + 17, "根据计划充值金额充值")
    ;


    private Integer value;
    private String desc;


}
