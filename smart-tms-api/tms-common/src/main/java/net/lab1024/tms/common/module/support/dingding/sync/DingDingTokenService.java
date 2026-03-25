package net.lab1024.tms.common.module.support.dingding.sync;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.constant.RedisKeyConst;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigCacheService;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import net.lab1024.tms.common.module.support.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 钉钉操作
 *
 * @author lidoudou
 * @date 2023/4/20 下午4:31
 */
@Slf4j
@Service
public class DingDingTokenService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private DingDingConfigCacheService dingDingConfigCacheService;

    /**
     * 获取accessToken
     *
     * @param enterpriseId
     * @return
     */
    public String getAccessToken(Long enterpriseId) {
        String token = redisService.get(String.format(RedisKeyConst.DING_DING.TOKEN, enterpriseId));
        if (token == null) {
            return this.requestToken(enterpriseId);
        }
        return token;
    }

    /**
     * 请求获取钉钉accessToken
     *
     * @param enterpriseId
     * @return
     */
    private String requestToken(Long enterpriseId) {
        try {
            DingDingConfigEntity configEntity = dingDingConfigCacheService.getConfig(enterpriseId);
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            Client client = new Client(config);

            GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                    .setAppKey(configEntity.getAppKey())
                    .setAppSecret(configEntity.getAppSecret());
            GetAccessTokenResponse accessTokenResponse = client.getAccessToken(getAccessTokenRequest);
            String accessToken = accessTokenResponse.getBody().getAccessToken();
            Long expires = accessTokenResponse.getBody().getExpireIn();
            redisService.set(String.format(RedisKeyConst.DING_DING.TOKEN, enterpriseId), accessToken, expires);
            return accessToken;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }
        return StringConst.EMPTY;
    }


}
