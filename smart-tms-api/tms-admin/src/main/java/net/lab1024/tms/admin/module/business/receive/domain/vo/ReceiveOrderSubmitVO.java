package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperMailAddressDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 提交对账的展示信息
 *
 * @author lidoudou
 * @date 2022/8/2 下午2
 */
@Data
public class ReceiveOrderSubmitVO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("订单所属企业")
    private Long enterpriseId;

    @ApiModelProperty("业务时间")
    private LocalDate businessDate;
}
