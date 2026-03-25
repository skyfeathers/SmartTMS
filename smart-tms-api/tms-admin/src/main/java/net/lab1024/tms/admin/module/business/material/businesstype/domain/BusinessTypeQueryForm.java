package net.lab1024.tms.admin.module.business.material.businesstype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.material.businesstype.constant.TripTypeEnum;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 业务资料-业务类型-查询参数
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class BusinessTypeQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty("运输类型")
    @CheckEnum(value = TripTypeEnum.class, message = "业务类型错误")
    private Integer tripType;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
