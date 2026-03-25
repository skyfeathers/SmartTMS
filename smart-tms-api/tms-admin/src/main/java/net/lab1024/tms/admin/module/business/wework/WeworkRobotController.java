package net.lab1024.tms.admin.module.business.wework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.wework.bot.WXBizJsonMsgCrypt;
import net.lab1024.tms.admin.module.business.wework.bot.WeWorkConfig;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * @author yandy
 * @description:
 * @date 2025/12/25 6:00 下午
 */
@Slf4j
@RestController
@RequestMapping("/wework/robot/receive")
public class WeworkRobotController {

    public static void main(String[] args) {
        String str = Base64.encodeBase64String(UUID.randomUUID().toString().replaceAll("-","").getBytes());
        System.out.println(str);
    }

    /**
     * 第一步：回调URL验证（GET请求，仍需解密echostr）
     */
    @NoNeedLogin
    @GetMapping
    public String verifyCallback(
            @RequestParam("msg_signature") String msgSignature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {
        try {
            log.info("智能机器人回调验证：msg_signature={}, timestamp={}, nonce={}, echostr={}",
                    msgSignature, timestamp, nonce, echostr);

            // 1. 初始化加解密工具
            WXBizJsonMsgCrypt cryptoUtil = new WXBizJsonMsgCrypt(
                    WeWorkConfig.TOKEN,
                    WeWorkConfig.ENCODING_AES_KEY,
                   ""
            );

            String plainEchoStr = cryptoUtil.VerifyURL(msgSignature, timestamp, nonce, echostr);
            log.info("智能机器人回调验证：解密后的echostr={}", plainEchoStr);
            // 4. 返回解密后的明文，完成验证
            return plainEchoStr;
        } catch (Exception e) {
            log.error("智能机器人回调验证失败", e);
            return "error";
        }
    }

    /**
     * 第二步：接收智能机器人JSON回调消息+被动回复（POST请求）
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String receiveAndReply(HttpServletRequest request, @RequestParam("msg_signature") String msgSignature,
                                  @RequestParam("timestamp") String timestamp,
                                  @RequestParam("nonce") String nonce) {
        try {
            // 1. 读取JSON请求体（智能机器人推送原生JSON，无需解密）
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder jsonSb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonSb.append(line);
            }

            WXBizJsonMsgCrypt cryptoUtil = new WXBizJsonMsgCrypt(
                    WeWorkConfig.TOKEN,
                    WeWorkConfig.ENCODING_AES_KEY,
                    ""
            );
            String requestJson = jsonSb.toString();
            log.info("智能机器人回调requestJson：{}", requestJson);
            String aaa = cryptoUtil.DecryptMsg(msgSignature, timestamp, nonce, requestJson);
            log.info("智能机器人回调JSON：{}", aaa);
            JSONObject aaaObj = JSONObject.parseObject(aaa);

            JSONObject replyJson = new JSONObject();
            replyJson.put("msgtype", "stream");
            JSONObject streamJson = new JSONObject();
            streamJson.put("id", UUID.randomUUID().toString());
            streamJson.put("finish", true);
            streamJson.put("content", aaa);

            replyJson.put("stream", streamJson);
            String replyJsonStr = replyJson.toJSONString();
            log.info("待加密的回复JSON：{}", replyJsonStr);
            // 步骤6：生成加密的回复XML（文档101033规范）
            String replyMsg = cryptoUtil.EncryptMsg(replyJsonStr, timestamp, nonce);
            log.info("加密的回复JSON：{}", replyMsg);
            return replyMsg;

        } catch (Exception e) {
            log.error("处理智能机器人消息失败", e);
            return buildErrorJson(-1, "处理失败：" + e.getMessage());
        }
    }


    /**
     * 构造错误回复JSON
     */
    private String buildErrorJson(int errCode, String errMsg) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("errcode", errCode);
        errorJson.put("errmsg", errMsg);
        return errorJson.toJSONString();
    }
}