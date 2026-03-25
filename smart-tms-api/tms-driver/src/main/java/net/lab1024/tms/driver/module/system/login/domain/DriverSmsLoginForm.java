package net.lab1024.tms.driver.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.util.SmartVerificationUtil;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.token.LoginDeviceEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DriverSmsLoginForm {

    @ApiModelPropertyEnum(value = LoginDeviceEnum.class, desc = "登录设备类型")
    @CheckEnum(message = "登录设备类型不正确", value = LoginDeviceEnum.class)
    @NotNull(message = "登录设备类型不能为空")
    private Integer deviceType;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号码格式错误")
    @ApiModelProperty("手机号|必填")
    private String phone;

    @Pattern(regexp = SmartVerificationUtil.FIXED_NUMBER, message = "验证码格式错误")
    @NotBlank(message = "短信验证码不能为空")
    @Length(min = NumberConst.SMS_CODE, max = NumberConst.SMS_CODE, message = "短信验证码6位")
    @ApiModelProperty("短信验证码|必填")
    private String smsCode;

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;


}
