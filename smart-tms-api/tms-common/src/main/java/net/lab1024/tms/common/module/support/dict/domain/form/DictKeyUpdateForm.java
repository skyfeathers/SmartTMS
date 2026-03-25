package net.lab1024.tms.common.module.support.dict.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2022/5/26 12:21 上午
 */
@Data
public class DictKeyUpdateForm extends DictKeyAddForm {

    @ApiModelProperty("keyId")
    @NotNull(message = "keyId不能为空")
    private Long dictKeyId;
}