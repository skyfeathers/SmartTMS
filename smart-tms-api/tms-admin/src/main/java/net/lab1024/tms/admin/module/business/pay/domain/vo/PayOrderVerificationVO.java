package net.lab1024.tms.admin.module.business.pay.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.time.LocalDateTime;

/**
 * 核销
 * @author yandy
 * @Date 2022-08-13
 */
@Data
public class PayOrderVerificationVO {

    private Long payOrderVerificationId;

    @ApiModelProperty("付款单id")
    private Long payOrderId;

    @ApiModelProperty("核销附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String verificationAttachment;

    @ApiModelProperty("核销备注")
    private String verificationRemark;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
