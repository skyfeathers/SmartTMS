package net.lab1024.tms.common.config;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 12:17 下午
 */
@Configuration
public class OcrAliConfig {

    @Value("${ali.ocr.endpoint}")
    private String endpoint;

    @Value("${ali.ocr.access-key}")
    private String accessKey;

    @Value("${ali.ocr.secret-key}")
    private String secretKey;

    @Bean
    public Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(secretKey)
                .setEndpoint(endpoint);
        return new Client(config);
    }
}