package net.lab1024.tms.admin.module.business.etc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 编辑
 *
 * @author lidoudou
 * @date 2022/6/30 上午10:54
 */
@Data
public class EtcUpdateForm extends EtcAddForm {

    @ApiModelProperty("ETC ID")
    @NotNull(message = "ETC ID不能为空")
    private Long etcId;

}
