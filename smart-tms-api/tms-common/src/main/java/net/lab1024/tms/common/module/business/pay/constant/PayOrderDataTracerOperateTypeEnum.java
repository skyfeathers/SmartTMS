package net.lab1024.tms.common.module.business.pay.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum PayOrderDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    PAY(INIT_CODE + 1, "支付"),
    VERIFICATION(INIT_CODE + 2, "核销"),

    CANCEL(INIT_CODE + 3, "作废"),

    NFT_PAY(INIT_CODE + 4, "提交NFT付款"),

    NFT_PAY_SUCCESS(INIT_CODE + 5, "网络货运付款成功"),

    NFT_PAY_FAIL(INIT_CODE + 6, "网络货运付款失败"),

    NFT_CANCEL(INIT_CODE + 7, "网络货运作废"),
    ;


    private Integer value;
    private String desc;


}
