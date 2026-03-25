package net.lab1024.tms.common.module.business.waybill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

/**
 * @author yandy
 */

@Getter
@AllArgsConstructor
public enum WaybillDataTracerOperateTypeEnum implements DataTracerOperateTypeEnum {

    UPDATE_CONTAINER_NUMBER(INIT_CODE  + 1, "修改箱号"),
    UPDATE_LEADSEAL_NUMBER(INIT_CODE  + 2, "修改铅封号"),
    UPDATE_COST(INIT_CODE  + 3, "修改费用"),
    ADD_VOUCHER(INIT_CODE  + 4, "添加凭证"),
    CANCEL(INIT_CODE  + 5, "作废"),
    DRIVER_GOODS(INIT_CODE  + 6, "发货"),
    RECEIVE_GOODS(INIT_CODE  + 7, "收货"),
    COMPLETE(INIT_CODE  + 8, "运输完成"),
    SUBMIT_RECEIVE_ORDER(INIT_CODE  + 9, "提交应收对账"),
    CANCEL_OIL_CARD_RECHARGE(INIT_CODE  + 10, "作废油卡充值申请"),
    PAY_COMPLETE(INIT_CODE  + 11, "确认支付完成"),
    UPLOAD_RECEIPT_ATTACHMENT(INIT_CODE + 12, "上传回单"),
    UPLOAD_TRUNK_ORDER_ATTACHMENT(INIT_CODE + 13, "上传派车单"),
    NFT_UPDATE_STATUS_FAIL(INIT_CODE + 14, "网络货运状态更新失败"),
    NFT_UPDATE_STATUS(INIT_CODE + 15, "网络货运状态更新"),
    NFT_VOUCHER(INIT_CODE + 16, "网络货运凭证上传"),
    ORDER_SHIPPER_UPDATE(INIT_CODE  + 17, "订单货主变动"),
    SPILT_TRANSPORT(INIT_CODE  + 18, "分段运输"),
    UPDATE_PATH(INIT_CODE + 19, "更新运输路线"),
    UPDATE_VOUCHER(INIT_CODE + 20, "编辑凭证"),
    DELETE_VOUCHER(INIT_CODE + 21, "删除凭证"),
    ;


    private Integer value;
    private String desc;


}
