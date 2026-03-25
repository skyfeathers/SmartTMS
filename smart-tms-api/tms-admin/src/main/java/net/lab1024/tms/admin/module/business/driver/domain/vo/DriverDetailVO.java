package net.lab1024.tms.admin.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.util.List;

/***
 * 司机详情
 *
 * @author lidoudou
 * @date 2022/6/22 下午5:48
 */
@Data
public class DriverDetailVO extends DriverVO {

    @ApiModelPropertyEnum(value = DriverStatusEnum.class, desc = "状态")
    @DataTracerFieldDoc("启用状态")
    @DataTracerFieldEnum(enumClass = DriverStatusEnum.class)
    private Integer status;

    @ApiModelProperty("银行卡列表")
    private List<DriverBankVO> bankList;

    @ApiModelProperty("司机车辆关联")
    private List<DriverVehicleVO> driverVehicleList;
}
