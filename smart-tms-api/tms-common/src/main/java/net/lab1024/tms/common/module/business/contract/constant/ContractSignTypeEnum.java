package net.lab1024.tms.common.module.business.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 合同签署类型
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@AllArgsConstructor
@Getter
public enum ContractSignTypeEnum implements BaseEnum {
    ONLINE(1,"电子签约"),
    OFFLINE(2,"线下签约")
    ;

    private Integer value;

    private String desc;
}
