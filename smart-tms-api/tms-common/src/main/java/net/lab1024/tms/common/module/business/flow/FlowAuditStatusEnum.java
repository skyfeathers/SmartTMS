package net.lab1024.tms.common.module.business.flow;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 流程定义发起人类型 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:27
 */
public enum FlowAuditStatusEnum implements BaseEnum {


    WAIT(1, "待审核"),

    PROCESSING(2, "审核中"),

    PASS(3, "审核通过"),

    REJECT(4, "审核驳回"),

    ALREADY_CC(5, "已抄送"),

    CANCEL(9, "审核撤销"),

    ;

    private final Integer type;

    private final String desc;

    FlowAuditStatusEnum(Integer type, String desc) {
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
