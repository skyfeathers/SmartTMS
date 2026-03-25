package net.lab1024.tms.admin.module.business.quick;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 快速查询枚举 ]
 *
 * @author yandanyang
 * @date 2020/10/28 17:05
 */
public enum QuickSearchTypeEnum implements BaseEnum {
    ORDER(1, "订单"),
    WAYBILL(2, "运单"),
    PAY_ORDER(3, "付款单"),
    RECEIVE_ORDER(4, "收款单"),
    ;

    private Integer type;

    private String desc;

    QuickSearchTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
