package net.lab1024.tms.fixedasset.module.business.check.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 盘点操作类型
 *
 * @author lidoudou
 * @date 2023/3/27 上午11:47
 */

@Getter
@AllArgsConstructor
public enum CheckDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    COMPLETE(INIT_CODE + 1, "盘点完成"),

    CHECK(INIT_CODE + 2, "盘点");


    private Integer value;
    private String desc;


}
