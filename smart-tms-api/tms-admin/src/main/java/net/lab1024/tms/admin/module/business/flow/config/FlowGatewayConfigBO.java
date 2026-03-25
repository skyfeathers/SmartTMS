package net.lab1024.tms.admin.module.business.flow.config;

import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskGatewayTypeEnum;

/**
 * [ 网关节点配置信息 ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:46
 */
@Data
public class FlowGatewayConfigBO {

    /**
     * 路由判断类型类型
     * {@link FlowTaskGatewayTypeEnum}
     */
    private Integer gatewayType;

    /**
     * 脚本
     */
    private String script;
}
