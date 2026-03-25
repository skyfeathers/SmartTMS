package net.lab1024.tms.driver.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.driver.module.business.bracket.domain.dto.BracketBaseDTO;

import java.time.LocalDateTime;

/**
 * 列表展示
 *
 * @author lidoudou
 * @date 2022/6/29 上午11:40
 */
@Data
public class BracketListVO extends BracketBaseDTO {

    @ApiModelProperty("挂车ID")
    private Long bracketId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
