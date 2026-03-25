package net.lab1024.tms.admin.module.business.fleet.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBaseDTO;

import java.util.List;

/***
 * @author lidoudou
 * @date 2022/6/27 下午4:00
 */
@Data
public class FleetAddForm extends FleetBaseDTO {

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("银行列表")
    private List<FleetBankDTO> bankList;
}
