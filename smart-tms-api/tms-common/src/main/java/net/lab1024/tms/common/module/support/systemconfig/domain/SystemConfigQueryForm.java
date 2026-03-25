package net.lab1024.tms.common.module.support.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 分页查询 系统配置
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigQueryForm extends PageParam {

    @ApiModelProperty("参数KEY")
    @Length(max = 50, message = "参数Key最多50字符")
    private String configKey;
}
