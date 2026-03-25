package net.lab1024.tms.admin.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量修改负责人
 *
 * @author lidoudou
 * @date 2022/8/12 下午5:12
 */
@Data
public class VehicleManagerUpdateForm {

    @ApiModelProperty("车辆ID列表")
    @NotNull(message = "车辆ID不能为空")
    @Size(min = 1, message = "车辆ID不能为空")
    private List<Long> vehicleIdList;

    @ApiModelProperty("负责人ID")
    private Long managerId;
}
