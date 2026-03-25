package net.lab1024.tms.common.module.business.fleet.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum FleetDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    AUDIT(INIT_CODE + 1, "审核"),

    ADD_DRIVER(INIT_CODE + 2, "添加司机"),

    DEL_DRIVER(INIT_CODE + 3, "删除司机"),

    ADD_VEHICLE(INIT_CODE + 4, "添加车辆"),

    DEL_VEHICLE(INIT_CODE + 5, "删除车辆"),

    ADD_BANK(INIT_CODE + 6, "添加银行"),

    UPDATE_MANAGER(INIT_CODE + 7, "修改负责人");

    private Integer value;
    private String desc;


}
