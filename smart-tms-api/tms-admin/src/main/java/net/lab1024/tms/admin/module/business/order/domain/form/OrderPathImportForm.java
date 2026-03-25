package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 运输路线列表
 *
 * @author lidoudou
 * @date 2022/12/14 下午4:43
 */
@Data
public class OrderPathImportForm {

    @ApiModelPropertyEnum(value = PathTypeEnum.class, desc = "类型")
    @NotNull(message = "运输路线类型不能为空")
    @CheckEnum(value = PathTypeEnum.class, message = "运输路线类型不正确")
    private Integer type;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contactName;

    @ApiModelProperty("联系电话")
    private String contactPhone;

}
