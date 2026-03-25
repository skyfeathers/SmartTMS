package net.lab1024.tms.common.module.support.sms.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 新增 短信记录 实体类
 *
 * @author Turbolisten
 * @date 2020/3/16 16:44
 */
@Data
public class SmsRecordAddDTO extends SmsRecordAddBasicDTO{

    /**
     * 接收人手机号
     */
    @NotBlank(message = "接收人手机号不能为空")
    @Length(max = 11, message = "手机号最多11字符")
    private String phone;
}
