package net.lab1024.tms.common.module.support.sms.ali;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import net.lab1024.tms.common.common.code.SystemErrorCode;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.config.AliSmsConfig;
import net.lab1024.tms.common.module.support.sms.constant.SmsNotifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.domain.SmsSendResultDTO;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云短信 api
 *
 * @author Turbolisten
 * @date 2020/02/20 15:57
 */
@Service
public class AliSmsService {

    @Autowired
    private IAcsClient client;

    @Autowired
    private AliSmsConfig aliSmsConfig;


    /**
     * 调用成功 code
     */
    public static final String SUCCESS_CODE = "OK";

    /**
     * 短信发送失败 status
     */
    public static final Integer SEND_FAILED_STATUS = 2;

    /**
     * 发送验证码短信
     *
     * @param smsTypeEnum
     * @param phone
     * @param code
     * @return 发送成功返回 短信记录id
     */
    public ResponseDTO<String> sendVerifyCode(SmsVerifyTypeEnum smsTypeEnum, String phone, String code) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliSmsConfig.getApiUrl());
        // sys version 固定参数
        request.setSysVersion("2017-05-25");
        // action 固定参数 SendSms
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", aliSmsConfig.getSignName());
        // 根据短信类型 获取模版
        String templateCode = aliSmsConfig.getTemplateCode(smsTypeEnum.getCodeKey());
        if (StringUtils.isBlank(templateCode)) {
            return ResponseDTO.userErrorParam("短信模板不存在-" + smsTypeEnum.getDesc());
        }
        request.putQueryParameter("TemplateCode", templateCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        request.putQueryParameter("TemplateParam", jsonObject.toJSONString());
        // 发送请求
        ResponseDTO<JSONObject> responseDTO = this.sendRequest(request);
        if (!responseDTO.getOk()) {
            return ResponseDTO.error(responseDTO);
        }

        // 获取短信记录id
        String bizId = responseDTO.getData().getString("BizId");
        return ResponseDTO.ok(bizId);
    }


    /**
     * 发送消息短信
     *
     * @param smsNotifyType 模板代码
     * @param phoneNumbers  手机号码
     * @param paramMap         模板变量参数
     * @return result
     */
    public ResponseDTO<String> sendMessageNotify(SmsNotifyTypeEnum smsNotifyType, List<String> phoneNumbers, Map<String, Object> paramMap) {

        // 根据短信类型 获取短信模版
        String templateCode = aliSmsConfig.getTemplateCode(smsNotifyType.getCodeKey());
        if (!StringUtils.isBlank(templateCode)) {
            return ResponseDTO.userErrorParam("短信模板不存在");
        }
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliSmsConfig.getApiUrl());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", aliSmsConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", String.join(",", phoneNumbers));
        request.putQueryParameter("SignName", aliSmsConfig.getSignName());
        request.putQueryParameter("TemplateCode",templateCode);

        // 填充模板变量；如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为其替换值
        if (MapUtils.isNotEmpty(paramMap)) {
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(paramMap));
        }

        // 发送请求
        ResponseDTO<JSONObject> responseDTO = this.sendRequest(request);
        if (!responseDTO.getOk()) {
            return ResponseDTO.error(responseDTO);
        }
        // 获取短信记录id
        String bizId = responseDTO.getData().getString("BizId");
        return ResponseDTO.ok(bizId);
    }

    /**
     * 查询短信发送详情
     * 目前为了简便 只能查询当天的记录 第一页 第一条  以及 服务商的短信记录id查询
     *
     * @param phone
     * @param bizId
     * @return
     */
    public ResponseDTO<SmsSendResultDTO> querySendDetail(String phone, String bizId) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliSmsConfig.getApiUrl());
        request.setSysVersion("2017-05-25");
        request.setSysAction("QuerySendDetails");
        request.putQueryParameter("RegionId", aliSmsConfig.getRegionId());
        request.putQueryParameter("PhoneNumber", phone);
        /**
         * 查询时间 分页数量 当前页 参数必填
         * 暂时 默认当天 1页
         */
        request.putQueryParameter("SendDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        request.putQueryParameter("PageSize", "1");
        request.putQueryParameter("CurrentPage", "1");
        request.putQueryParameter("BizId", bizId);
        // 发送请求
        ResponseDTO<JSONObject> responseDTO = this.sendRequest(request);
        if (!responseDTO.getOk()) {
            return ResponseDTO.error(responseDTO);
        }
        JSONArray jsonArray = responseDTO.getData().getJSONObject("SmsSendDetailDTOs").getJSONArray("SmsSendDetailDTO");
        if (jsonArray.isEmpty()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 默认只获取 第一条记录
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        // 返回 短信发送结果
        String sendDate = jsonObject.getString("SendDate");
        String receiveDate = jsonObject.getString("ReceiveDate");
        // 1等待回执 2失败 3成功 目前只要不失败 就当作成功
        Integer sendStatus = jsonObject.getInteger("SendStatus");
        boolean successFlag = !Objects.equals(sendStatus, SEND_FAILED_STATUS);

        SmsSendResultDTO resultDTO = new SmsSendResultDTO();
        resultDTO.setContent(jsonObject.getString("Content"));
        resultDTO.setPhone(jsonObject.getString("PhoneNum"));
        resultDTO.setSendTime(SmartLocalDateUtil.parseYMDHMS(sendDate));
        resultDTO.setReceiveTime(SmartLocalDateUtil.parseYMDHMS(receiveDate));
        resultDTO.setErrorMsg(jsonObject.getString("ErrCode"));
        resultDTO.setSuccessFlag(successFlag);
        return ResponseDTO.ok(resultDTO);
    }


    /**
     * 公用的发送短信请求方法
     *
     * @param request
     * @return
     */
    private ResponseDTO<JSONObject> sendRequest(CommonRequest request) {
        JSONObject resultJSON;
        try {
            CommonResponse response = client.getCommonResponse(request);
            resultJSON = JSONObject.parseObject(response.getData());
            String resultCode = resultJSON.getString("Code");
            if (!Objects.equals(resultCode, SUCCESS_CODE)) {
                // 返回错误信息
                String errMsg = resultJSON.getString("Message");
                return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, resultCode + "-" + errMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        return ResponseDTO.ok(resultJSON);
    }


}
