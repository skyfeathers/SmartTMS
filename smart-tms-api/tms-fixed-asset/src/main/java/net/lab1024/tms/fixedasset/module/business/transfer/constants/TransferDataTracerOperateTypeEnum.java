package net.lab1024.tms.fixedasset.module.business.transfer.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * 资产转移操作日志类型
 *
 * @author lidoudou
 * @date 2023/3/27 下午5:01
 */
@Getter
@AllArgsConstructor
public enum TransferDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    COMPLETE(INIT_CODE + 1, "完成转移"),

    REJECT(INIT_CODE + 2, "驳回转移"),
    ;


    private Integer value;
    private String desc;


}
