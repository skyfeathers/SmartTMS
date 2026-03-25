package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;

@Data
public class NoticeViewRecordQueryForm extends PageParam {

    @ApiModelProperty("通知公告id")
    @NotNull(message = "通知公告id不能为空")
    private Long noticeId;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("关键字")
    private String keywords;

}
