package net.lab1024.tms.admin.module.business.flow.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskGatewayTypeEnum;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskEntity;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 11:23
 */
@Service
public class FlowGatewayConfigService {
    private final Object lock = new Object();

    @Autowired
    private ApplicationContext applicationContext;

    private BeanResolver beanResolver;

    private BeanResolver getBeanResolver() {
        if (this.beanResolver != null) {
            return this.beanResolver;
        }
        synchronized(this.lock) {
            this.beanResolver = new BeanFactoryResolver(applicationContext) ;
        }
        return this.beanResolver;
    }
    /**
     * 获取首个判断未true的路由节点
     *
     * @param taskList
     * @param businessData
     * @return
     */
    public FlowTaskEntity getGatewayTask(List<FlowTaskEntity> taskList, Object businessData) {
        if (CollectionUtils.isEmpty(taskList)) {
            throw new BusinessException("任务节点不能为空");
        }
        for (FlowTaskEntity flowTaskEntity : taskList) {
            if (this.gatewayJudge(flowTaskEntity, businessData)) {
                return flowTaskEntity;
            }
        }
        return null;
    }


    private Boolean gatewayJudge(FlowTaskEntity taskEntity, Object businessData) {
        if (!FlowTaskTypeEnum.GATEWAY.equalsValue(taskEntity.getTaskType())) {
            throw new BusinessException("任务节点不是网关节点。。");
        }
        FlowGatewayConfigBO gatewayConfig = JSON.parseObject(taskEntity.getTaskConfig(), FlowGatewayConfigBO.class);
        Integer gatewayType = gatewayConfig.getGatewayType();
        if(FlowTaskGatewayTypeEnum.JS.equalsValue(gatewayType)){
            return this.jsJudge(gatewayConfig.getScript(),businessData);
        }
        if(FlowTaskGatewayTypeEnum.SPEL.equalsValue(gatewayType)){
            return this.spelJudge(gatewayConfig.getScript(),businessData);
        }
        return false;
    }


    public Boolean spelJudge(String script,Object businessData){
        StandardEvaluationContext  context = new StandardEvaluationContext();
        context.setBeanResolver(this.getBeanResolver());
        context.setVariable("businessData", businessData);
        ExpressionParser parser = FlowSpelParserManager.getInstance().getSpelParser();
        Boolean result = parser.parseExpression(script).getValue(context, Boolean.class);
        return result;
    }


    public boolean jsJudge(String script,Object businessData) {
        ScriptEngine javaScriptEngine = FlowJsScriptEngineManager.getInstance().getJavaScriptEngine();
        try {
            Compilable compilable = (Compilable) javaScriptEngine;
            CompiledScript javascript = compilable.compile(script);

            Bindings bindings = javaScriptEngine.createBindings();
            bindings.put("businessData", businessData);

            Object eval = javascript.eval(bindings);
            return BooleanUtils.toBoolean(eval.toString());
        } catch (Exception e) {
            throw new BusinessException("JS 网关判断错误:{}",e);
        }
    }

    public ResponseDTO<String> checkFlowGatewayConfig(String taskConfig) {
        if (StrUtil.isBlank(taskConfig)) {
            return ResponseDTO.userErrorParam("审批流程配置配置不能为空");
        }
        ResponseDTO<FlowGatewayConfigBO> parseResp = this.parseConfig(taskConfig, FlowGatewayConfigBO.class);
        if (!parseResp.getOk()) {
            return ResponseDTO.userErrorParam("审批流程配置格式错误");
        }
        FlowGatewayConfigBO configBO = parseResp.getData();
        if (configBO == null) {
            return ResponseDTO.userErrorParam("审批流程配置不能为空");
        }
        Integer gatewayType = configBO.getGatewayType();
        if (gatewayType == null || SmartBaseEnumUtil.checkEnum(gatewayType, FlowTaskGatewayTypeEnum.class)) {
            return ResponseDTO.userErrorParam("审批流程配置网关类型错误");
        }
        if (StrUtil.isBlank(configBO.getScript())) {
            return ResponseDTO.userErrorParam("审批流程配置网关脚本不能为空");
        }

        return ResponseDTO.ok();
    }

    public <T> ResponseDTO<T> parseConfig(String flowConfig, Class<T> clazz) {
        try {
            T configBO = JSON.parseObject(flowConfig, clazz);
            return ResponseDTO.ok(configBO);
        } catch (Exception e) {
            return ResponseDTO.userErrorParam();
        }
    }

}
