package net.lab1024.tms.admin.module.business.etc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 金额变动VO
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:41
 */
@Data
public class EtcBalanceRecordVO {

    @ApiModelProperty("主键ID")
    private Long balanceRecordId;

    @ApiModelProperty("编号")
    private String balanceRecordNo;

    @ApiModelProperty("etc ID")
    private Long etcId;

    @ApiModelProperty("收入标识")
    private Boolean incomeFlag;

    @ApiModelProperty("变动前金额")
    private BigDecimal beforeBalance;

    @ApiModelProperty("变动金额")
    private BigDecimal changeBalance;

    @ApiModelProperty("变动金额")
    private BigDecimal afterBalance;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
