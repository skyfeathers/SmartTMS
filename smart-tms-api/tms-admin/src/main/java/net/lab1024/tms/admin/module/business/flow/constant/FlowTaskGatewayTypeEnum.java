package net.lab1024.tms.admin.module.business.flow.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 路由节点判断脚本类型 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:27
 */
public enum FlowTaskGatewayTypeEnum implements BaseEnum {


    JS(1, "js脚本判断"),

    SPEL(2, "spel表达式"),
    ;

    private final Integer type;

    private final String desc;

    FlowTaskGatewayTypeEnum(Integer type, String desc) {
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
