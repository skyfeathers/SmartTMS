package net.lab1024.tms.admin.module.business.oa.cost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.cost.constant.CostApplyStatusEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:47
 */
@Data
public class CostApplyVO {

    private Long applyId;

    @ApiModelProperty("申请单号")
    private String applyNo;

    @ApiModelProperty("申请时间")
    private LocalDate applyDate;

    @ApiModelPropertyEnum(desc = "状态", value = CostApplyStatusEnum.class)
    private Integer status;

    @ApiModelProperty("所属企业 - ID")
    private Long enterpriseId;

    @ApiModelProperty("所属企业")
    private String enterpriseName;

    @ApiModelProperty("所属部门 - ID")
    private Long departmentId;

    @ApiModelProperty("所属部门")
    private String departmentName;

    @ApiModelProperty("申请人 - ID")
    private Long applyUserId;

    @ApiModelProperty("申请人")
    private String applyUserName;

    @ApiModelProperty("申请总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("申请说明")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}