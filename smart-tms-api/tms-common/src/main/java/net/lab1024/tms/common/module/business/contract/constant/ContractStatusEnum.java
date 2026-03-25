package net.lab1024.tms.common.module.business.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 合同状态
 *
 * @author lihaifan
 * @date 2022/7/15 11:12
 */
@AllArgsConstructor
@Getter
public enum ContractStatusEnum implements BaseEnum {
    WAIT_SIGN(0,"待签署"),
    SIGNED(1,"已签署"),
    CANCEL(2,"已作废"),
    ;

    private Integer value;

    private String desc;
}
