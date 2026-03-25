package net.lab1024.tms.fixedasset.module.business.transfer.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 转移状态
 *
 * @author lidoudou
 * @date 2023/3/21 下午2:45
 */
@Getter
@AllArgsConstructor
public enum TransferStatusEnum implements BaseEnum {

    WAIT(1, "已提交"),

    REJECT(2,"已驳回"),

    COMPLETE(3, "已完成");

    private final Integer value;

    private final String desc;
}
