package net.lab1024.tms.admin.module.business.flow.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 审批处理人类型 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:48
 */
public enum FlowHandlerTypeEnum implements BaseEnum {

    ALL(1, "所有人"),

    ASSIGN_USER(2, "指定人员"),

    ASSIGN_ROLE(3, "指定角色"),

    ASSIGN_DEPARTMENT(4, "指定部门"),

    INITIATOR_MANAGER(5,"发起人部门主管"),

    INITIATOR(6, "发起人自己"),

    CUSTOM_SERVICE(20,"自定义服务"),
    ;

    private final Integer type;

    private final String desc;

    FlowHandlerTypeEnum(Integer type, String desc) {
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
