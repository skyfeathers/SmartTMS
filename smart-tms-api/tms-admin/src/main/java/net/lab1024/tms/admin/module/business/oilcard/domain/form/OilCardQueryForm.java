package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * 油卡查询form
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:44
 */
@Data
public class OilCardQueryForm extends PageParam {

    @ApiModelProperty("油卡卡号/创建人")
    private String keyWords;

    @ApiModelPropertyEnum(value = OilCardTypeEnum.class, desc = "油卡类型")
    private Integer type;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    private Integer fuelType;

    @ApiModelProperty("主卡id")
    private Long masterOilCardId;

    @ApiModelProperty("所属公司")
    private List<Long> enterpriseIdList;

    @ApiModelProperty("禁用状态 true禁用")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate endTime;

    @ApiModelProperty(value = "金额变动记录-开始时间", example = "2022-01-01")
    private LocalDate balanceRecordStartTime;

    @ApiModelProperty(value = "金额变动记录-结束时间", example = "2022-01-01")
    private LocalDate balanceRecordEndTime;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "充值记录类型", hidden = true)
    private Integer recordType;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
