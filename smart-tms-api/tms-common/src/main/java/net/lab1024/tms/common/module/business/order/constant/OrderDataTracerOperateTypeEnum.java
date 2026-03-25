package net.lab1024.tms.common.module.business.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum OrderDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    CANCEL(INIT_CODE  + 1, "作废订单"),

    UPDATE_COST(INIT_CODE  + 2, "更新费用明细"),

    IMPORT_ORDER(INIT_CODE  + 3, "短驳订单导入"),

    UPDATE_SCHEDULE_FLAG(INIT_CODE  + 4, "修改分配状态"),

    ;


    private Integer value;
    private String desc;


}
