package net.lab1024.tms.admin.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.util.SmartVerificationUtil;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.captcha.domain.CaptchaForm;
import net.lab1024.tms.common.module.support.token.LoginDeviceEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录
 *
 * @author 李开云
 * @date 2017年12月19日上午11:49:46
 */
@Data
public class LoginForm extends CaptchaForm {

    @ApiModelProperty(example = "admin")
    @NotBlank(message = "登录名不能为空")
    @Length(max = 30, message = "登录账号最多30字符")
    private String loginName;

    @ApiModelProperty(example = "123456")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = SmartVerificationUtil.PWD_REGEXP, message = "请输入6-15位密码(数字|大小写字母|小数点)")
    private String password;

    @ApiModelProperty(value = "登录终端")
    @ApiModelPropertyEnum(LoginDeviceEnum.class)
    @CheckEnum(value = LoginDeviceEnum.class, required = true, message = "此终端不允许登录")
    private Integer loginDevice;

    @ApiModelProperty(value = "唯一码")
    private String uniqueCode;
}
