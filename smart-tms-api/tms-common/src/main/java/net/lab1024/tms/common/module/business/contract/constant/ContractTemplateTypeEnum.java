package net.lab1024.tms.common.module.business.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @description:
 * @date 2023/3/9 2:27 下午
 */
@AllArgsConstructor
@Getter
public enum ContractTemplateTypeEnum implements BaseEnum {
    CONSIGNMENT_TRANSPORTATION_CONTRACT(1, "委托运输协议"),
    YX_ROAD_TRANSPORT_CONTRACT(5, "公路运输合同"),
    CONTAINER_CONTRACT(10, "集装箱运输合同"),
    ;

    private Integer value;

    private String desc;


}
