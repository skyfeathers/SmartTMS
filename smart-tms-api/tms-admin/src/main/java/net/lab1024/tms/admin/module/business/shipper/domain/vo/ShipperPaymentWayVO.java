package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PaymentTypeEnum;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 支付方式
 *
 * @author yandanyang
 * @date 2020/8/31 9:10
 */
@Data
public class ShipperPaymentWayVO {

    @ApiModelProperty("paymentWayId")
    private Long paymentWayId;

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelPropertyEnum(value = PaymentTypeEnum.class, desc = "付款方式")
    private Integer paymentType;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("开户支行名称")
    private String bankBranchName;

    @ApiModelProperty("银行或微信支付宝账户名")
    private String accountName;

    @ApiModelProperty("银行或微信支付宝账号")
    private String accountNumber;

    @ApiModelProperty("附件信息")
    private String attachment;

    @ApiModelProperty("附件信息VOList,详情展示")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private List<FileVO> attachmentList;

    @ApiModelProperty("收款二维码")
    @Length(max = 255, message = "收款二维码URL超出最大长度255")
    private String receiveImage;

    @ApiModelProperty("收款二维码Url,详情展示")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String receiveImageUrl;

    @ApiModelProperty("默认标识 1:默认 0:非默认")
    private Boolean defaultFlag;

}
