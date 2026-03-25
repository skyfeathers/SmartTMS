package net.lab1024.tms.common.module.business.shipper.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 货主等级 ]
 *
 * @author yandanyang
 * @date 2020/10/28 17:05
 */
public enum ShipperGradeEnum implements BaseEnum {
    CORE(1, "核心"),
    POTENTIAL(2, "有潜力"),
    GENERAL(3, "普通"),
    BAD(4, "较差");

    private Integer type;

    private String desc;

    ShipperGradeEnum(Integer type, String desc) {
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
