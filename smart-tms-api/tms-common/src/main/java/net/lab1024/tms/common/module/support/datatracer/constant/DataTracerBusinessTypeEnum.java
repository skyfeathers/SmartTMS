package net.lab1024.tms.common.module.support.datatracer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [ 数据业务类型 ]
 *
 * @author 罗伊
 */
@AllArgsConstructor
@Getter
public enum DataTracerBusinessTypeEnum implements BaseEnum {

    SHIPPER(1, "货主"),

    VEHICLE(2, "车辆"),

    DRIVER(3, "司机"),

    FLEET(4, "车队"),

    BRACKET(5, "挂车"),

    WAYBILL(6, "运单"),

    ORDER(7, "订单"),

    PAY_ORDER(8, "付款单"),

    RECEIVE_ORDER(9, "收款单"),

    OIL_CARD(10, "油卡"),

    ETC(11, "ETC"),

    CONTRACT(12, "合同"),

    ASSET(13, "资产"),

    ASSET_PURCHASE(14, "资产采购"),

    ASSET_REQUISITION(15, "资产领用"),

    ASSET_REVERT(16, "资产退还"),

    ASSET_BORROW(17, "资产借用"),

    ASSET_BACK(18, "资产归还"),

    ASSET_ALLOCATION(19, "资产调拨"),

    ASSET_TRANSFER(20, "资产转移"),

    ASSET_REPAIR(21, "资产维修"),

    ASSET_CLEAR(22, "资产报废"),

    ASSET_CHECK(23, "资产盘点"),

    OA_COST_APPLY(24, "费用申请"),

    CAR_COST(25, "车辆费用"),

    ASSET_DEPRECIATION(26, "折旧"),

    CONSUMABLES_STOCK(27, "易耗品清单"),

    CONSUMABLES_STOCK_PURCHASE(28, "易耗品采购"),

    CONSUMABLES_STOCK_CHECK(29, "易耗品盘点"),

    CONSUMABLES_STOCK_REQUISITION(30, "易耗品领用"),

    CAR_PAY_WAYBILL(31, "挂靠车费用登记"),

    CAR_PAY_COST(32, "挂靠车费用报表"),

    VEHICLE_COST(33, "车辆费用"),

    ;

    private final Integer value;

    private final String desc;
}
