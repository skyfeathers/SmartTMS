package net.lab1024.tms.admin.module.business.shipper.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PaymentTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/1/22 上午10:49
 */
@Data
public class ShipperPaymentWayAddForm {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主id不能为空")
    private Long shipperId;

    @ApiModelPropertyEnum(value = PaymentTypeEnum.class, desc = "付款方式")
    @NotNull(message = "付款方式不能为空")
    @CheckEnum(value = PaymentTypeEnum.class)
    private Integer paymentType;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("开户支行名称")
    private String bankBranchName;

    @ApiModelProperty("银行或微信支付宝账户名")
    @NotNull(message = "银行或微信支付宝账户名不能为空")
    private String accountName;

    @ApiModelProperty("银行或微信支付宝账号")
    private String accountNumber;

    @ApiModelProperty("附件信息")
    @Length(max = 255, message = "附件URL超出最大长度255")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("收款二维码")
    @Length(max = 255, message = "收款二维码URL超出最大长度255")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String receiveImage;

    @ApiModelProperty("是否公户")
    private Boolean publicAccountFlag;

}