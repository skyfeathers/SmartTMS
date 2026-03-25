package net.lab1024.tms.common.module.business.bracket.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum BracketDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    AUDIT(INIT_CODE  + 1, "审核"),

    ADD_INSURANCE(INIT_CODE  + 2, "添加保险信息"),

    DEL_INSURANCE(INIT_CODE  + 3, "删除保险信息"),

    UPDATE_INSURANCE(INIT_CODE  + 4, "修改保险信息"),

    ADD_REPAIR(INIT_CODE  + 5, "添加维修信息"),

    DEL_REPAIR(INIT_CODE  + 6, "删除维修信息"),

    UPDATE_REPAIR(INIT_CODE  + 7, "修改维修信息"),

    UPDATE_MANAGER(INIT_CODE  + 8, "修改负责人"),
    ;


    private Integer value;
    private String desc;


}
