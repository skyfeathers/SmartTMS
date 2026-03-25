package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.TrackWayEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * [ 货主跟进 ]
 *
 * @author yandanyang
 * @date 2020/10/23 20:35
 */
@Data
public class ShipperTrackAddForm {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主id不能为空")
    private Long shipperId;

    @ApiModelProperty("跟进时间")
    @NotNull(message = "跟进时间不能为空")
    private LocalDateTime trackTime;

    @ApiModelProperty("下次跟进时间")
    private LocalDateTime nextTrackTime;

    @ApiModelProperty("被拜访人名称")
    @Length(max = 1000, message = "被拜访人名称不能多于5000字符")
    @NotBlank(message = "被拜访人名称不能为空")
    private String intervieweeName;

    @ApiModelPropertyEnum(desc = "跟进方式", value = TrackWayEnum.class)
    @NotNull(message = "跟进方式不能为空")
    private Integer trackWay;

    @ApiModelProperty("跟踪内容")
    @Length(max = 5000, message = "根据内容不能多于5000字符")
    private String trackContent;

    @ApiModelProperty("备注")
    @Length(max = 5000, message = "备注不能多于5000字符")
    private String remark;

    @ApiModelProperty("附件信息")
    private String attachment;

}
