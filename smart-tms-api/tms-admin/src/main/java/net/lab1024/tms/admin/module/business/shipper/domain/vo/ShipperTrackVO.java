package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.TrackWayEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/23 20:48
 */
@Data
public class ShipperTrackVO {

    @ApiModelProperty("货主跟进id")
    private Long trackId;

    @ApiModelProperty("跟进时间")
    private LocalDateTime trackTime;

    @ApiModelProperty("跟进人id")
    private Long employeeId;

    @ApiModelProperty("跟进人名称")
    private String employeeName;

    @ApiModelProperty("跟进人部门")
    private String departmentName;

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("被拜访人名称")
    private String intervieweeName;

    @ApiModelProperty("跟踪内容")
    private String trackContent;

    @ApiModelPropertyEnum(desc = "跟进方式", value = TrackWayEnum.class)
    @NotNull(message = "跟进方式不能为空")
    private Integer trackWay;

    @ApiModelProperty("下次跟进时间")
    private LocalDateTime nextTrackTime;

    @ApiModelProperty("附件信息")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
