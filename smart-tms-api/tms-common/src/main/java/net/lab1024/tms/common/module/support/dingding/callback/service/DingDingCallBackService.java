package net.lab1024.tms.common.module.support.dingding.callback.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.support.dingding.DingCallbackCrypto;
import net.lab1024.tms.common.module.support.dingding.callback.dao.DingDingEventRecordDao;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventDeptDTO;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventUserDTO;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigCacheService;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import net.lab1024.tms.common.module.support.dingding.callback.domain.entity.DingDingEventRecordEntity;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 钉钉操作
 *
 * @author lidoudou
 * @date 2023/4/20 下午4:31
 */
@Slf4j
@Service
public class DingDingCallBackService {

    @Autowired
    private DingDingEventRecordDao dingDingEventRecordDao;
    @Autowired
    private DingDingConfigCacheService dingDingConfigCacheService;
    @Autowired
    private DingDingCallBackFactory dingDingCallBackFactory;
    /**
     * @param enterpriseId 所属企业
     * @param msgSignature 签名
     * @param timeStamp    时间戳
     * @param nonce        随机字符串
     * @param json         内含加密串
     */
    public Map<String, String> callback(Long enterpriseId, String msgSignature, String timeStamp, String nonce, JSONObject json) {
        String recordRemark = "";
        try {
            DingDingConfigEntity dingDingConfigEntity = dingDingConfigCacheService.getConfig(enterpriseId);
            if (dingDingConfigEntity == null) {
                recordRemark = recordRemark + "企业钉钉应用配置信息不存在;";
            }
            // 解密
            DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(dingDingConfigEntity.getAesToken(), dingDingConfigEntity.getAesKey(), dingDingConfigEntity.getAppKey());
            String encryptMsg = json.getString("encrypt");
            String decryptMsg = callbackCrypto.getDecryptMsg(msgSignature, timeStamp, nonce, encryptMsg);
            JSONObject eventJson = JSON.parseObject(decryptMsg);
            // 获取事件类型
            String eventType = eventJson.getString("EventType");
            DingDingEventTypeEnum eventTypeEnum = SmartBaseEnumUtil.getEnumByValue(eventType, DingDingEventTypeEnum.class);
            IDingCallBackHandleService dingCallBackHandleService = dingDingCallBackFactory.getHandleService(eventTypeEnum);
            if (dingCallBackHandleService == null) {
                recordRemark = recordRemark + "暂未找到与之对应的事件处理方法："+eventType;
            }else {
                recordRemark = JSONObject.toJSONString(eventJson);
                // 事件处理
                this.eventHandle(enterpriseId, eventJson, dingCallBackHandleService);
            }
            Map<String, String> successMap = callbackCrypto.getEncryptedMap("success");
            return successMap;
        }catch (Exception e) {
            recordRemark = recordRemark + e.getMessage();

        }finally {
            DingDingEventRecordEntity dingDingEventRecordEntity = new DingDingEventRecordEntity();
            dingDingEventRecordEntity.setEnterpriseId(enterpriseId);
            dingDingEventRecordEntity.setMsgSignature(msgSignature);
            dingDingEventRecordEntity.setTimeStamp(timeStamp);
            dingDingEventRecordEntity.setNonce(nonce);
            dingDingEventRecordEntity.setRequestBody(JSONObject.toJSONString(json));
            dingDingEventRecordEntity.setRemark(recordRemark);
            dingDingEventRecordDao.insert(dingDingEventRecordEntity);

        }
        return null;
    }

    private void eventHandle(Long enterpriseId, JSONObject eventJson, IDingCallBackHandleService dingCallBackHandleService){
        String eventType = eventJson.getString("EventType");
        DingDingEventTypeEnum eventTypeEnum = SmartBaseEnumUtil.getEnumByValue(eventType, DingDingEventTypeEnum.class);
        switch (eventTypeEnum) {
            case CHECK_URl:
                log.error("【钉钉】CHECK_URl");
                break;
            case USER_ADD_ORG:
            case USER_MODIFY_ORG:
            case USER_LEAVE_ORG:
                DingDingEventUserDTO dingEventUserDTO = eventJson.toJavaObject(DingDingEventUserDTO.class);
                dingCallBackHandleService.handle(enterpriseId, dingEventUserDTO);
                break;
            case ORG_DEPT_CREATE:
            case ORG_DEPT_MODIFY:
            case ORG_DEPT_REMOVE:
                DingDingEventDeptDTO dingEventDeptDTO = eventJson.toJavaObject(DingDingEventDeptDTO.class);
                dingCallBackHandleService.handle(enterpriseId, dingEventDeptDTO);
                break;
            default:
                log.error("【钉钉】位置的时间类型");
        }
    }


}
