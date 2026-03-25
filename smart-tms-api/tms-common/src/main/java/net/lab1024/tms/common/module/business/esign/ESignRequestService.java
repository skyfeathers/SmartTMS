package net.lab1024.tms.common.module.business.esign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.config.RestTemplateConfig;
import net.lab1024.tms.common.module.business.esign.domain.base.ESignResponseDTO;
import net.lab1024.tms.common.module.business.esign.domain.base.ESignUploadResponseDTO;
import net.lab1024.tms.common.module.business.esign.domain.entity.ESignRequestRecordEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.EnterpriseESignConfigDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseESignConfigEntity;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * e签宝接口请求
 *
 * @author lihaifan
 * @date 2022/7/14 14:31
 */
@Service
@Slf4j
public class ESignRequestService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestTemplateConfig restTemplateConfig;
    @Autowired
    private ESignRequestRecordDao eSignRequestRecordDao;
    @Autowired
    private EnterpriseESignConfigDao enterpriseESignConfigDao;

    @Value("${esign.host}")
    private String host;

    private TypeFactory typeFactory;

    @PostConstruct
    private void initTypeFactory() {
        typeFactory = TypeFactory.defaultInstance();
    }

    /**
     * post请求
     *
     * @param beans
     * @param requestApi
     * @return
     */
    public <T> T post(Object beans, String requestApi, Class<T> resultClass, Long enterpriseId) {
        return request(beans, requestApi, HttpMethod.POST, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    public <T> T post(Object beans, String requestApi, TypeReference<T> resultClass, Long enterpriseId) {
        return request(beans, requestApi, HttpMethod.POST, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    /**
     * put请求
     *
     * @param beans
     * @param requestApi
     * @return
     */
    public <T> T put(Object beans, String requestApi, Class<T> resultClass, Long enterpriseId) {
        return request(beans, requestApi, HttpMethod.PUT, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    public <T> T put(Object beans, String requestApi, TypeReference<T> resultClass, Long enterpriseId) {
        return request(beans, requestApi, HttpMethod.PUT, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    public void put(String requestApi, Long enterpriseId) {
        request(null, requestApi, HttpMethod.PUT, null, null,enterpriseId);
    }

    /**
     * get请求
     *
     * @param requestApi
     * @param resultClass
     * @param query
     * @param <T>
     * @param <Q>
     * @return
     */
    public <T, Q> T get(String requestApi, Class<T> resultClass, Q query, Long enterpriseId) {
        Map<String, String> queryParams = this.beanToMap(query, true);
        return request(null, requestApi, HttpMethod.GET, typeFactory.constructType(resultClass), queryParams,enterpriseId);
    }

    public <T> T get(String requestApi, Class<T> resultClass, Long enterpriseId) {
        return request(null, requestApi, HttpMethod.GET, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    public <T, Q> T get(String requestApi, TypeReference<T> resultClass, Q query, Long enterpriseId) {
        Map<String, String> queryParams = this.beanToMap(query, true);
        return request(null, requestApi, HttpMethod.GET, typeFactory.constructType(resultClass), queryParams,enterpriseId);
    }

    public <T> T get(String requestApi, TypeReference<T> resultClass, Long enterpriseId) {
        return request(null, requestApi, HttpMethod.GET, typeFactory.constructType(resultClass), null,enterpriseId);
    }

    /**
     * delete请求
     *
     * @param requestApi
     * @return
     */
    public void delete(String requestApi, Long enterpriseId) {
        request(null, requestApi, HttpMethod.DELETE, null, null,enterpriseId);
    }


    /**
     * 通用发送请求
     *
     * @param beans
     * @param requestApi
     * @param httpMethod
     * @return
     */
    private <T> T request(Object beans, String requestApi, HttpMethod httpMethod, JavaType resultClass, Map<String, String> queryParams, Long enterpriseId) {
        if (enterpriseId == null) {
            throw new BusinessException("e签宝接口请求异常，未知企业");
        }
        EnterpriseESignConfigEntity eSignConfig = enterpriseESignConfigDao.getByEnterpriseId(enterpriseId);
        if (eSignConfig == null || StringUtils.isBlank(eSignConfig.getAppId()) || StringUtils.isBlank(eSignConfig.getAppSecret())) {
            throw new BusinessException("e签宝接口请求异常，配置信息有误，请联系管理员设置后重试");
        }
        String appId = eSignConfig.getAppId();
        String appSecret = eSignConfig.getAppSecret();
        // 拼接接口地址
        String hostRequestApi = host + requestApi;
        if (queryParams != null) {
            // 按照字符ASC码顺序排序
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(hostRequestApi);
            queryParams.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> uriComponentsBuilder.queryParam(e.getKey(), e.getValue()));
            hostRequestApi = uriComponentsBuilder.toUriString();
            requestApi = hostRequestApi.replace(host, StringUtils.EMPTY);
        }
        // 计算请求体MD5值
        String requestBody = null;
        String contentMD5 = StringUtils.EMPTY;
        if (beans != null) {
            requestBody = JSONObject.toJSONString(beans);
            contentMD5 = this.getContentMD5(requestBody);
        }
        // 计算签名 获取headers
        HttpHeaders headers = this.getHttpHeaders(contentMD5, requestApi, httpMethod, appId, appSecret);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        log.info("发起e签宝请求（" + httpMethod.name() + "）：" + hostRequestApi + "；请求体：" + (beans == null ? "无" : requestBody));
        ResponseEntity<ESignResponseDTO> responseEntity = restTemplate.exchange(hostRequestApi, httpMethod, httpEntity, ESignResponseDTO.class);
        ESignResponseDTO responseDTO = responseEntity.getBody();
        log.info("e签宝请求返回（" + httpMethod.name() + "）：" + hostRequestApi + "；响应体：" + (responseDTO == null ? "无" : JSONObject.toJSONString(responseDTO)));
        ESignRequestRecordEntity eSignRequestRecordEntity = new ESignRequestRecordEntity();
        eSignRequestRecordEntity.setUrl(hostRequestApi);
        eSignRequestRecordEntity.setHttpMethod(httpMethod.name());
        eSignRequestRecordEntity.setParams(requestBody);
        eSignRequestRecordEntity.setHeaders(JSON.toJSONString(headers.entrySet()));
        eSignRequestRecordEntity.setResponseBody(JSON.toJSONString(responseDTO));
        this.saveRecordEntity(eSignRequestRecordEntity);
        if (responseDTO == null) {
            throw new BusinessException("e签宝接口请求异常");
        }
        // 判断是否成功
        if (responseDTO.getCode() != 0) {
            throw new BusinessException("e签宝接口请求失败：" + responseDTO.getMessage());
        }
        Object data = responseDTO.getData();
        if (resultClass == null || data == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(data, resultClass);
    }

    /**
     * 文件上传
     *
     * @param file
     * @param requestApi
     * @param contentMd5
     * @return
     */
    public ESignUploadResponseDTO upload(File file, String requestApi, String contentMd5) {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new BusinessException("e签宝-文件上传异常：文件不存在");
        }
        byte[] bytesArray = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fis.close();
        } catch (IOException e) {
            throw new BusinessException("e签宝-文件上传异常", e);
        }
        log.info("发起e签宝文件上传请求");
        okhttp3.MediaType parse = okhttp3.MediaType.parse(MediaType.APPLICATION_PDF_VALUE);
        RequestBody body = RequestBody.create(parse, bytesArray);
        OkHttpClient okHttpClient = restTemplateConfig.httpClientBuilder();
        Request request = new Request.Builder().url(requestApi)
                .method("PUT", body)
                .addHeader("Content-MD5", contentMd5)
                .addHeader("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                .build();
        Response execute;
        try {
            execute = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException("e签宝-文件上传接口请求异常,", e);
        }
        ResponseBody responseBody = execute.body();
        if (responseBody == null) {
            throw new BusinessException("e签宝-文件上传接口请求异常,responseBody为空");
        }
        String response;
        try {
            response = responseBody.string();
        } catch (IOException e) {
            throw new BusinessException("e签宝-文件上传接口请求响应异常,", e);
        }
        log.info("e签宝文件上传请求返回；响应体：" + response);
        ESignRequestRecordEntity eSignRequestRecordEntity = new ESignRequestRecordEntity();
        eSignRequestRecordEntity.setUrl(requestApi);
        eSignRequestRecordEntity.setHttpMethod("PUT");
        eSignRequestRecordEntity.setParams(null);
        eSignRequestRecordEntity.setHeaders(null);
        eSignRequestRecordEntity.setResponseBody(response);
        this.saveRecordEntity(eSignRequestRecordEntity);
        ESignUploadResponseDTO responseDTO = JSON.parseObject(response, ESignUploadResponseDTO.class);
        // 判断是否成功
        if (responseDTO.getErrCode() != 0) {
            throw new BusinessException("e签宝-文件上传接口请求失败：" + responseDTO.getMsg());
        }
        return responseDTO;
    }

    /**
     * 计算签名 获取headers
     *
     * @param contentMD5
     * @param requestApi
     * @param httpMethod
     * @return
     */
    private HttpHeaders getHttpHeaders(String contentMD5, String requestApi, HttpMethod httpMethod, String appId, String appSecret) {
        // 构建待签名字符串
        String method = httpMethod.toString();
        String accept = "*/*";
        String contentType = "application/json; charset=UTF-8";
        String date = "";

        StringBuffer sb = new StringBuffer();
        sb.append(method).append("\n").append(accept).append("\n").append(contentMD5).append("\n")
                .append(contentType).append("\n").append(date).append("\n").append(requestApi);
        // 计算请求签名值
        String reqSignature = this.doSignatureBase64(sb.toString(), appSecret);
        // 获取时间戳
        long timeStamp = System.currentTimeMillis();
        // 获得header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Tsign-Open-App-Id", appId);
        headers.add("X-Tsign-Open-Auth-Mode", "Signature");
        headers.add("X-Tsign-Open-Ca-Timestamp", String.valueOf(timeStamp));
        headers.add("Accept", accept);
        headers.add("Content-Type", contentType);
        headers.add("X-Tsign-Open-Ca-Signature", reqSignature);
        headers.add("Content-MD5", contentMD5);
        return headers;
    }

    /**
     * 计算请求体MD5值
     *
     * @param requestBody
     * @return
     */
    private String getContentMD5(String requestBody) {
        byte[] md5Bytes;
        MessageDigest md5;
        String contentMD5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md5.update(requestBody.getBytes(StandardCharsets.UTF_8));
            // 获取文件MD5的二进制数组（128位）
            md5Bytes = md5.digest();
            // 把MD5摘要后的二进制数组md5Bytes使用Base64进行编码（而不是对32位的16进制字符串进行编码）
            contentMD5 = new String(Base64.encodeBase64(md5Bytes), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            String msg = MessageFormat.format("不支持此算法: {0}", e.getMessage());
            throw new BusinessException(msg);
        }
        return contentMD5;
    }

    /***
     * 计算请求签名值
     *
     * @param message 待计算的消息
     * @return HmacSHA256计算后摘要值的Base64编码
     * @throws Exception 加密过程中的异常信息
     */
    private String doSignatureBase64(String message, String appSecret) {
        // 密钥
        String algorithm = "HmacSHA256";
        Mac hmacSha256;
        String digestBase64;
        try {
            hmacSha256 = Mac.getInstance(algorithm);
            byte[] keyBytes = appSecret.getBytes(StandardCharsets.UTF_8);
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm));
            // 使用HmacSHA256对二进制数据消息Bytes计算摘要
            byte[] digestBytes = hmacSha256.doFinal(messageBytes);
            // 把摘要后的结果digestBytes使用Base64进行编码
            digestBase64 = new String(Base64.encodeBase64(digestBytes), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            String msg = MessageFormat.format("不支持此算法: {0}", e.getMessage());
            log.error(msg);
            throw new BusinessException(msg, e);
        } catch (InvalidKeyException e) {
            String msg = MessageFormat.format("无效的密钥规范: {0}", e.getMessage());
            log.error(msg);
            throw new BusinessException(msg, e);
        }
        return digestBase64;
    }

    /***
     * 获取文件MD5-二进制数组（128位）
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String getFileMD5Bytes1282(File file) {
        FileInputStream fis = null;
        byte[] md5Bytes = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md5.update(buffer, 0, length);
            }
            md5Bytes = md5.digest();
            fis.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new BusinessException("e签宝-获取文件MD5-二进制数组（128位）异常", e);
        }
        if (md5Bytes == null) {
            return null;
        }
        // 对文件MD5的二进制数组进行base64编码
        return new String(Base64.encodeBase64(md5Bytes));
    }

    /**
     * bean转Map
     *
     * @param beans
     * @return
     */
    public Map<String, String> beanToMap(Object beans, boolean filterNullValues) {
        Map<String, String> requestParams = new HashMap<>();
        BeanMap beanMap = BeanMap.create(beans);
        for (Object o : beanMap.entrySet()) {
            if (o instanceof Map.Entry) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;
                String key = entry.getKey();
                Object value = entry.getValue();
                if (filterNullValues && value == null) {
                    continue;
                }
                requestParams.put(key, String.valueOf(value));
            }
        }
        return requestParams;
    }

    /**
     * 异步保存操作记录 避免被业务回滚
     *
     * @param eSignRequestRecordEntity
     */
    public void saveRecordEntity(ESignRequestRecordEntity eSignRequestRecordEntity) {
        eSignRequestRecordDao.insert(eSignRequestRecordEntity);
    }
}
