package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 金额变动VO
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:41
 */
@Data
public class OilCardBalanceRecordVO {

    @ApiModelProperty("主键ID")
    private Long balanceRecordId;

    private Long relatedRecordId;

    @ApiModelProperty("关联编号")
    private String relatedOilCardNo;

    @ApiModelProperty("编号")
    private String balanceRecordNo;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡编号")
    private String oilCardNo;

    @ApiModelPropertyEnum(desc = "交易记录类型", value = OilCardBalanceRecordTypeEnum.class)
    private Integer recordType;

    @ApiModelProperty("交易记录类型描述")
    private String recordTypeDesc;

    @ApiModelProperty("变动前金额")
    private BigDecimal beforeBalance;

    @ApiModelProperty("变动金额")
    private BigDecimal changeBalance;

    @ApiModelProperty("变动金额")
    private BigDecimal afterBalance;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime transactionTime;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
