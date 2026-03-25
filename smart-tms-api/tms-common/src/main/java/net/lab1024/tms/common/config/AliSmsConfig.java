package net.lab1024.tms.common.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 12:17 下午
 */
@Data
@Configuration
@ConfigurationProperties("ali.sms")
public class AliSmsConfig {

    private String accessKey;

    private String secretKey;

    private String regionId;

    private String apiUrl;

    private String signName;

    private Map<String, String> templateCodeMap;

    protected static final String ALI_SMS_CLIENT = "aliSmsClient";

    @Bean(name = ALI_SMS_CLIENT)
    public IAcsClient initAliSmsClient() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, secretKey);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    public String getTemplateCode(String key) {
        if (key == null || templateCodeMap == null) {
            return null;
        }
        return templateCodeMap.get(key);
    }
}