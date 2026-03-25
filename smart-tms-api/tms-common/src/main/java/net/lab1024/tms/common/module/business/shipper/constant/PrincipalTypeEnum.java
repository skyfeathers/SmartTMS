package net.lab1024.tms.common.module.business.shipper.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 货主负责人类型
 *
 * @author lidoudou
 * @date 2022/6/24 上午10:16
 */
public enum PrincipalTypeEnum implements BaseEnum {

    CUSTOMER_SERVICE(1, "客服"),

    MANAGER(2, "业务负责人");

    private Integer value;
    private String desc;

    PrincipalTypeEnum(Integer value, String desc) {
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
