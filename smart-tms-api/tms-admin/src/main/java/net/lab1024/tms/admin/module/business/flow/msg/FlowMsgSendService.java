package net.lab1024.tms.admin.module.business.flow.msg;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yandy
 * @description:
 * @date 2022/8/3 6:01 下午
 */
@Service
public class FlowMsgSendService implements IFlowMsgSendService<String> {

    @Autowired
    private MsgCommonService msgCommonService;

    @Override
    public void sendMsg(List<Long> employeeIdList, FlowInstanceEntity flowInstanceEntity, String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
        MsgSubTypeEnum msgSubTypeEnum = this.getMsgSubType(flowTypeEnum);
        if (msgSubTypeEnum == null) {
            return;
        }
        Map<String, Object> contentParam = Maps.newHashMap();
        contentParam.put("msg", msg);
        List<MsgSendDTO> sendList = Lists.newArrayList();
        for (Long employeeId : employeeIdList) {
            MsgSendDTO msgSendDTO = new MsgSendDTO();
            msgSendDTO.setEnterpriseId(flowInstanceEntity.getEnterpriseId());
            msgSendDTO.setMsgSubType(msgSubTypeEnum);
            msgSendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            msgSendDTO.setReceiverId(employeeId);
            msgSendDTO.setDataId(flowInstanceEntity.getBusinessId());
            msgSendDTO.setContentParam(contentParam);
            sendList.add(msgSendDTO);
        }
        msgCommonService.send(sendList);
    }

    private MsgSubTypeEnum getMsgSubType(FlowTypeEnum flowTypeEnum) {
        MsgSubTypeEnum msgSubTypeEnum = null;
        switch (flowTypeEnum) {
            case WAY_BILL_AUDIT:
                msgSubTypeEnum = MsgSubTypeEnum.AUDIT_WAY_BILL_AUDIT;
                break;
            case PAY_AUDIT:
                msgSubTypeEnum = MsgSubTypeEnum.AUDIT_PAY_AUDIT;
                break;
            case RECEIVE_AUDIT:
                msgSubTypeEnum = MsgSubTypeEnum.AUDIT_RECEIVE_AUDIT;
                break;
            case OIL_CARD_AUDIT:
                msgSubTypeEnum = MsgSubTypeEnum.AUDIT_OIL_CARD_AUDIT;
                break;
        }
        return msgSubTypeEnum;
    }

}

