package net.lab1024.tms.admin.module.business.capitalflow.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收的资金流水
 *
 * @author lidoudou
 * @date 2022/8/20 下午2:19
 */
@Data
public class ReceiveCapitalFlowVO {

    @ApiModelProperty("收款单ID")
    private Long receiveOrderId;

    @ApiModelProperty("收款单号")
    private String receiveOrderNumber;

    @ApiModelProperty("日期")
    private LocalDate verificationTime;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主简称")
    private String shortName;

    @ApiModelProperty("核销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("流水单号")
    private String sequenceCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    private String createUserName;

    private LocalDateTime createTime;

}
