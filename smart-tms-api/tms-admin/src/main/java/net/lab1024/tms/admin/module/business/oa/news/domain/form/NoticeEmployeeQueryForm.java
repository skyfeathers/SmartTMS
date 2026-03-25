package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

@Data
public class NoticeEmployeeQueryForm extends PageParam {

    @ApiModelProperty("标题、作者、来源、文号")
    private String keywords;

    @ApiModelProperty("分类")
    private Long noticeTypeId;

    @ApiModelProperty("发布-开始时间")
    private LocalDate publishTimeBegin;

    @ApiModelProperty("未读标识")
    private Boolean notViewFlag;

    @ApiModelProperty("发布-截止时间")
    private LocalDate publishTimeEnd;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;
}
