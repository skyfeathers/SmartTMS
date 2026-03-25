package net.lab1024.tms.fixedasset.module.business.depreciation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 折旧单操作类型
 *
 * @author lidoudou
 * @date 2023/4/11 上午11:55
 */
@Getter
@AllArgsConstructor
public enum DepreciationDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    CANCEL(INIT_CODE + 1, "作废"),
    ;


    private Integer value;
    private String desc;


}
