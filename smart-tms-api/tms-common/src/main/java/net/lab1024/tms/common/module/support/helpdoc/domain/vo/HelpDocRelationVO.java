package net.lab1024.tms.common.module.support.helpdoc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 帮助文档 关联项目
 *
 * @author zhuoda
 * @date 2022/08/12 20:01
 */
@Data
public class HelpDocRelationVO {

    @ApiModelProperty("关联名称")
    private String relationName;

    @ApiModelProperty("关联id")
    private Long relationId;
}
