package net.lab1024.tms.admin.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 挂车列表
 *
 * @author lidoudou
 * @date 2022/11/2 上午10:31
 */
@Data
public class BracketSimpleVO {

    @ApiModelProperty("挂车ID")
    private Long bracketId;

    @ApiModelProperty("挂车编号")
    private String bracketNo;

    @ApiModelProperty("速记码")
    private String shorthandCode;
}
