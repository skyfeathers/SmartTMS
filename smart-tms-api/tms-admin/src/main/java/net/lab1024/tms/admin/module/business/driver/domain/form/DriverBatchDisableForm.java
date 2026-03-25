package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 司机批量禁用
 *
 * @author lihaifan
 * @date 2022/7/7 17:19
 */
@Data
public class DriverBatchDisableForm {

    @ApiModelProperty("司机ID")
    @NotEmpty(message = "请选择司机")
    private List<Long> driverIdList;

    @ApiModelPropertyEnum(value = DriverStatusEnum.class, desc = "司机状态")
    @CheckEnum(value = DriverStatusEnum.class, required = true, message = "司机状态错误")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long requestUserId;

    @ApiModelProperty(hidden = true)
    private String requestUserName;
}
