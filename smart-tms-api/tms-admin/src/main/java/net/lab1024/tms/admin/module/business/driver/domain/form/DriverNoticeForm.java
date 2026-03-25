package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 司机消息对象
 *
 * @author lidoudou
 * @date 2022/7/1 下午3:23
 */
@Data
public class DriverNoticeForm {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机名称")
    private String driverName;

    @ApiModelProperty("到期日")
    private Integer expireDay;
}
