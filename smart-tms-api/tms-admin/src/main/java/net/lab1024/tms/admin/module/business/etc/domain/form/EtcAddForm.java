package net.lab1024.tms.admin.module.business.etc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.etc.domain.dto.EtcBaseDTO;

/**
 * 添加
 *
 * @author lidoudou
 * @date 2022/6/30 上午10:54
 */
@Data
public class EtcAddForm extends EtcBaseDTO {

    @ApiModelProperty("密码")
    private String password;
}
