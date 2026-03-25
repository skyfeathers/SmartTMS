package net.lab1024.tms.common.module.business.waybill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @description:
 * @date 2023/3/16 1:54 下午
 */
@AllArgsConstructor
@Getter
public enum WaybillYunJianStatusEnum implements BaseEnum {

    NO_SYNC(0, "未同步"),

    FAIL(1, "失败"),

    SUCCESS(2, "成功"),

    ;

    private Integer value;
    private String desc;
}