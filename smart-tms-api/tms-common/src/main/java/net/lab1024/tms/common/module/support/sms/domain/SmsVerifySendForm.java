package net.lab1024.tms.common.module.support.sms.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lidoudou
 * @description:
 * @date 2024/5/15 09:57
 */
@Data
public class SmsVerifySendForm {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("发送类型")
    @NotNull(message = "手机号不能为空")
    @CheckEnum(value = SmsVerifyTypeEnum.class, message = "发送类型错误")
    private Integer smsType;

    @ApiModelProperty(value = "图形验证码")
    @NotBlank(message = "图形验证码不能为空")
    private String captchaCode;

    @ApiModelProperty(value = "图形验证码uuid标识")
    @NotBlank(message = "图形验证码uuid标识不能为空")
    private String captchaUuid;
}