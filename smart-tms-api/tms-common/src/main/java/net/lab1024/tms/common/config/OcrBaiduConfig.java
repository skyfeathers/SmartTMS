package net.lab1024.tms.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 12:17 下午
 */
@Configuration
@Data
public class OcrBaiduConfig {


    @Value("${baidu.ocr.client-key}")
    private String clientKey;

    @Value("${baidu.ocr.client-secret}")
    private String clientSecret;

}