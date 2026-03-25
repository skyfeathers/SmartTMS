package net.lab1024.tms.driver.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询
 *
 * @author zhaoxinyang
 * @date 2023/09/07 08:40
 */
@Data
public class BracketSimpleVO {

    @ApiModelProperty("挂车车牌号")
    private Long bracketId;

    @ApiModelProperty("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty("审核状态")
    private Integer auditStatus;

}
