package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新 通知、公告
 */
@Data
public class NoticeUpdateForm extends NoticeAddForm {

    @ApiModelProperty("id")
    @NotNull(message = "通知id不能为空")
    private Long noticeId;

}
