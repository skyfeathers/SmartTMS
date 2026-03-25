package net.lab1024.tms.common.module.support.sms;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.RedisKeyConst;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.redis.RedisService;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.domain.SmsVerifySendForm;
import net.lab1024.tms.common.module.support.sms.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 短信路由
 *
 * @author listen
 * @date 2019/09/26 21:13
 */
@Api(tags = SwaggerTagConst.Support.SUPPORT_SMS)
@RestController
public class SmsController extends SupportBaseController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisService redisService;

    /**
     * 发送短信 需要使用/support/sms/send/verify/{phone}/{smsType}路径？
     * @param phone
     * @param smsType
     * @return
     */
    @NoNeedLogin
    @ApiOperation(value = "发送短信验证码", notes = SmsVerifyTypeEnum.INFO)
    @GetMapping("/sms/send/verify/{phone}/{smsType}")
    public ResponseDTO<String> sendVerifyCode(@PathVariable String phone, @PathVariable Integer smsType) {
        return ResponseDTO.userErrorParam("您使用的APP版本过低，请前往应用市场下载最新版本。");
    }

    @NoNeedLogin
    @ApiOperation(value = "发送短信验证码", notes = SmsVerifyTypeEnum.INFO)
    @PostMapping("/sms/send/verify")
    public ResponseDTO<String> sendVerifyCode(@RequestBody @Valid SmsVerifySendForm smsVerifySendForm) {
        SmsVerifyTypeEnum smsTypeEnum = SmartBaseEnumUtil.getEnumByValue(smsVerifySendForm.getSmsType(), SmsVerifyTypeEnum.class);
        if (null == smsTypeEnum) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        /**
         * 1、校验redis里的验证码
         * 2、校验成功后，删除redis
         */
        String redisCaptchaKey = redisService.generateRedisKey(RedisKeyConst.Support.CAPTCHA, smsVerifySendForm.getCaptchaUuid());
        String redisCaptchaCode = redisService.get(redisCaptchaKey);
        if (StringUtils.isBlank(redisCaptchaCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "图片验证码已过期，请刷新重试");
        }
        if (!Objects.equals(redisCaptchaCode, smsVerifySendForm.getCaptchaCode())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "图片验证码错误，请输入正确的验证码");
        }
        return smsService.sendVerification(smsVerifySendForm.getPhone(), smsTypeEnum);
    }
}
