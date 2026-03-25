package net.lab1024.tms.fixedasset.module.business.borrowback.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 资产借用操作类型
 *
 * @author lidoudou
 * @date 2023/3/28 下午3:37
 */
@Getter
@AllArgsConstructor
public enum BorrowBackDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    BORROW_COMPLETE(INIT_CODE + 1, "完成借用"),

    BORROW_REJECT(INIT_CODE + 2, "驳回借用"),
    ;


    private Integer value;
    private String desc;


}
