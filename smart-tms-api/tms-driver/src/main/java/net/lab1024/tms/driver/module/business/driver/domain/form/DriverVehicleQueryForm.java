package net.lab1024.tms.driver.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/***
 * 根据司机查询车辆信息
 *
 * @author zhaoxinyang
 * @date 2023/8/28 18:12
 */
@Data
public class DriverVehicleQueryForm extends PageParam {

    @ApiModelProperty(value = "司机ID", hidden = true)
    private Long driverId;

    @ApiModelProperty(value = "用户类型", hidden = true)
    private Integer userType;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty(value = "删除标记", hidden = true)
    private Boolean deletedFlag;

}
