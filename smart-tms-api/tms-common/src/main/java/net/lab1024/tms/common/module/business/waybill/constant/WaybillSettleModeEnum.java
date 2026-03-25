package net.lab1024.tms.common.module.business.waybill.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 结算类型
 *
 * @author zhuoda
 * @Date 2022-07-13
 */
public enum WaybillSettleModeEnum implements BaseEnum {

    /**
     * 外调车到付  结算类型发生变动，到付类型合并
     */
    ARRIVE_PAY(10, "到付"),

    /**
     * 挂靠车到付
     */
    MONTH_PAY(40, "到付"),

    MONTH_SETTLE(20, "挂靠车月结"),

    ARRIVE_MONTH_PAY(50, "外调车月结"),

    SELF_VEHICLE_SETTLE(30, "自有车结算"),
    ;

    private Integer value;

    private String desc;

    WaybillSettleModeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
