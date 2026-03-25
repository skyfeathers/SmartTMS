package net.lab1024.tms.admin.module.business.pic.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */
@Data
public class PicAddForm {

    @ApiModelProperty("分类id")
    private List<Long> categoryIdList;

    @ApiModelPropertyEnum(value = PicTypeEnum.class, desc = "投放类型")
    @NotNull(message = "请选择投放类型")
    @CheckEnum(value = PicTypeEnum.class, required = true, message = "投放类型错误")
    private Integer type;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("图片")
    @NotBlank(message = "图片不能为空")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String imgKey;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("开始有效时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束有效时间")
    private LocalDateTime endTime;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty(hidden = true)
    private String createUserName;

}
