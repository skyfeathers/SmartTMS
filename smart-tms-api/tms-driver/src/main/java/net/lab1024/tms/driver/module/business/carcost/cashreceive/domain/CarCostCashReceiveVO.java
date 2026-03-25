package net.lab1024.tms.driver.module.business.carcost.cashreceive.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车-现金收入 VO
 *
 * @author zhaoxinyang
 * @date 2023/11/06 15:49
 */
@Data
public class CarCostCashReceiveVO {

    @ApiModelProperty("现金收入表ID")
    private Long cashReceiveId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;
    
    @ApiModelProperty("收入金额")
    private BigDecimal amount;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}