package net.lab1024.tms.admin.module.business.feedback.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * @Author: dangxinfa
 * @Date: 2022/6/28
 */
@Data
public class FeedbackQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    @Length(max = 25, message = "搜索词最多25字符")
    private String searchWord;

    @ApiModelProperty(value = "开始时间", example = "2021-02-14")
    private LocalDate startDate;

    @ApiModelProperty(value = "截止时间", example = "2022-10-15")
    private LocalDate endDate;
}
