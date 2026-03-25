package net.lab1024.tms.fixedasset.config;

import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.core.OperateLogAspect;
import net.lab1024.tms.common.module.support.operatelog.core.OperateLogConfig;
import net.lab1024.tms.common.module.support.operatelog.core.OperateLogUser;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yandy
 * @description:
 * @date 2022/5/20 10:13 上午
 */
@Configuration
public class OperateLogAspectConfig extends OperateLogAspect{

    /**
     * 配置信息
     * @return
     */
    @Override
    public OperateLogConfig getOperateLogConfig() {
        OperateLogConfig config = OperateLogConfig.builder().corePoolSize(4).queueCapacity(1000).build();
        return config;
    }

    /**
     * 请求用户信息
     * @param request
     * @return
     */
    @Override
    public OperateLogUser getOperateLogUser(HttpServletRequest request) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        if(requestUser == null){
            return null;
        }
        OperateLogUser operateLogUser = new OperateLogUser();
        operateLogUser.setUserId(requestUser.getUserId());
        operateLogUser.setUserName(requestUser.getUserName());
        return operateLogUser;
    }


}