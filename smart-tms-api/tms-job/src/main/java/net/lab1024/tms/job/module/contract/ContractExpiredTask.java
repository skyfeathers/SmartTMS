package net.lab1024.tms.job.module.contract;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.BusinessSettingEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseBusinessSettingEntity;
import net.lab1024.tms.job.module.oa.enterprise.EnterpriseBusinessSettingDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应收账期到期提醒
 *
 * @author lidoudou
 * @date 2022/10/24 下午4:02
 */
@Slf4j
@Service
public class ContractExpiredTask {

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private MsgCommonService msgCommonService;
    @Autowired
    private EnterpriseBusinessSettingDao enterpriseBusinessSettingDao;

    /**
     * 查询合同到期时间大于当天的，推送消息通知
     */
    @Scheduled(cron = "01 0 9 * * *")
    public void sendMsg() {
        Map<Long, List<Long>> enterpriseReceiverMap = getMsgReceiverId();
        if (enterpriseReceiverMap.isEmpty()) {
            return;
        }

        List<ContractEntity> list = contractDao.selectByExpireDate();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        /**
         * 循环 每次处理500条数据
         */
        List<List<ContractEntity>> partitions = Lists.partition(list, 500);
        for (List<ContractEntity> partition : partitions) {
            List<MsgSendDTO> msgSendList = Lists.newArrayList();
            for (ContractEntity contractEntity : partition) {
                // 发送提醒消息
                List<MsgSendDTO> msgList = this.buildMsg(contractEntity, enterpriseReceiverMap);
                if (CollectionUtils.isNotEmpty(msgList)) {
                    msgSendList.addAll(msgList);
                }
            }
            // 批量更新到期状态
            if (CollectionUtils.isNotEmpty(msgSendList)) {
                // 发送消息
                msgCommonService.send(msgSendList);
            }
        }
    }

    /**
     * 设置消息提醒
     *
     * @param contractEntity
     */
    private List<MsgSendDTO> buildMsg(ContractEntity contractEntity, Map<Long, List<Long>> enterpriseReceiverMap) {
        List<Long> receiverIdList = enterpriseReceiverMap.get(contractEntity.getEnterpriseId());
        List<MsgSendDTO> msgList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(receiverIdList)) {
            return msgList;
        }
        // build 通知消息
        for (Long receiverId : receiverIdList) {
            MsgSendDTO sendDTO = new MsgSendDTO();
            sendDTO.setEnterpriseId(contractEntity.getEnterpriseId());
            sendDTO.setMsgSubType(MsgSubTypeEnum.CONTRACT_EXPIRE);
            sendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            sendDTO.setReceiverId(receiverId);
            sendDTO.setDataId(contractEntity.getContractId());
            // 参数
            Map<String, Object> contentParam = new HashMap<>(3);
            contentParam.put("contractCode", contractEntity.getContractCode());
            sendDTO.setContentParam(contentParam);

            msgList.add(sendDTO);
        }
        return msgList;
    }

    /**
     * 根据企业查询接收人
     *
     * @return
     */
    private Map<Long, List<Long>> getMsgReceiverId() {
        List<EnterpriseBusinessSettingEntity> entityList = enterpriseBusinessSettingDao.selectByKey(BusinessSettingEnum.CONTRACT_MESSAGE_RECEIVER.getValue());
        if (CollectionUtils.isEmpty(entityList)) {
            return Maps.newHashMap();
        }

        Map<Long, List<Long>> enterpriseReceiverIdMap = Maps.newHashMap();
        for (EnterpriseBusinessSettingEntity item : entityList) {
            String settingValue = item.getSettingValue();
            List<Long> receiverIdList = SmartStringUtil.splitConverToLongList(settingValue, ",");
            if (CollectionUtils.isNotEmpty(receiverIdList)) {
                enterpriseReceiverIdMap.put(item.getEnterpriseId(), receiverIdList);
            }
        }
        return enterpriseReceiverIdMap;
    }
}
