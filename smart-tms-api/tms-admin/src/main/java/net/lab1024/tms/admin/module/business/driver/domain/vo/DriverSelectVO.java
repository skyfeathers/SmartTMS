package net.lab1024.tms.admin.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 司机下拉选择框
 *
 * @author lihaifan
 * @date 2022/7/11 10:38
 */
@Data
public class DriverSelectVO {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String telephone;

    @ApiModelProperty("速记码")
    private String shorthandCode;

    @ApiModelProperty("身份证号")
    private String drivingLicense;
}
