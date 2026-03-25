package net.lab1024.tms.common.module.business.esign;

/**
 * e签宝接口
 *
 * @author lihaifan
 * @date 2022/7/14 15:39
 */
public class ESignApiConst {

    // 合同文件上传
    public static String GET_UPLOAD_URL = "/v1/files/getUploadUrl";

    // 查询文件上传状态
    public static String GET_FILE_STATUS = "/v1/files/%s/status";

    // 查询PDF文件详情
    public static String GET_FILE_DETAIL = "/v1/files/%s";

    // 一步发起签署
    public static String CREATE_FLOW_ONE_STEP = "/api/v3/signflows/createFlowOneStep";

    // 搜索关键字坐标
    public static String SEARCH_WORDS_POSITION = "/v1/documents/%s/searchWordsPosition?keywords=%s";

    // 获取签署链接
    public static String EXECUTE_URL = "/v3/signflows/%s/executeUrl";

    // 下载已签文件
    public static String DOCUMENTS = "/v1/signflows/%s/documents";

    // 撤销签署流程
    public static String REVOKE = "/v1/signflows/%s/revoke";
}
