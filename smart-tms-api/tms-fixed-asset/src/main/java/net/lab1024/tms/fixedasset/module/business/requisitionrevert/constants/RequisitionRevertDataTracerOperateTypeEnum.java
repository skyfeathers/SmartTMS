package net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 资产领用
 *
 * @author lidoudou
 * @date 2023/3/28 下午5:31
 */
@Getter
@AllArgsConstructor
public enum RequisitionRevertDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    REQUISITION_COMPLETE(INIT_CODE + 1, "完成领用"),

    REQUISITION_REJECT(INIT_CODE + 2, "驳回领用"),
    ;


    private Integer value;
    private String desc;


}
