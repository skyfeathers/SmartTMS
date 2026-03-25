package net.lab1024.tms.common.module.system.datascope.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 数据范围 类型
 *
 */
public enum DataScopeTypeEnum implements BaseEnum {

    ORDER(1, 10, "订单", "运输业务订单管理"),

    WAYBILL(2, 20, "运单", "运输业务运单管理"),
    ;

    private final Integer value;

    private final Integer sort;

    private final String name;

    private final String desc;

    DataScopeTypeEnum(Integer value, Integer sort, String name, String desc) {
        this.value = value;
        this.sort = sort;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getSort() {
        return sort;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }


}
