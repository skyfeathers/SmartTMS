package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * 银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:36
 */
@Data
public class DriverBankAddForm extends DriverBankBaseForm {

    @ApiModelProperty("司机ID")
    @NotNull(message = "司机ID不能为空")
    private Long driverId;

}
