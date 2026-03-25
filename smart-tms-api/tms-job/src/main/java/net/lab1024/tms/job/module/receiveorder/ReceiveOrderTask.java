package net.lab1024.tms.job.module.receiveorder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.BusinessSettingEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseBusinessSettingEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.job.module.oa.enterprise.EnterpriseBusinessSettingDao;
import net.lab1024.tms.job.module.receiveorder.domain.ShipperSimpleDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 应收账款
 *
 * @author lidoudou
 * @date 2022/10/24 下午4:02
 */
@Slf4j
@Service
public class ReceiveOrderTask {

    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private MsgCommonService msgCommonService;
    @Autowired
    private EnterpriseBusinessSettingDao enterpriseBusinessSettingDao;

    /**
     * 推送今天到期的营收的消息
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendMsg() {

        List<ReceiveOrderEntity> list = receiveOrderDao.selectByAccountPeriodDate(Boolean.FALSE);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> shipperIdList = list.stream().map(ReceiveOrderEntity::getShipperId).collect(Collectors.toSet());
        List<ShipperSimpleDTO> shipperList = receiveOrderDao.selectByShipperIdList(shipperIdList);
        Map<Long, ShipperSimpleDTO> shipperMap = shipperList.stream().collect(Collectors.toMap(ShipperSimpleDTO::getShipperId, Function.identity()));

        Map<Long, List<Long>> enterpriseReceiverMap = getMsgReceiverId();
        /**
         * 循环 每次处理500条数据
         */
        List<List<ReceiveOrderEntity>> partitions = Lists.partition(list, 500);
        for (List<ReceiveOrderEntity> partition : partitions) {
            List<MsgSendDTO> msgSendList = Lists.newArrayList();
            for (ReceiveOrderEntity item : partition) {
                if (!shipperMap.containsKey(item.getShipperId())) {
                    continue;
                }

                // 发送提醒消息
                ShipperSimpleDTO shipperSimpleDTO = shipperMap.get(item.getShipperId());
                // 发送提醒消息
                List<MsgSendDTO> msgList = this.buildMsg(item, enterpriseReceiverMap.get(item.getEnterpriseId()), shipperSimpleDTO.getShortName());
                if (CollectionUtils.isNotEmpty(msgList)) {
                    msgSendList.addAll(msgList);
                }
                if (null != shipperSimpleDTO.getManagerId()) {
                    List<MsgSendDTO> managerIdList = this.buildMsg(item, Arrays.asList(shipperSimpleDTO.getManagerId()), shipperSimpleDTO.getShortName());
                    if (CollectionUtils.isNotEmpty(managerIdList)) {
                        msgSendList.addAll(managerIdList);
                    }
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
     * @param receiveOrderEntity
     */
    private List<MsgSendDTO> buildMsg(ReceiveOrderEntity receiveOrderEntity, List<Long> receiverIdList, String shortName) {
        // build 通知消息
        List<MsgSendDTO> msgList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(receiverIdList)) {
            return msgList;
        }
        for (Long receiverId : receiverIdList) {
            MsgSendDTO sendDTO = new MsgSendDTO();
            sendDTO.setMsgSubType(MsgSubTypeEnum.RECEIVE_ORDER_ACCOUNT_PERIOD_DATE);
            sendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
            sendDTO.setReceiverId(receiverId);
            sendDTO.setEnterpriseId(receiveOrderEntity.getEnterpriseId());
            sendDTO.setDataId(receiveOrderEntity.getReceiveOrderId());
            // 参数
            Map<String, Object> contentParam = new HashMap<>(3);
            contentParam.put("shortName", shortName);
            contentParam.put("receiveOrderNumber", receiveOrderEntity.getReceiveOrderNumber());
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
        List<EnterpriseBusinessSettingEntity> entityList = enterpriseBusinessSettingDao.selectByKey(BusinessSettingEnum.RECEIVE_ORDER_MESSAGE_RECEIVER.getValue());
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
