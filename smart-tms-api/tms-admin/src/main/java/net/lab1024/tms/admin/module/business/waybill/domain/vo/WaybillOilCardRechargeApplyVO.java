package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/8/20 4:53 下午
 */
@Data
public class WaybillOilCardRechargeApplyVO {

    private Long rechargeApplyId;



    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单号")
    @Excel(name = "运单号", width = 15, orderNum = "8")
    private String waybillNumber;

    @ApiModelProperty("油卡id")
    private Long oilCardId;

    @ApiModelProperty("油卡卡号")
    @Excel(name = "油卡卡号", orderNum = "1", width = 15)
    private String oilCardNo;

    @ApiModelProperty("充值日期")
    @Excel(name = "充值时间", orderNum = "1", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime rechargeTime;

    @ApiModelProperty("油卡主卡公司ID")
    private Long masterOilCardEnterpriseId;

    @ApiModelProperty("金额")
    @Excel(name = "油卡费", type = 10, orderNum = "2")
    private BigDecimal amount;

    @ApiModelProperty("车队id")
    private Long fleetId;

    @ApiModelProperty("车队名称")
    private String fleetName;

    @ApiModelProperty("司机id")
    private Long driverId;

    @ApiModelProperty("司机名称")
    @Excel(name = "司机名称", orderNum = "9")
    private String driverName;

    @ApiModelProperty("司机手机")
    @Excel(name = "司机电话", orderNum = "10", width = 15)
    private String driverPhone;

    @ApiModelProperty("车辆id")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    @Excel(name = "车辆", orderNum = "11", width = 15)
    private String vehicleNumber;

    @ApiModelProperty("备注")
    @Excel(name = "备注", orderNum = "16")
    private String remark;

    @ApiModelPropertyEnum(desc = "结算类型", value = WaybillSettleTypeEnum.class)
    private Integer settleType;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("所属公司id")
    private Long enterpriseId;

    @ApiModelProperty("订单所属公司")
    @Excel(name = "订单所属公司", orderNum = "5", width = 20)
    private String enterpriseName;

    @ApiModelProperty("油卡主卡所属公司")
    @Excel(name = "油卡主卡所属公司", orderNum = "5", width = 20)
    private String masterOilCardEnterpriseName;

    @ApiModelPropertyEnum(value = TransportationTypeEnum.class, desc = "业务类型")
    private Integer businessTypeCode;

    @ApiModelProperty("业务类型")
    @Excel(name = "业务类型", orderNum = "7", width = 15)
    private String containerBusinessTypeName;

    @ApiModelProperty("流程实例id")
    private Long flowInstanceId;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    @Excel(name = "创建人", orderNum = "14")
    private String createUserName;

    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", orderNum = "15", width = 20,format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}