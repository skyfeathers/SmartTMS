package net.lab1024.tms.admin.module.business.flow.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 15:16
 */
public enum FlowInstanceQueryTypeEnum implements BaseEnum {

    MY_HANDLE(1, "我处理的"),

    MY_INITIATE(2, "我发起的"),

    MY_RECEIVED(3, "我收到的"),
    ;



    private final Integer type;

    private final String desc;

    FlowInstanceQueryTypeEnum(Integer type, String desc) {
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
