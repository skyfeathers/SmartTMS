package net.lab1024.tms.common.module.business.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 合同签署人类型
 *
 * @author lihaifan
 * @date 2022/7/15 11:10
 */
@AllArgsConstructor
@Getter
public enum ContractSignerTypeEnum implements BaseEnum {
    DRIVER(1,"司机"),
    FLEET(2,"车队"),
    SHIPPER(3,"货主"),
    ;

    private Integer value;

    private String desc;
}
