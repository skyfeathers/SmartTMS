package net.lab1024.tms.driver.module.business.feedback.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 意见反馈分页查询
 *
 * @Author: zhaoxinyang
 * @Date: 2023/09/01 14:18
 */
@Data
public class FeedbackQueryForm extends PageParam {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("用户类型")
    private Integer createUserType;

    @ApiModelProperty("搜索词")
    @Length(max = 25, message = "搜索词最多25字符")
    private String searchWord;

}
