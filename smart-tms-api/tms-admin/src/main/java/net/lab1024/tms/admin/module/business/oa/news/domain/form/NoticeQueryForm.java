package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 通知公告 分页查询
 *
 * @author: zhuoda
 * @date: 2022/08/12 20:24
 */
@Data
public class NoticeQueryForm extends PageParam {

    @ApiModelProperty("分类")
    private Long noticeTypeId;

    @ApiModelProperty("标题、作者、来源")
    private String keywords;

    @ApiModelProperty("文号")
    private String documentNumber;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("删除标识")
    private Boolean deletedFlag;

    @ApiModelProperty("创建-开始时间")
    private LocalDate createTimeBegin;

    @ApiModelProperty("创建-截止时间")
    private LocalDate createTimeEnd;

    @ApiModelProperty("发布-开始时间")
    private LocalDate publishTimeBegin;

    @ApiModelProperty("发布-截止时间")
    private LocalDate publishTimeEnd;

    @ApiModelProperty("通知类型")
    private Integer noticePushType;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
