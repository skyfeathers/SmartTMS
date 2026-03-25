package net.lab1024.tms.common.module.support.ocr.constant;

/**
 * @author yandy
 * @description:
 * @date 2022/12/3 2:17 下午
 */
public class OcrBaiduConstant {

    /**
     * tokenUrl
     */
    public static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={1}&client_secret={2}";
    /**
     * 道路运输证
     */
    public static final String ROAD_TRANSPORT_CERTIFICATE = "https://aip.baidubce.com/rest/2.0/ocr/v1/road_transport_certificate?access_token={1}";

    public static final String ROAD_TRANSPORT_CERTIFICATE_KEYS = "";
}