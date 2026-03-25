package net.lab1024.tms.common.module.business.pic.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

/**
 * @author zhuoda
 * @Date 2021/1/23
 */
@Data
public class PicSimpleVO {

    @ApiModelProperty("id")
    private Long picId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("图片key")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String imgKey;

    @ApiModelProperty("跳转数据")
    private String gotoData;
}
