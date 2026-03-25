package net.lab1024.tms.admin.module.business.carcost.cashpay.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车现金支出 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/25 10:24
 */
@Data
public class CarCostCashPayVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("司车辆ID机ID")
    private Long vehicleId;

    @ApiModelProperty("现金支出表ID")
    private Long cashPayId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支出方式")
    private Integer payMode;

    @ApiModelProperty("支出金额")
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

    @ApiModelProperty("创建人名字")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}