package net.lab1024.tms.admin.module.business.receive.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收帐款详情
 *
 * @author lidoudou
 * @date 2022/7/22 上午10:17
 */
@Data
public class ReceiveOrderVerificationVO {

    @ApiModelProperty("应收帐款ID")
    private Long receiveOrderId;

    @ApiModelProperty("核销记录单号")
    private String recordNumber;

    @ApiModelProperty("核销日期")
    private LocalDate verificationTime;

    @ApiModelProperty("核销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("资金流水")
    private String sequenceCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
