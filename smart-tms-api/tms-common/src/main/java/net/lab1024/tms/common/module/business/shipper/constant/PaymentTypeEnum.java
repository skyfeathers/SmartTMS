package net.lab1024.tms.common.module.business.shipper.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 付款方式 ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:23
 */
public enum PaymentTypeEnum implements BaseEnum {

    BANK(0, "银行卡" ),

    WE_CHAT(1, "微信" ),

    ZHI_FU_BAO(2, "支付宝" );

    private Integer value;
    private String desc;

    PaymentTypeEnum(Integer value, String desc) {
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
