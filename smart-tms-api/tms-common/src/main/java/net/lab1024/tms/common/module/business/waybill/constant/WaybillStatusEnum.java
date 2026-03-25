package net.lab1024.tms.common.module.business.waybill.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 凭证类型
 *
 * @author zhuoda
 * @Date 2022-07-13
 */
public enum WaybillStatusEnum implements BaseEnum {

    WAIT_RECEIVE(10, "待接单"),

    TRANSPORT(20, "运输中"),

    COMPLETE(30, "运输完成"),

    CANCEL(50, "作废"),
    ;

    private Integer value;

    private String desc;

    WaybillStatusEnum(Integer value, String desc) {
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
