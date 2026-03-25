package net.lab1024.tms.fixedasset.module.business.repair.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 维修操作类型
 *
 * @author lidoudou
 * @date 2023/3/27 下午4:18
 */
@Getter
@AllArgsConstructor
public enum RepairDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    COMPLETE(INIT_CODE + 1, "完成维修");


    private Integer value;
    private String desc;


}
