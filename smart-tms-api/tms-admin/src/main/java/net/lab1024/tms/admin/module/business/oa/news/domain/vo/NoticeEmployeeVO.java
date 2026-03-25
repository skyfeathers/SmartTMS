package net.lab1024.tms.admin.module.business.oa.news.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeEmployeeVO extends NoticeVO {

    @ApiModelProperty("是否查看")
    private Boolean viewFlag;

    @ApiModelProperty("发布日期")
    private LocalDate publishDate;

}
