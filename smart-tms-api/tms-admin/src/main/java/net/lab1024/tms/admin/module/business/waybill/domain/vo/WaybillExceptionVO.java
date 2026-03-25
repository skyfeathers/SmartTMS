package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillExceptionTypeEnum;

import java.time.LocalDateTime;

/**
 * 运单异常
 *
 * @author lihaifan
 * @date 2022/12/3 15:41
 */
@Data
public class WaybillExceptionVO {

    @ApiModelProperty("异常ID")
    private Long exceptionId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelPropertyEnum(value = WaybillExceptionTypeEnum.class, desc = "异常类型")
    private Integer exceptionType;

    @ApiModelProperty("异常描述")
    private String exceptionMessage;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer createUserType;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
