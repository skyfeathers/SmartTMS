package net.lab1024.tms.common.module.business.driver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum DriverDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    AUDIT(INIT_CODE  + 1, "审核"),
    ACTIVE(INIT_CODE  + 2, "激活"),
    DISABLE(INIT_CODE  + 3, "禁用"),
    ADD_BANK(INIT_CODE  + 4, "添加银行"),
    UPDATE_MANAGER(INIT_CODE  + 5, "修改负责人"),

    UPDATE_BANK(INIT_CODE  + 6, "更新银行"),

    ;


    private Integer value;
    private String desc;


}
