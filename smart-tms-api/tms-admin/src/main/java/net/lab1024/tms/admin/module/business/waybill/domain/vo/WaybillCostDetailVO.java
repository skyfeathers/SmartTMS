package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/9/29 4:13 下午
 */
@Data
public class WaybillCostDetailVO extends WaybillVO {

    @ApiModelProperty("订单调度")
    private String scheduleName;

    @ApiModelProperty("应付费用信息")
    private List<WaybillCostVO> costList;

    @ApiModelProperty("应收费用信息")
    private List<WaybillReceiveCostVO> receiveCostList;

}