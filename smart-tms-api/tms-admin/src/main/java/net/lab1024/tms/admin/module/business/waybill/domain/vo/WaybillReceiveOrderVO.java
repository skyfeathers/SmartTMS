package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;

/**
 * 应收帐款 展示信息
 *
 * @author lidoudou
 * @date 2022/7/21 上午9:47
 */
@Data
public class WaybillReceiveOrderVO {

    @ApiModelProperty("收款单ID")
    private Long receiveOrderId;

    @ApiModelProperty("收款单号")
    private String receiveOrderNumber;

    @ApiModelProperty("是否需要开票")
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("开票抬头")
    private String invoiceHeads;

    @ApiModelProperty("纳税人识别号")
    private String taxpayerIdentificationNumber;

    @ApiModelProperty("银行账户")
    private String accountNumber;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("应收对账备注")
    private String remark;

    @ApiModelPropertyEnum(desc = "是否核算", value = CheckStatusEnum.class)
    private Integer checkStatus;




}
