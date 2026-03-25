package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:32
 */
@Data
public class ShipperAddForm extends ShipperDTO {

    @ApiModelProperty("客服Id")
    private List<Long> customerServiceIdList;

    @ApiModelProperty("业务负责人")
    @Size(min = 1, message = "业务负责人不能为空")
    @Size(max = 1, message = "业务负责人只能选择一个")
    private List<Long> managerIdList;

    @ApiModelProperty("货主联系人信息")
    @Valid
    @Size(max = 50, message = "联系人单次提交做多50个")
    private List<ShipperContactDTO> contactList;

    @ApiModelProperty("货主付款方式信息")
    @Valid
    @Size(max = 50, message = "付款方式单次提交做多50个")
    private List<ShipperPaymentWayDTO> paymentWayList;

    @ApiModelProperty("货主开票信息")
    @Valid
    private List<ShipperInvoiceDTO> invoiceList;

    @ApiModelProperty("货主邮寄地址信息")
    @Valid
    @Size(max = 50, message = "邮寄地址信息单次提交做多50个")
    private List<ShipperMailAddressDTO> mailAddressList;

}
