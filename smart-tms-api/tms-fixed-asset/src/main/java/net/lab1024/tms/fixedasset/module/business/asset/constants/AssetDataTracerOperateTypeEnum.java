package net.lab1024.tms.fixedasset.module.business.asset.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 资产操作类型
 *
 * @author lidoudou
 * @date 2023/3/31 下午5:20
 */
@Getter
@AllArgsConstructor
public enum AssetDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    APPLY_REQUISITION(INIT_CODE + 1, "申请领用"),

    APPLY_REVERT(INIT_CODE + 2, "申请退还"),

    APPLY_BORROW(INIT_CODE + 3, "申请借用"),

    APPLY_BACK(INIT_CODE + 4, "申请归还"),

    APPLY_ALLOCATION(INIT_CODE + 5, "申请调拨"),

    APPLY_TRANSFER(INIT_CODE + 6, "申请转移"),

    ASSET_CANCEL(INIT_CODE + 7, "资产报废"),

    ASSET_REPAIR(INIT_CODE + 8, "资产维修"),
    ;


    private Integer value;
    private String desc;


}
