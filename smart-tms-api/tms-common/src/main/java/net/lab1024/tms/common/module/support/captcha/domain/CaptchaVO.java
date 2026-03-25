package net.lab1024.tms.common.module.support.captcha.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图形验证码 VO
 *
 * @author 胡克
 * @date 2021/8/31 20:52
 */
@Data
public class CaptchaVO {

    @ApiModelProperty("验证码唯一标识" )
    private String captchaUuid;

    @ApiModelProperty("验证码图片内容-生产环境无效" )
    private String captchaText;

    @ApiModelProperty("验证码Base64图片" )
    private String captchaBase64Image;

    @ApiModelProperty("过期时间（秒）" )
    private Long expireSeconds;
}
