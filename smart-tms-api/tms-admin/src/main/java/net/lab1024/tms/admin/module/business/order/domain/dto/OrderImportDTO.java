package net.lab1024.tms.admin.module.business.order.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据导入
 *
 * @author lidoudou
 * @date 2022/11/19 下午5:09
 */
@Data
public class OrderImportDTO {

    @Excel(name = "货主全称")
    @NotBlank(message = "货主全称不能为空")
    private String consignor;

    @Excel(name = "本单调度员")
    @NotBlank(message = "本单调度员不能为空")
    private String scheduleName;

    @Excel(name = "运输类型")
    @NotBlank(message = "运输类型不能为空")
    private String transportationTypeName;

    @Excel(name = "运输方式")
    @NotBlank(message = "运输方式不能为空")
    private String transportModeName;

    @Excel(name = "业务类型")
    private String containerBusinessTypeName;

    @Excel(name = "柜型")
    private String cabinetName;

    @Excel(name = "货物类型")
    @NotBlank(message = "货物类型不能为空")
    private String goodsTypeName;

    @Excel(name = "货物名称")
    @NotBlank(message = "货物名称不能为空")
    private String goodsName;

    @Excel(name = "货物单位")
    @NotBlank(message = "货物单位不能为空")
    private String goodsUnitName;

    @Excel(name = "货物量")
    @NotNull(message = "货物量不能为空")
    private BigDecimal goodsQuantity;

    @Excel(name = "做柜时间")
    private LocalDateTime deliverGoodsTime;

    @Excel(name = "业务操作人")
    @NotBlank(message = "业务操作人不能为空")
    private String createUserName;

    @Excel(name = "路线")
    private String transportRouteName;

    @Excel(name = "司机")
    @NotBlank(message = "司机不能为空")
    private String driverName;

    @Excel(name = "司机手机号")
    @NotBlank(message = "司机手机号不能为空")
    private String driverPhone;

    @Excel(name = "车牌")
    @NotBlank(message = "车牌不能为空")
    private String vehicleNumber;

    // 箱号
    @Excel(name = "柜号")
    private String containerNumber;

    @Excel(name = "补充说明")
    private String remark;

    @Excel(name = "应收运费")
    @NotNull(message = "应收运费不能为空")
    private BigDecimal receiveAmount;

    @Excel(name = "支出运费")
    @NotNull(message = "支出运费不能为空")
    private BigDecimal payAmount;

    @Excel(name = "油卡费用")
    @NotNull(message = "油卡费用不能为空")
    private BigDecimal oilCardAmount;

    @Excel(name = "税点")
    @NotNull(message = "税点不能为空")
    private BigDecimal taxPoint;

    @Excel(name = "业务时间")
    @NotNull(message = "业务时间不能为空")
    private LocalDate businessDate;

    @Excel(name = "装/卸货时间")
    @NotNull(message = "装/卸货时间不能为空")
    private LocalDateTime loadTime;

    @Excel(name = "结算方式")
    @NotBlank(message = "结算方式不能为空")
    private String settleModeName;

    @Excel(name = "尿素费用")
    private BigDecimal ureaCost;

    @Excel(name = "维修费用")
    private BigDecimal maintenanceCost;

    @Excel(name = "司机工资")
    private BigDecimal salaryCost;

    @Excel(name = "其他费用")
    private BigDecimal otherCost;



}
