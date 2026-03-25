package net.lab1024.tms.admin.module.business.oa.news.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通知、公告的 类型
 *
 * @author: zhuoda
 * @date: 2022/08/12 20:26
 */
@Data
public class NoticeTypeVO {

    @ApiModelProperty("通知类型id")
    private Long noticeTypeId;

    @ApiModelProperty("通知类型-名称")
    private String noticeTypeName;

}
