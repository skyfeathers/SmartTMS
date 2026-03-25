package net.lab1024.tms.admin.module.business.fleet.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;

import java.util.List;

/**
 * 车队详情
 *
 * @author lidoudou
 * @date 2022/6/27 下午5:11
 */
@Data
public class FleetDetailVO extends FleetVO {

    @ApiModelProperty("银行列表")
    private List<FleetBankDTO> bankList;
}
