package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 单据类型
 *
 * @author lidoudou
 * @date 2023/4/18 下午5:09
 */
@Getter
@AllArgsConstructor
public enum ConsumablesStockRecordTypeEnum implements BaseEnum {

    PURCHASE(1, "耗材入库"),

    REQUISITION(2, "耗材领用"),

    CHECK(3, "耗材盘点");


    private Integer value;
    private String desc;


}
