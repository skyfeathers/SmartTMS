package net.lab1024.tms.common.module.support.sms.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.common.util.SmartVerificationUtil;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.sms.constant.SmsNotifyTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 发送 通知类短信 DTO
 *
 * @author Turbolisten
 * @date 2020/4/23 16:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SmsNotifySendDTO {

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号码格式错误")
    private String phone;

    @CheckEnum(value = SmsNotifyTypeEnum.class, required = true, message = "短信类型错误")
    private Integer notifyMsgType;

    @ApiModelProperty("模版参数集合")
    private Map<String, Object> param;

}
