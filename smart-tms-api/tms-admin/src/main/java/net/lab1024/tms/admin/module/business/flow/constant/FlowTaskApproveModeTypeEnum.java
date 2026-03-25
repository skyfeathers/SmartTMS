package net.lab1024.tms.admin.module.business.flow.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 审核方式 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:48
 */
public enum FlowTaskApproveModeTypeEnum implements BaseEnum {


    AND_SIGN(1, "会签"),

    OR_SIGN(2, "或签"),
    ;

    private final Integer type;

    private final String desc;

    FlowTaskApproveModeTypeEnum(Integer type, String desc) {
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
