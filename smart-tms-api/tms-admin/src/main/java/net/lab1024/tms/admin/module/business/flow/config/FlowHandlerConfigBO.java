package net.lab1024.tms.admin.module.business.flow.config;

import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowHandlerTypeEnum;

import java.util.List;

/**
 * [ 流程处理人配置信息 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:25
 */
@Data
public class FlowHandlerConfigBO {

    /**
     * 处理人类型
     * {@link FlowHandlerTypeEnum}
     */
    private Integer handlerType;

    /**
     * id列表，类型为所有，此值为空
     */
    private List<Long> configIdList;

    /**
     * 类型为 自定义服务 此值必填
     */
    private String serviceName;

}
