package net.lab1024.tms.driver.module.business.bracket.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * 查询
 *
 * @author zhaoxinyang
 * @date 2023/09/07 08:38
 */
@Data
public class BracketQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty(value = "司机ID", hidden = true)
    private Long driverId;

    @ApiModelProperty(value = "用户类型", hidden = true)
    private Integer userType;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

}
