package net.lab1024.tms.admin.module.business.capitalflow.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款流水
 *
 * @author lidoudou
 * @date 2022/8/20 下午2:25
 */
@Data
public class PayOrderCapitalFlowVO {

    @ApiModelProperty("收款单ID")
    private Long payOrderId;

    @ApiModelProperty("付款单号")
    private String payOrderNumber;

    @ApiModelProperty("司机名称")
    private String driverName;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("核销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("流水单号")
    private String sequenceCode;

    private String createUserName;

    private LocalDateTime createTime;

}
