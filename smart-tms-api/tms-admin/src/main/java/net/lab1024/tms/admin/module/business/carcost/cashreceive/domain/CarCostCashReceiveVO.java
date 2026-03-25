package net.lab1024.tms.admin.module.business.carcost.cashreceive.domain;

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
 * @date 2023/10/24 08:58
 */
@Data
public class CarCostCashReceiveVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("现金收入表ID")
    private Long cashReceiveId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司车辆ID机ID")
    private Long vehicleId;

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

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名字")
    private String createUserName;

    @ApiModelProperty("创建人类型")
    private Integer createUserType;

    @ApiModelProperty("创建人类型描述")
    private String createUserTypeDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}