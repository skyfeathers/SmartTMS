package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;

import java.time.LocalDate;
import java.util.List;

/***
 * 列表查询
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:40
 */
@Data
public class RepairQueryForm extends PageParam {

    @ApiModelProperty("模块ID")
    private Long moduleId;

    @ApiModelProperty("模块ID列表")
    private List<Long> moduleIdList;

    @ApiModelProperty("维修厂ID")
    private Long repairPlantId;

    @ApiModelProperty("模块类型")
    @CheckEnum(message = "模块类型错误", value = RepairModuleTypeEnum.class)
    private Integer moduleType;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty("车辆模块")
    private Integer vehicleModule = RepairModuleTypeEnum.VEHICLE.getValue();

    @ApiModelProperty("挂车模块")
    private Integer bracketModule = RepairModuleTypeEnum.BRACKET.getValue();

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}