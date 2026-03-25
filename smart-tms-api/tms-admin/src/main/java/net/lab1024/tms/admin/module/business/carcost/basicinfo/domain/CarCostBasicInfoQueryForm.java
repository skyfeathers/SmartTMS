package net.lab1024.tms.admin.module.business.carcost.basicinfo.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 自有车基本信息分页查询
 *
 * @author zhaoxinyang
 * @date 2024/9/27 11:27
 */
@Data
public class CarCostBasicInfoQueryForm extends PageParam {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("运单号")
    @Length(max = 200, message = "运单号最多200个字符")
    private String waybillNumber;

    @ApiModelProperty("箱号")
    @Length(max = 200, message = "箱号最多200个字符")
    private String containerNumber;

    @ApiModelProperty("确认状态")
    private Boolean confirmFlag;

    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;
}
