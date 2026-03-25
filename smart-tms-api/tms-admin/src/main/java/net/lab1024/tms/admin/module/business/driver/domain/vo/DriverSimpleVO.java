package net.lab1024.tms.admin.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;

/***
 * 司机的基础信息
 *
 * @author lidoudou
 * @date 2022/10/18 下午1:19
 */
@Data
public class DriverSimpleVO {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String telephone;

    @ApiModelProperty("所属企业ID")
    private Long enterpriseId;

    @ApiModelProperty("所属企业名称")
    private String enterpriseName;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class,desc = "经营方式")
    private Integer businessMode;
}
