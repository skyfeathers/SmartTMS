package net.lab1024.tms.common.module.business.receive.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum ReceiveOrderDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    CHECK(INIT_CODE + 1, "确认核算"),

    CANCEL_CHECK(INIT_CODE + 2, "作废核算"),

    VERIFICATION(INIT_CODE + 3, "核销"),

    INVOICE(INIT_CODE + 4, "开票"),

    CANCEL_INVOICE(INIT_CODE + 5, "作废开票申请"),

    UPLOAD_BILL(INIT_CODE + 6, "上传对账单"),

    UPDATE_INVOICE(INIT_CODE + 7, "更新开票"),
    ;


    private Integer value;
    private String desc;


}
