package net.lab1024.tms.job.module.login;

import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.module.support.token.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenDeleteTask {

    @Resource
    private TokenService tokenService;

    /**
     * 删除登录token
     */
    @Scheduled(cron = "0 0 20 * * ?")
    public void deleteLoginToken() {
        tokenService.deleteLoginToken(UserTypeEnum.ADMIN);
    }

}
