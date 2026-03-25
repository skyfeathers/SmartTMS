package net.lab1024.tms.admin.module.business.contract.esign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.lab1024.tms.admin.module.business.contract.basic.ContractDao;
import net.lab1024.tms.common.common.code.ErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.esign.constant.ESignFlowStatusEnum;
import net.lab1024.tms.common.module.business.esign.constant.ESignNoticeActionEnum;
import net.lab1024.tms.common.module.business.esign.domain.entity.ESignNoticeRecordEntity;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * e签宝回调
 *
 * @author lihaifan
 * @date 2022/9/16 18:33
 */
@Service
public class ESignNoticeService {

    @Autowired
    private ESignNoticeRecordDao eSignNoticeRecordDao;
    @Autowired
    private ContractDao contractDao;

    /**
     * 成功返回code
     */
    private final Integer OK_CODE = 200;

    /**
     * 成功返回code
     */
    private final String OK_MSG = "success";

    /**
     * e签宝回调
     *
     * @param requestBody
     * @return
     */
    public ResponseDTO<String> notice(String requestBody) {
        // 保存回调记录
        JSONObject requestJSON = JSON.parseObject(requestBody);
        String action = requestJSON.getString("action");
        String flowId = requestJSON.getString("flowId");
        ESignNoticeActionEnum noticeActionEnum = SmartBaseEnumUtil.getEnumByValue(action, ESignNoticeActionEnum.class);
        String actionDesc = noticeActionEnum == null ? StringUtils.EMPTY : noticeActionEnum.getDesc();
        ESignNoticeRecordEntity eSignNoticeRecordEntity = new ESignNoticeRecordEntity();
        eSignNoticeRecordEntity.setAction(action);
        eSignNoticeRecordEntity.setActionDesc(actionDesc);
        eSignNoticeRecordEntity.setFlowId(flowId);
        eSignNoticeRecordEntity.setRequestBody(requestBody);
        // 成功返回值
        ResponseDTO<String> responseDTO = new ResponseDTO<>(OK_CODE, ErrorCode.LEVEL_SYSTEM, Boolean.TRUE, OK_MSG, null);
        // 没有找到action 则返回成功
        if (noticeActionEnum == null) {
            this.saveRecordEntity(eSignNoticeRecordEntity);
            return responseDTO;
        }
        // 通过不同的action判断回调内容
        switch (noticeActionEnum) {
            case SIGN_FLOW_UPDATE:
                //签署人签署完成 更新事件描述
                String resultDescription = requestJSON.getString("resultDescription");
                actionDesc += "-" + resultDescription;
                break;
            case SIGN_FLOW_FINISH:
                //流程结束 更新事件描述
                String statusDescription = requestJSON.getString("statusDescription");
                actionDesc += "-" + statusDescription;
                // 更新合同签署状态
                Integer signResult = requestJSON.getInteger("flowStatus");
                ESignFlowStatusEnum flowStatusEnum = SmartBaseEnumUtil.getEnumByValue(signResult, ESignFlowStatusEnum.class);
                ContractStatusEnum contractStatusEnum = ContractStatusEnum.CANCEL;
                if(flowStatusEnum != null && ESignFlowStatusEnum.SUCCESS.equals(flowStatusEnum)){
                    contractStatusEnum = ContractStatusEnum.SIGNED;
                }
                ContractEntity contractEntity = contractDao.selectByOnlineId(flowId);
                if(contractEntity == null){
                    break;
                }
                ContractEntity updateEntity = new ContractEntity();
                updateEntity.setContractId(contractEntity.getContractId());
                updateEntity.setStatus(contractStatusEnum.getValue());
                contractDao.updateById(updateEntity);
                break;
            case SIGN_DOC_EXPIRE_REMIND:
                //流程文件过期前提醒
                break;
            case SIGN_DOC_EXPIRE:
                //流程文件过期
                break;
            case BATCH_ADD_WATERMARK_REMIND:
                //文件添加数字水印完成
                break;
            case PROCESS_HANDOVER:
                //经办人转交签署任务
                break;
            case WILL_FINISH:
                //意愿认证完成
                Boolean success = requestJSON.getBoolean("success");
                actionDesc += "-" + (BooleanUtils.isTrue(success) ? "成功" : "失败");
                break;
            case PARTICIPANT_MARKREAD:
                //签署人已读
                break;
            default:
                break;
        }
        eSignNoticeRecordEntity.setActionDesc(actionDesc);
        this.saveRecordEntity(eSignNoticeRecordEntity);
        return responseDTO;
    }


    /**
     * 保存操作记录
     *
     * @param eSignNoticeRecordEntity
     */
    public void saveRecordEntity(ESignNoticeRecordEntity eSignNoticeRecordEntity) {
        eSignNoticeRecordDao.insert(eSignNoticeRecordEntity);
    }
}
