package net.lab1024.tms.common.module.business.vehicle.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum VehicleDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    AUDIT(INIT_CODE  + 1, "审核"),
    ENABLE(INIT_CODE  + 2, "启用"),
    DISABLE(INIT_CODE  + 3, "禁用"),

    ADD_INSURANCE(INIT_CODE  + 4, "添加保险信息"),

    DEL_INSURANCE(INIT_CODE  + 5, "删除保险信息"),

    UPDATE_INSURANCE(INIT_CODE  + 6, "修改保险信息"),

    ADD_REPAIR(INIT_CODE  + 7, "添加维修信息"),

    DEL_REPAIR(INIT_CODE  + 8, "删除维修信息"),

    UPDATE_REPAIR(INIT_CODE  + 9, "修改维修信息"),

    UPDATE_MANAGER(INIT_CODE  + 10, "修改负责人"),

    ADD_REVIEW(INIT_CODE  + 11, "添加审车信息"),

    UPDATE_REVIEW(INIT_CODE  + 12, "更新审车信息"),

    DEL_REVIEW(INIT_CODE  + 13, "删除审车信息"),

    ADD_MAINTENANCE(INIT_CODE  + 14, "添加保养信息"),

    UPDATE_MAINTENANCE(INIT_CODE  + 15, "更新保养信息"),

    DEL_MAINTENANCE(INIT_CODE  + 16, "删除保养信息"),

    ;


    private Integer value;
    private String desc;


}
