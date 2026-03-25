package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自有车-现金信息 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:04
 */
@Data
public class CashInfoVO {

    @ApiModelProperty("初始余额")
    private BigDecimal initialAmount;

    @ApiModelProperty("充值金额（领取的出车款）")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("充值金额备注")
    private String rechargeRemark;

    @ApiModelProperty("小计")
    private BigDecimal subtotal;

    @ApiModelProperty("现金余额")
    private BigDecimal endAmount;

    @ApiModelProperty("费用项目")
    private List<CarCostItemInfoVO> itemInfoVOList;

}
