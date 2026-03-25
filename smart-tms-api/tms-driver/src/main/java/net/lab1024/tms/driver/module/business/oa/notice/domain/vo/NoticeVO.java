package net.lab1024.tms.driver.module.business.oa.notice.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.time.LocalDateTime;

/**
 * 通知公告 信息
 *
 * @author: zhaoxinyang
 * @date: 2023/08/28 16:34
 */
@Data
public class NoticeVO extends NoticeSimpleVO {
    
    @ApiModelProperty("作者")
    private String author;
    
    @ApiModelProperty("来源")
    private String source;
    
    @ApiModelProperty("文号")
    private String documentNumber;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;
    
    @ApiModelProperty("页面浏览量")
    private Integer pageViewCount;
    
    @ApiModelProperty("用户浏览量")
    private Integer userViewCount;
}
