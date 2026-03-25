package net.lab1024.tms.admin.module.business.contract.esign;

import net.lab1024.tms.common.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 签名验证
 *
 * @author lihaifan
 * @date 2022/7/14 14:22
 */
@Service
public class ESignSafeVerify {


    /**
     * 校验验证是否通过
     * @param request
     * @return
     */
    public Boolean checkPass(HttpServletRequest request, String requestBody) {
        // 验证签名需要用到密钥 系统为多企业 过来的回调不知是哪个企业的 未知密钥 无法验证
        return Boolean.TRUE;
//        String signture = request.getHeader("X-Tsign-Open-SIGNATURE");
//        //1. 获取时间戳的字节流
//        String timestamp = request.getHeader("X-Tsign-Open-TIMESTAMP");
//        //2. 获取query请求字符串
//        String requestQuery = getRequestQueryStr(request);
//        //3. 获取body的数据
//        //4、按照规则进行加密
//        String signdata = timestamp + requestQuery + requestBody;
//        String mySignature = this.getSignature(signdata, eSignConfig.getAppKey(), "HmacSHA256", "UTF-8");
//        System.out.println("加密出来的签名值：----------->>>>>>" + mySignature);
//        System.out.println("header里面的签名值：---------->>>>>>" + signture);
//        return mySignature.equals(signture);

    }

    /**
     * 获取请求body
     *
     * @param request
     * @param encoding
     * @return
     */
    public String getRequestBody(HttpServletRequest request, String encoding) {
        // 请求内容RequestBody
        String reqBody = null;
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        try {
            for (int i = 0; i < contentLength; ) {
                int readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reqBody = new String(buffer, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reqBody;
    }

    /**
     * 获取query请求字符串
     *
     * @param request
     * @return
     */
    public String getRequestQueryStr(HttpServletRequest request) {
        //对 Query 参数按照字典对 Key 进行排序后,按照value1+value2方法拼接
        //转换一下数据类型并排序
        List<String> req_List = new ArrayList();
        Enumeration<String> reqEnu = request.getParameterNames();
        while (reqEnu.hasMoreElements()) {
            req_List.add(reqEnu.nextElement());
        }
        Collections.sort(req_List);
        String requestQuery = "";
        for (String key : req_List) {
            String value = request.getParameter(key);
            requestQuery += value == null ? "" : value;
        }
        System.out.println("获取的query请求字符串是：------》》》" + requestQuery);
        return requestQuery;
    }

    /**
     * 签名计算
     *
     * @param data
     * @param key
     * @param algorithm
     * @param encoding
     * @return
     */
    public static String getSignature(String data, String key, String algorithm, String encoding) {
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(encoding), algorithm);
            mac.init(secretKey);
            mac.update(data.getBytes(encoding));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new BusinessException("获取Signature签名信息异常：" + e.getMessage());
        }
        return byte2hex(mac.doFinal());
    }

    /***
     * 将byte[]转成16进制字符串
     *
     * @param data
     *
     * @return 16进制字符串
     */
    public static String byte2hex(byte[] data) {
        StringBuilder hash = new StringBuilder();
        String stmp;
        for (int n = 0; data != null && n < data.length; n++) {
            stmp = Integer.toHexString(data[n] & 0XFF);
            if (stmp.length() == 1)
                hash.append('0');
            hash.append(stmp);
        }
        return hash.toString();
    }

}
