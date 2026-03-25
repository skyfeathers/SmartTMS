package net.lab1024.tms.common.module.support.sms.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartValidatorBeanUtil;
import net.lab1024.tms.common.config.AsyncConfig;
import net.lab1024.tms.common.module.support.sms.SmsRecordDao;
import net.lab1024.tms.common.module.support.sms.constant.SmsNotifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsTypeEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.domain.SmsRecordAddDTO;
import net.lab1024.tms.common.module.support.sms.domain.SmsRecordBatchAddDTO;
import net.lab1024.tms.common.module.support.sms.domain.SmsRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信记录 业务
 *
 * @author Turbolisten
 * @date 2020/3/16 16:59
 */
@Slf4j
@Service
public class SmsRecordService {

    @Autowired
    private SmsRecordDao smsRecordDao;
    @Autowired
    private SmsRecordManager smsRecordManager;

    /**
     * 保存短信发送记录
     *
     * @param addDTO
     * @return
     */
    @Async(AsyncConfig.ASYNC_EXECUTOR_THREAD_NAME)
    public ResponseDTO<String> addRecord(SmsRecordAddDTO addDTO) {

        // 校验参数
        String result = SmartValidatorBeanUtil.validateModel(addDTO);
        if (StringUtils.isNotBlank(result)) {
            log.error("保存短信发送记录-参数错误:{},原始数据:{}", result, addDTO);
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, result);
        }

        /**
         * 根据短信类型 获取子类型
         */
        SmsTypeEnum smsType = addDTO.getSmsType();
        Integer subType = addDTO.getSmsSubType();
        String subTypeDesc = null;
        switch (smsType) {
            case VERIFICATION_CODE:
                subTypeDesc = SmartBaseEnumUtil.getEnumDescByValue(subType, SmsVerifyTypeEnum.class);
                break;
            case NOTIFY:
                subTypeDesc = SmartBaseEnumUtil.getEnumDescByValue(subType, SmsNotifyTypeEnum.class);
                break;
            default:
                throw new BusinessException("未处理的短信类型");
        }
        if (StringUtils.isBlank(subTypeDesc)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, result);
        }
        SmsRecordEntity recordEntity = SmartBeanUtil.copy(addDTO, SmsRecordEntity.class);
        recordEntity.setSmsType(smsType.getValue());
        recordEntity.setSmsTypeDesc(smsType.getDesc());
        recordEntity.setSmsSubTypeDesc(subTypeDesc);
        smsRecordDao.insert(recordEntity);

        return ResponseDTO.ok();
    }

    /**
     * 批量添加操作记录
     * @param batchAddDTO
     * @return
     */
    public ResponseDTO<String> batchAddRecord(SmsRecordBatchAddDTO batchAddDTO) {
        // 校验参数
        String result = SmartValidatorBeanUtil.validateModel(batchAddDTO);
        if (StringUtils.isNotBlank(result)) {
            log.error("保存短信发送记录-参数错误:{},原始数据:{}", result, batchAddDTO);
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, result);
        }

        /**
         * 根据短信类型 获取子类型
         */
        SmsTypeEnum smsType = batchAddDTO.getSmsType();
        Integer subType = batchAddDTO.getSmsSubType();
        String subTypeDesc = null;
        switch (smsType) {
            case VERIFICATION_CODE:
                subTypeDesc = SmartBaseEnumUtil.getEnumDescByValue(subType, SmsVerifyTypeEnum.class);
                break;
            case NOTIFY:
                subTypeDesc = SmartBaseEnumUtil.getEnumDescByValue(subType, SmsNotifyTypeEnum.class);
                break;
            default:
                throw new BusinessException("未处理的短信类型");
        }
        if (StringUtils.isBlank(subTypeDesc)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, result);
        }

        SmsRecordEntity recordEntity = SmartBeanUtil.copy(batchAddDTO, SmsRecordEntity.class);
        recordEntity.setSmsType(smsType.getValue());
        recordEntity.setSmsTypeDesc(smsType.getDesc());
        recordEntity.setSmsSubTypeDesc(subTypeDesc);

        List<String> phoneList = batchAddDTO.getPhoneList();
        List<SmsRecordEntity> insertEntities = phoneList.stream().map(e -> {
            SmsRecordEntity insertEntity = SmartBeanUtil.copy(recordEntity, SmsRecordEntity.class);
            insertEntity.setPhone(e);
            return insertEntity;
        }).collect(Collectors.toList());
        smsRecordManager.saveBatch(insertEntities);

        return ResponseDTO.ok();
    }
}
