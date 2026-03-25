package net.lab1024.tms.common.module.support.sms.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 新增 短信记录 实体类
 *
 * @author Turbolisten
 * @date 2020/3/16 16:44
 */
@Data
public class SmsRecordBatchAddDTO extends SmsRecordAddBasicDTO {

    /**
     * 接收人手机号
     */
    @NotEmpty(message = "接收人手机号不能为空")
    private List<String> phoneList;

}
