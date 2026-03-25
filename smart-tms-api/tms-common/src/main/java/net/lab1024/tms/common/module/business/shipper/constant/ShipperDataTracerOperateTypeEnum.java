package net.lab1024.tms.common.module.business.shipper.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum ShipperDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    UPDATE_PRINCIPAL(INIT_CODE  + 1, "修改负责人"),
    UPDATE_MANAGE(INIT_CODE  + 2, "修改业务负责人"),
    AUDIT(INIT_CODE  + 3, "审核"),
    ADD_INVOICE(INIT_CODE  + 4, "添加开票信息"),
    UPDATE_VERIFY(INIT_CODE  + 5, "修改校验状态"),
    UPDATE_INVOICE(INIT_CODE  + 6, "修改开票信息"),
    ;


    private Integer value;
    private String desc;


}
