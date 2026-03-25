package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自有车-油卡信息 包含期初期末 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:04
 */
@Data
public class OilCardInfoVO {

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "油卡燃料类型")
    private Integer fuelType;

    @ApiModelProperty("初始余额")
    @Excel(name = "初始余额", type = 10)
    private BigDecimal initialAmount;

    @ApiModelProperty("油卡充值")
    @Excel(name = "油卡充值", type = 10)
    private BigDecimal oilCardReceiveAmount;

    @ApiModelProperty("油卡充值备注")
    private String oilCardReceiveRemark;

//    @ApiModelProperty("定点充值")
//    private BigDecimal oilCardFixedPointReceiveAmount;

    @ApiModelProperty("油卡支付金额")
    @Excel(name = "加油金额", type = 10)
    private BigDecimal oilCardPayAmount;

    private BigDecimal oilCardPayOilConsumption;

    @ApiModelProperty("油卡支付备注")
    private String oilCardPayRemark;

    @ApiModelProperty("现金支付金额")
    @Excel(name = "现金加油金额", type = 10)
    private BigDecimal cashPayAmount;

    private BigDecimal cashPayOilConsumption;

    @ApiModelProperty("现金支付备注")
    private String cashPayRemark;

    /**
     * 升数
     */
    private BigDecimal oilConsumption;

    @ApiModelProperty("小计")
    @Excel(name = "小计", type = 10)
    private BigDecimal subtotal;

    @ApiModelProperty("油卡余额")
    @Excel(name = "油卡余额", type = 10)
    private BigDecimal endAmount;

}
