package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperGradeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTagEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/24 9:42
 */
@Data
public class ShipperTrackListVO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelPropertyEnum(value = ShipperGradeEnum.class, desc = "等级")
    private Integer grade;

    @ApiModelPropertyEnum(value = ShipperTagEnum.class, desc = "标签")
    private Integer tagType;

    @ApiModelPropertyEnum(value = ShipperTypeEnum.class, desc = "货主类型")
    private List<Integer> shipperTypeList;

    @ApiModelProperty("负责人id")
    private Long managerId;

    @ApiModelProperty("负责人名称")
    private String managerName;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("省编码")
    private Integer provinceCode;

    @ApiModelProperty("省名称")
    @Length(max = 50, message = "省不能超过50字符")
    private String provinceName;

    @ApiModelProperty("市编码")
    private Integer cityCode;

    @ApiModelProperty("市名称")
    @Length(max = 50, message = "市不能超过50字符")
    private String cityName;

    @ApiModelProperty("区编码")
    private Integer districtCode;

    @ApiModelProperty("区名称")
    @Length(max = 100, message = "区不能超过100字符")
    private String districtName;

    @ApiModelProperty("详细地址")
    @Length(max = 500, message = "货主详细地址不能超过500字符")
    private String address;

    @ApiModelProperty("最后一次跟进时间")
    private LocalDateTime lastTrackTime;

    @ApiModelProperty("最后一次跟踪内容")
    private String lastTrackContent;

    @ApiModelProperty("最后一次跟进时间间隔天数")
    private Long lastTrackDurationDays;

    private LocalDateTime createTime;

}
