package net.lab1024.tms.common.module.business.pic.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2021/1/23
 */
@Data
public class PicVO {

    @ApiModelProperty("id")
    private Long picId;

    @ApiModelPropertyEnum(value = PicTypeEnum.class, desc = "投放类型")
    private Integer type;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("图片key")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String imgKey;

    @ApiModelProperty("是否启用")
    private Boolean enableFlag;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("虚拟浏览量")
    private Integer virtualViewCount;

    @ApiModelProperty("开始有效时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束有效时间")
    private LocalDateTime endTime;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名字")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
