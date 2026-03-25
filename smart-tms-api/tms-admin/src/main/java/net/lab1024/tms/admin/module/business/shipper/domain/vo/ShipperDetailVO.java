package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [ 货主详情 ]
 *
 * @author yandanyang
 * @date 2020/7/30 10:39
 */
@Data
public class ShipperDetailVO extends ShipperDTO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("货主的人员信息")
    private List<ShipperPrincipalDTO> shipperPrincipalList;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("货主联系人信息")
    private List<ShipperContactDTO> contactList;

    @ApiModelProperty("货主付款方式信息")
    private List<ShipperPaymentWayDTO> paymentWayList;

    @ApiModelProperty("货主开票信息")
    private List<ShipperInvoiceDTO> invoiceList;

    @ApiModelProperty("货主邮寄信息")
    private List<ShipperMailAddressDTO> mailAddressList;

}
