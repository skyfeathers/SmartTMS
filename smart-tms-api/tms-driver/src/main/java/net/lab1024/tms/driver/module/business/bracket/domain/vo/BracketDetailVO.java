package net.lab1024.tms.driver.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.driver.module.business.bracket.domain.dto.BracketBaseDTO;

import java.time.LocalDateTime;

/**
 * 详情
 *
 * @author zhaoxinyang
 * @date 2023/09/07 16:33
 */
@Data
public class BracketDetailVO extends BracketListVO {

    @ApiModelProperty("审核状态描述")
    private String auditStatusDesc;

}
