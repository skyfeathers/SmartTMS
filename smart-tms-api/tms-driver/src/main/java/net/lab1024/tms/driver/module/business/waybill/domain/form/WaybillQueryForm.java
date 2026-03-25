package net.lab1024.tms.driver.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillQueryForm extends PageParam {

    @ApiModelProperty("运单号/订单号/集装箱号/箱号铅封号")
    private String keywords;

    @ApiModelPropertyEnum(value = WaybillStatusEnum.class, desc = "运单状态")
    private List<Integer> waybillStatusList;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("车辆")
    private Long vehicleId;

    @ApiModelProperty("运单创建开始时间")
    private LocalDate startTime;

    @ApiModelProperty("运单创建截止时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "司机ID", hidden = true)
    private Long driverId;

    @ApiModelProperty(value = "公司ID", hidden = true)
    private Long enterpriseId;

}
