package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;

import java.time.LocalDate;

/**
 * 发票列表查询
 *
 * @author lidoudou
 * @date 2022/8/17 下午3:53
 */
@Data
public class ReceiveInvoiceQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("开票-开始时间")
    private LocalDate invoiceStartTime;

    @ApiModelProperty("开票-结束时间")
    private LocalDate invoiceEndTime;

    @ApiModelPropertyEnum(desc = "开票状态", value = InvoiceStatusEnum.class)
    private Integer invoiceStatus;

    @ApiModelPropertyEnum(value = CheckStatusEnum.class, desc = "核算状态", hidden = true)
    private Integer checkStatus;

    @ApiModelProperty(value = "是否需要开票")
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;

    @ApiModelProperty(value = "核销状态", hidden = true)
    private Boolean verificationFlag;

}
