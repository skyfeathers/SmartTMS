package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardBaseDTO;

import java.util.List;

/**
 * 添加
 *
 * @author lidoudou
 * @date 2022/6/30 上午10:54
 */
@Data
public class OilCardAddForm extends OilCardBaseDTO {

    @ApiModelProperty("密码")
    private String password;

}
