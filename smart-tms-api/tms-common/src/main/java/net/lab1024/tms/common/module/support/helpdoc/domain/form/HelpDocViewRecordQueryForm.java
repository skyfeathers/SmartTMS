package net.lab1024.tms.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;

@Data
public class HelpDocViewRecordQueryForm extends PageParam {

    @ApiModelProperty("帮助文档id")
    @NotNull(message = "帮助文档id不能为空")
    private Long helpDocId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("关键字")
    private String keywords;


}
