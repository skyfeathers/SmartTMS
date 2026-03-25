package net.lab1024.tms.common.module.support.ocr;

import cn.hutool.core.net.URLEncodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.lab1024.tms.common.config.OcrBaiduConfig;
import net.lab1024.tms.common.constant.RedisKeyConst;
import net.lab1024.tms.common.module.support.ocr.constant.OcrBaiduConstant;
import net.lab1024.tms.common.module.support.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

/**
 * @author yandy
 * @description:
 * @date 2022/12/3 2:10 下午
 */
@Service
public class OcrBaiduRequestService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OcrBaiduConfig ocrBaiduConfig;

    public String getToken() {
        String token = redisService.get(RedisKeyConst.BaiduOcr.TOKEN);
        if (token == null) {
            return this.requestToken();
        }
        return token;
    }

    private String requestToken() {
        String result = restTemplate.getForObject(OcrBaiduConstant.TOKEN_URL, String.class, ocrBaiduConfig.getClientKey(), ocrBaiduConfig.getClientSecret());
        if (StringUtils.isBlank(result)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(result);

        String accessToken = jsonObject.getString("access_token");
        Long expires = jsonObject.getLong("expires_in");
        redisService.set(RedisKeyConst.BaiduOcr.TOKEN, accessToken, expires);
        return accessToken;
    }


    public JSONObject requestOcr(String requestUrl, String imageUrl) {
        try {
            URL imageURL = new URL(imageUrl);
            URLConnection urlConnection = imageURL.openConnection();
            // 输入流转换为字节流
            byte[] bytes = FileCopyUtils.copyToByteArray(urlConnection.getInputStream());
            String base64Img = Base64.getEncoder().encodeToString(bytes);
            // token
            String accessToken = this.getToken();
            if (StringUtils.isBlank(accessToken)) {
                return null;
            }
            // 参数
            LinkedMultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("image", URLEncodeUtil.encode(base64Img));
            // header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap, headers);

            String res = restTemplate.postForObject(requestUrl, httpEntity, String.class, accessToken);
            JSONObject jsonObject = JSON.parseObject(res);
            return jsonObject;
        } catch (Exception e) {

        }
        return null;
    }



}