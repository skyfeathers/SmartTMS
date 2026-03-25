package net.lab1024.tms.common.module.business.msg.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 消息 子类型枚举
 *
 * @author listen
 * @date 2022/07/22 20:20
 */
@Getter
@AllArgsConstructor
public enum MsgSubTypeEnum implements BaseEnum {
    /**
     * 请注意 类型是消息主类型 x10之后 顺延
     * 例如：审批开始是 1000 货主2000 司机 3000 等
     */

    // ----------------------------- 审批业务 1000 -----------------------------
    AUDIT_WAY_BILL_AUDIT(1000, MsgTypeEnum.AUDIT, "运单审批", "{msg}"),
    AUDIT_OIL_CARD_AUDIT(1001, MsgTypeEnum.AUDIT, "油卡充值审批", "{msg}"),
    AUDIT_PAY_AUDIT(1002, MsgTypeEnum.AUDIT, "应付款审批", "{msg}"),
    AUDIT_RECEIVE_AUDIT(1003, MsgTypeEnum.AUDIT, "应收款审批", "{msg}"),



    // ----------------------------- 货主业务 2000 -----------------------------
    SHIPPER_HE_TONG_DAO_QI(2000, MsgTypeEnum.SHIPPER, "货主合同到期", "货主【{name}】的身份证即将到期，请尽快进行更新~"),
    SHIPPER_FA_PIAO_ZHANG_DAN_SHANG_CHUAN(2001, MsgTypeEnum.SHIPPER, "货主发票账单已上传", "货主【{name}】的发票、账单已上传，请及时查看~"),



    // ----------------------------- 司机业务 3000 -----------------------------
    DRIVER_SHEN_FEN_ZHENG_DAO_QI(3000, MsgTypeEnum.DRIVER, "司机身份证到期", "司机【{name}】的身份证即将到期，请尽快进行更新~"),
    DRIVER_JIA_SHI_ZHENG_DAO_QI(3001, MsgTypeEnum.DRIVER, "司机驾驶证到期", "司机【{name}】的驾驶证即将到期，请尽快进行更新~"),
    DRIVER_CONG_YE_ZI_GE_ZHENG_DAO_QI(3002, MsgTypeEnum.DRIVER, "司机从业资格证到期", "司机【{name}】的从业资格证即将到期，请尽快进行更新~"),
    DRIVER_HE_TONG_DAO_QI(3003, MsgTypeEnum.DRIVER, "司机合同到期", "司机【{name}】的合同即将到期，请尽快进行更新~"),



    // ----------------------------- 挂车业务 4000 -----------------------------
    BRACKET_XING_SHI_ZHENG_DAO_QI(4000, MsgTypeEnum.BRACKET, "挂车行驶证到期", "挂车【{name}】的行驶证即将到期，请尽快进行更新~"),
    BRACKET_BAO_XIAN_DAO_QI(4001, MsgTypeEnum.BRACKET, "挂车保险到期", "挂车【{name}】的保险即将到期，请尽快进行更新~"),



    // ----------------------------- 车辆业务 5000 -----------------------------
    CAR_XING_SHI_ZHENG_DAO_QI(5000, MsgTypeEnum.CAR, "车辆行驶证到期", "车辆【{name}】的行驶证即将到期，请尽快进行更新~"),
    CAR_BAO_XIAN_DAO_QI(5001, MsgTypeEnum.CAR, "车辆保险到期", "车辆【{name}】的保险即将到期，请尽快进行更新~"),
    CAR_DAO_LU_YUN_SHU_XU_KE_ZHENG_DAO_QI(5002, MsgTypeEnum.CAR, "车辆道路运输许可证到期", "车辆【{name}】的道路运输许可证即将到期，请尽快进行更新~"),
    CAR_DAO_DA_BAO_YANG_SHI_JIAN(5003, MsgTypeEnum.CAR, "车辆到达保养时间", "车辆【{name}】已到达保养周期，请及时进行保养~"),



    // ----------------------------- 订单业务 6000 -----------------------------
    ORDER_DUI_ZHANG_DAN_ZUO_FEI(6000, MsgTypeEnum.ORDER, "对账单作废", "订单号：【{orderId}】所提交的对账单被作废，请核实信息重新提交~，请尽快处理~"),
    ORDER_CREATE(6001, MsgTypeEnum.ORDER, "订单创建", "您有一个新订单待分配司机，请尽快处理~"),
    ORDER_NOT_SCHEDULED(6002, MsgTypeEnum.ORDER, "订单未分配司机", "订单号：【{orderId}】，您有一个订单到发货时间还未派车，请尽快处理~"),

    // ----------------------------- 运单 7000 -----------------------------
    CONTRACT_EXPIRE(7000, MsgTypeEnum.CONTRACT, "合同到期", "合同号：【{contractCode}】已到期，请及时处理~"),
    RECEIVE_ORDER_ACCOUNT_PERIOD_DATE(7001, MsgTypeEnum.RECEIVE_ORDER, "应收账款", "客户：【{shortName}】应收编号：【{receiveOrderNumber}】已到账期，请及时处理~"),


    ;

    private final Integer value;

    private final MsgTypeEnum msgType;

    private final String desc;

    private final String content;
}
