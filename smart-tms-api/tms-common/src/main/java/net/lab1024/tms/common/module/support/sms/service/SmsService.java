package net.lab1024.tms.common.module.support.sms.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.SystemEnvironment;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.common.util.SmartRandomUtil;
import net.lab1024.tms.common.common.util.SmartVerificationUtil;
import net.lab1024.tms.common.config.AsyncConfig;
import net.lab1024.tms.common.constant.RedisKeyConst;
import net.lab1024.tms.common.module.support.redis.RedisService;
import net.lab1024.tms.common.module.support.sms.ali.AliSmsService;
import net.lab1024.tms.common.module.support.sms.constant.SmsNotifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsTypeEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.domain.*;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 短信业务
 *
 * @author listen
 * @date 2020/02/20 20:06
 */
@Slf4j
@Service
public class SmsService {

    @Autowired
    private AliSmsService aliSmsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemEnvironment systemEnvironment;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private SmsRecordService smsRecordService;

    /**
     * 支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     */
    private final int MAX_BATCH_SIZE = 1000;

    /**
     * 发送通知类短信
     *
     * @param sendDTO
     * @return
     */
    public ResponseDTO<String> sendNotify(SmsNotifySendDTO sendDTO) {
        String phone = sendDTO.getPhone();
        if (!Pattern.matches(SmartVerificationUtil.PHONE_REGEXP, phone)) {
            // 不是手机号 不发送
            return ResponseDTO.ok();
        }
        Integer notifyMsgType = sendDTO.getNotifyMsgType();
        SmsNotifyTypeEnum notifyTypeEnum = SmartBaseEnumUtil.getEnumByValue(notifyMsgType, SmsNotifyTypeEnum.class);

        ResponseDTO<String> response = aliSmsService.sendMessageNotify(notifyTypeEnum, Lists.newArrayList(phone), sendDTO.getParam());

        if (response.getOk()) {
            // 保存发送记录
            SmsRecordAddDTO recordAddDTO = new SmsRecordAddDTO();
            recordAddDTO.setSmsType(SmsTypeEnum.NOTIFY);
            recordAddDTO.setSmsSubType(notifyMsgType);
            recordAddDTO.setContent("");
            recordAddDTO.setExtraData(JSON.toJSONString(sendDTO.getParam()));
            recordAddDTO.setPhone(phone);
            recordAddDTO.setServiceRecordId(response.getData());
            recordAddDTO.setSuccessFlag(response.getOk());
            recordAddDTO.setErrorMsg(response.getMsg());
            smsRecordService.addRecord(recordAddDTO);
        }

        return ResponseDTO.ok();
    }


    /**
     * 发送通知类短信
     *
     * @param sendDTO
     * @return
     */
    @Async(AsyncConfig.ASYNC_EXECUTOR_THREAD_NAME)
    public ResponseDTO<String> sendNotifyBatch(SmsNotifyBatchSendDTO sendDTO) {
        // 过滤非手机号的
        List<String> phoneList = sendDTO.getPhoneList().stream().filter(e -> Pattern.matches(SmartVerificationUtil.PHONE_REGEXP, e)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(phoneList)) {
            return ResponseDTO.ok();
        }
        Integer notifyMsgType = sendDTO.getNotifyMsgType();
        SmsNotifyTypeEnum notifyTypeEnum = SmartBaseEnumUtil.getEnumByValue(notifyMsgType, SmsNotifyTypeEnum.class);

        // 分页
        int totalCount = phoneList.size();
        // 取余 同样也是最有一页条数
        int lastPageNum = totalCount % MAX_BATCH_SIZE;
        // 取整 获取总共页数
        int pages = totalCount / MAX_BATCH_SIZE;
        // 取余若有值 则页数加一
        if (lastPageNum > 0) {
            pages++;
        }
        // 循环发送
        for (int i = 0; i < pages; i++) {
            List<String> sendPhoneList = phoneList.stream().skip(i * MAX_BATCH_SIZE).limit(MAX_BATCH_SIZE).collect(Collectors.toList());
            ResponseDTO<String> response = aliSmsService.sendMessageNotify(notifyTypeEnum, sendPhoneList, sendDTO.getParam());
            if (response.getOk()) {
                SmsRecordBatchAddDTO batchAddDTO = new SmsRecordBatchAddDTO();
                batchAddDTO.setSmsType(SmsTypeEnum.NOTIFY);
                batchAddDTO.setSmsSubType(notifyMsgType);
                batchAddDTO.setContent("");
                batchAddDTO.setExtraData(JSON.toJSONString(sendDTO.getParam()));
                batchAddDTO.setPhoneList(sendPhoneList);
                batchAddDTO.setServiceRecordId(response.getData());
                batchAddDTO.setSuccessFlag(response.getOk());
                batchAddDTO.setErrorMsg(response.getMsg());
                smsRecordService.batchAddRecord(batchAddDTO);
            }

        }
        return ResponseDTO.ok();
    }

    /**
     * 发送验证码类短信
     *
     * @param phone
     * @param smsTypeEnum
     * @return
     */
    public ResponseDTO<String> sendVerification(String phone, SmsVerifyTypeEnum smsTypeEnum) {
        if (!Pattern.matches(SmartVerificationUtil.PHONE_REGEXP, phone)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号格式不正确");
        }

        if (!systemEnvironment.isProd()) {
            return ResponseDTO.ok();
        }

        // 校验 当天验证码发送次数是否超限、验证码是否频繁
        String smsRedisKey = this.getSmsVerifyRedisKey(phone);
        long dayCount = redisService.lGetListSize(smsRedisKey);
        if (dayCount > NumberConst.SMS_DAY_LIMIT) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您发送验证码的姿势很帅，但还是请明天再试吧！");
        }

        SmsVerifyRecordDTO smsRecordDTO;
        long nowSecond = SmartLocalDateUtil.nowSecond();
        if (dayCount > 0) {
            // 获取最后一次发送记录
            smsRecordDTO = (SmsVerifyRecordDTO) redisService.lGetIndex(smsRedisKey, dayCount - 1);
            Long sendTime = smsRecordDTO.getSendSecond();
            if (NumberConst.SMS_SEND_INTERVAL > nowSecond - sendTime) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "验证码发送过于频繁，请稍后重试哦");
            }
        }

        // 获取短信模版 生成验证码、短信内容
        String code = SmartRandomUtil.generateRandomNum(NumberConst.SMS_CODE);

        ResponseDTO<String> response = aliSmsService.sendVerifyCode(smsTypeEnum, phone, code);
        if (!response.getOk()) {
            return response;
        }

        // 发送成功 更新redis短信发送记录
        smsRecordDTO = new SmsVerifyRecordDTO();
        smsRecordDTO.setSendTime(SmartLocalDateUtil.formatYMDHMS(LocalDateTime.now()));
        smsRecordDTO.setSendSecond(nowSecond);
        smsRecordDTO.setSmsType(smsTypeEnum.getValue());
        smsRecordDTO.setVerificationCode(code);
        smsRecordDTO.setUseFlag(false);
        long daySecond = RedisService.currentDaySecond();
        daySecond = daySecond < NumberConst.SMS_VALID_SECOND ? NumberConst.SMS_VALID_SECOND : daySecond;
        redisService.lSet(smsRedisKey, smsRecordDTO, daySecond);

        // 保存发送记录
        SmsRecordAddDTO recordAddDTO = new SmsRecordAddDTO();
        recordAddDTO.setSmsType(SmsTypeEnum.VERIFICATION_CODE);
        recordAddDTO.setSmsSubType(smsTypeEnum.getValue());
        recordAddDTO.setContent("");
        recordAddDTO.setExtraData(code);
        recordAddDTO.setPhone(phone);
        recordAddDTO.setServiceRecordId(response.getData());
        recordAddDTO.setSuccessFlag(response.getOk());
        recordAddDTO.setErrorMsg(response.getMsg());
        smsRecordService.addRecord(recordAddDTO);

        return response;
    }

    /**
     * 用于测试环境 发送短信
     *
     * @param smsTypeEnum
     * @param phone
     * @param code
     */
    public void sitSend(SmsVerifyTypeEnum smsTypeEnum, String phone, String code) {

        SmsVerifyRecordDTO smsRecordDTO = new SmsVerifyRecordDTO();
        smsRecordDTO.setSendSecond(SmartLocalDateUtil.nowSecond());
        smsRecordDTO.setSendTime(SmartLocalDateUtil.formatYMDHMS(LocalDateTime.now()));
        smsRecordDTO.setSmsType(smsTypeEnum.getValue());
        smsRecordDTO.setVerificationCode(code);
        smsRecordDTO.setUseFlag(false);

        // 发送成功 更新redis短信发送次数和记录
        long daySecond = RedisService.currentDaySecond();
        daySecond = daySecond < NumberConst.SMS_VALID_SECOND ? NumberConst.SMS_VALID_SECOND : daySecond;

        redisService.lSet(this.getSmsVerifyRedisKey(phone), smsRecordDTO, daySecond);
    }

    /**
     * 其他一些额外的校验
     *
     * @param phone
     * @param smsTypeEnum
     * @return
     */
    private ResponseDTO<String> extraCheck(String phone, SmsVerifyTypeEnum smsTypeEnum) {
        return ResponseDTO.ok();
    }

    /**
     * 校验短信验证码是否正确
     *
     * @param phone
     * @param code
     * @return
     */
    public ResponseDTO<String> checkVerifyCode(String phone, String code, SmsVerifyTypeEnum smsTypeEnum) {
        String superPassword = systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD);

        //测试验证码无需验证
        if (code.equals(superPassword)) {
            return ResponseDTO.ok();
        }
        if (!Pattern.matches(SmartVerificationUtil.PHONE_REGEXP, phone)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号格式不正确哦");
        }
        // 获取该手机号的短信发送记录
        String smsRedisKey = this.getSmsVerifyRedisKey(phone);
        List<SmsVerifyRecordDTO> recordList = redisService.lGetAll(smsRedisKey).stream().map(e -> (SmsVerifyRecordDTO) e).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(recordList)) {
            // 尚未发送验证码
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您还没有发送验证码呢");
        }

        // 获取该类型的短信发送记录 最后一条
        Optional<SmsVerifyRecordDTO> optional = recordList.stream().filter(e -> Objects.equals(e.getSmsType(), smsTypeEnum.getValue())).sorted(Comparator.comparingLong(SmsVerifyRecordDTO::getSendSecond).reversed()).findFirst();
        if (!optional.isPresent()) {
            // 尚未发送验证码
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您还没有发送验证码呢");
        }

        // 获取最后一条
        SmsVerifyRecordDTO smsRecordDTO = optional.get();
        if (smsRecordDTO.getUseFlag() || SmartLocalDateUtil.nowSecond() - smsRecordDTO.getSendSecond() > NumberConst.SMS_VALID_SECOND) {
            // 验证码已失效
            System.err.println("验证码已失效");
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "验证码已失效，请重新获取吧");
        }

        if (!Objects.equals(code, smsRecordDTO.getVerificationCode())) {
            // 验证码错误
            System.err.println("验证码错误");
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "短信验证码错误");
        }
        return ResponseDTO.ok();
    }

    private String getSmsVerifyRedisKey(String phone) {
        return RedisKeyConst.Support.SMS_VERIFICATION + phone;
    }

    /**
     * 查询短信发送详情
     *
     * @param phone
     * @param bizId
     * @return
     */
    public ResponseDTO<SmsSendResultDTO> querySendDetail(String phone, String bizId) {
        return aliSmsService.querySendDetail(phone, bizId);
    }
}
