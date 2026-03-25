package net.lab1024.tms.admin.module.business.review.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 审车列表VO
 *
 * @author zhaoxinyang
 * @date 2023/10/18 14:01
 */
@Data
public class ReviewVO {

    @ApiModelProperty("审车ID")
    private Long reviewId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车辆ID")
    private String vehicleNumber;

    @ApiModelProperty("公司名字")
    private String enterpriseName;

    @ApiModelProperty("审车人")
    private String reviewPerson;

    @ApiModelProperty("审车费用")
    private BigDecimal reviewAmount;

    @ApiModelProperty("审车地点")
    private String reviewPlant;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审车时间")
    private LocalDate reviewDate;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}