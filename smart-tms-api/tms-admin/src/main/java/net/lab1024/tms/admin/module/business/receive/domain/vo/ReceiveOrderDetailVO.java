package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 应收帐款详情
 *
 * @author lidoudou
 * @date 2022/8/4 下午2:23
 */
@Data
public class ReceiveOrderDetailVO extends ReceiveOrderVO {

    @ApiModelProperty("运单明细")
    private List<ReceiveOrderWaybillVO> waybillList;

    @ApiModelProperty("开票明细")
    private List<ReceiveOrderInvoiceVO> invoiceList;
}
