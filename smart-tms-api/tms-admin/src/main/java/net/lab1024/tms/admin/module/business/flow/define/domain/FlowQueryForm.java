package net.lab1024.tms.admin.module.business.flow.define.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

/**
 * 分页查询 审批流程
 *
 * @author zhaoxinyang
 * @date 2024/9/25 11:23
 */
@Data
public class FlowQueryForm {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelPropertyEnum(value = FlowTypeEnum.class, desc = "流程类型")
    private Integer flowType;

}
