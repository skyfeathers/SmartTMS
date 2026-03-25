package net.lab1024.tms.admin.module.business.pic.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;

/**
 * @author zhuoda
 * @Date 2021/1/23
 */

@Data
public class PicQueryForm extends PageParam {

    @ApiModelPropertyEnum(value = PicTypeEnum.class, desc = "投放类型")
    private Integer type;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
