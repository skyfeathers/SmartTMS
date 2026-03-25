package net.lab1024.tms.admin.module.business.flow.instance.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 10:37
 */
@Data
public class FlowInstanceTaskHandlerVO {

    @ApiModelProperty("处理人id")
    private Long handlerId;

    @ApiModelProperty("处理人名称")
    private String handlerName;
}
