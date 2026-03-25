package net.lab1024.tms.common.module.support.dingding.auth;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 钉钉操作
 *
 * @author lidoudou
 * @date 2023/4/20 下午4:31
 */
@Slf4j
@Service
public class DingDingAuthService {

    @Value("${ding.app-key}")
    private String appKey;

    @Value("${ding.app-secret}")
    private String appSecret;

    public String getMobileByCode(String authCode) {
        try {
            String accessToken = this.getAccessTokenByCode(authCode);
            GetUserResponseBody userBody = getUserInfo(accessToken);
            log.info("【钉钉】登录获取的用户信息为：" + JSONObject.toJSONString(userBody));
            String mobile = userBody.getMobile();
            return mobile;
        } catch (Exception e) {
            log.error("【钉钉】获取用户信息报错，" + e.getMessage());
        }
        return null;
    }

    public String getAccessTokenByCode(String authCode){
        try {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            com.aliyun.dingtalkoauth2_1_0.Client client = new com.aliyun.dingtalkoauth2_1_0.Client(config);

            GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                    .setClientId(appKey)
                    .setClientSecret(appSecret)
                    .setCode(authCode)
                    .setGrantType("authorization_code");
            GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
            return getUserTokenResponse.getBody().getAccessToken();
        }catch (Exception e) {
            log.error("【钉钉】获取AccessToken报错，" + e.getMessage());
        }
        return null;
    }


    /**
     * 获取用户个人信息
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    public GetUserResponseBody getUserInfo(String accessToken) throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkcontact_1_0.Client client = new com.aliyun.dingtalkcontact_1_0.Client(config);

        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.setXAcsDingtalkAccessToken(accessToken);
        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        GetUserResponseBody resp = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody();
        return resp;
    }

}
