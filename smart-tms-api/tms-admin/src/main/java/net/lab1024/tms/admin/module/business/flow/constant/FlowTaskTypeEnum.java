package net.lab1024.tms.admin.module.business.flow.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;
/**
 * [ 流程任务节点类型 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:27
 */
public enum FlowTaskTypeEnum implements BaseEnum {


    START(1, "开始节点"),

    APPROVE(2, "审核节点"),

    GATEWAY(3, "条件网关节点"),

    CC(4, "抄送节点"),

    ;

    private final Integer type;

    private final String desc;

    FlowTaskTypeEnum(Integer type, String desc) {
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
