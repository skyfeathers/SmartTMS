package net.lab1024.tms.common.module.business.oa.enterprise.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 企业类型
 *
 * @author lidoudou
 * @date 2022/7/28 下午5:37
 */
public enum EnterpriseTypeEnum implements BaseEnum {

    NORMAL(1, "非网络货运平台企业"),

    NFT(2, "网络货运平台企业"),
    ;

    private Integer value;
    private String desc;

    EnterpriseTypeEnum(Integer value, String desc) {
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
