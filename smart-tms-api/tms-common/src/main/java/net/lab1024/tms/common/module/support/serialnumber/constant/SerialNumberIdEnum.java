package net.lab1024.tms.common.module.support.serialnumber.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * id生成枚举类
 *
 * @author Administrator
 */
@AllArgsConstructor
@Getter
public enum SerialNumberIdEnum implements BaseEnum {

    OIL_CARD_BALANCE_RECORD(1, "油卡余额变动记录ID"),

    ETC(2, "etc"),

    REFUELING_RECORD(3, "车辆加油记录"),

    WAYBILL(4, "运单"),

    ORDER(5, "订单"),

    CONTRACT(6, "合同"),

    PAY_ORDER(7, "付款单"),

    RECEIVE_ORDER(8, "收款单"),

    VEHICLE_COST(9, "车辆费用"),

    ASSET(10, "资产"),

    ASSET_PURCHASE(11, "资产采购"),

    ASSET_REQUISITION(12, "资产领用"),

    ASSET_REVERT(13, "资产退还"),

    ASSET_BORROW(14, "资产借用"),

    ASSET_BACK(15, "资产归还"),

    ASSET_ALLOCATION(16, "资产调拨"),

    ASSET_TRANSFER(17, "资产转移"),

    ASSET_REPAIR(18, "资产维修"),

    ASSET_SCRAP(19, "资产报废"),

    ASSET_CHECK(20, "资产盘点"),

    OA_COST_APPLY(21, "资产盘点"),

    CAR_COST(22, "自有车费用"),

    ASSET_DEPRECIATION(23, "资产折旧"),

    ASSET_CONSUMABLE_STOCK(24, "低值易耗品"),

    ASSET_CONSUMABLE_PURCHASE(25, "低值易耗品采购"),

    ASSET_CONSUMABLE_REQUISITION(26, "低值易耗品领用"),

    ASSET_CONSUMABLE_CHECK(27, "低值易耗品盘点"),

    CAR_PAY_COST_WAYBILL(28, "车辆费用维护"),
    ;

    private final Integer serialNumberId;

    private final String desc;

    @Override
    public Integer getValue() {
        return serialNumberId;
    }

    @Override
    public String toString() {
        return "SerialNumberIdEnum{" +
                "serialNumberId=" + serialNumberId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
