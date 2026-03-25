package net.lab1024.tms.driver.module.business.oa.notice.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告 简单信息
 *
 * @author: zhaoxinyang
 * @date: 2023/08/28 15:34
 */
@Data
public class NoticeSimpleVO {
    
    @ApiModelProperty("id")
    private Long noticeId;
    
    @ApiModelProperty("分类名称")
    private String noticeTypeName;
    
    @ApiModelProperty("标题")
    private String title;
    
    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;
    
    @ApiModelProperty("纯文本内容")
    private String contentText;
    
    @ApiModelProperty("html内容")
    private String contentHtml;
    
}
