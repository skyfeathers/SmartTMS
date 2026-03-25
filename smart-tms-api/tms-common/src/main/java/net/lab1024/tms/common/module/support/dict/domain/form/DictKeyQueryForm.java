package net.lab1024.tms.common.module.support.dict.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * @author yandy
 * @description:
 * @date 2022/5/26 12:21 上午
 */
@Data
public class DictKeyQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    private String searchWord;

    @ApiModelProperty(value = "删除标识",hidden = true)
    private Boolean deletedFlag;
}