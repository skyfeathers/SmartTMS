package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;

import java.time.LocalDateTime;

/**
 * 油卡充值记录vo
 *
 * @author lidoudou
 * @date 2022/9/20 上午9:08
 */
@Data
public class WaybillOilCardRechargeListVO extends WaybillOilCardRechargeApplyVO {

    @ApiModelProperty("货主简称")
    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    @Excel(name = "订单编号", orderNum = "4", width = 20)
    private String orderNo;

    @Excel(name = "结算对象", orderNum = "12")
    private String settleTypeDesc;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "审核状态")
    @Excel(name = "审核状态", orderNum = "3")
    private String auditStatusDesc;

    @ApiModelPropertyEnum(value = TransportationTypeEnum.class, desc = "业务类型")
    @Excel(name = "运输类型", orderNum = "6", width = 15)
    private String businessTypeCodeDesc;

    @ApiModelProperty("业务类型")
    private Long containerBusinessTypeId;

    @ApiModelProperty("核销状态")
    private Boolean verificationFlag;

    @ApiModelProperty("核销人")
    private String verificationUserName;

    @ApiModelProperty("核销日期")
    private LocalDateTime verificationTime;

    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @ApiModelProperty("核销附件")
    private String verificationAttachment;

    @ApiModelProperty("是否可审核")
    private Boolean auditFlag;

    @ApiModelProperty("当前审核人")
    @Excel(name = "审批人", orderNum = "3")
    private String currentAuditor;

    @ApiModelProperty("充值状态")
    private Boolean rechargeFlag;

    @ApiModelProperty("充值人")
    private String rechargeUserName;

    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @ApiModelProperty("充值附件")
    private String rechargeAttachment;

    @ApiModelProperty("主卡ID")
    private Long masterOilCardId;

    @ApiModelProperty("主卡号")
    @Excel(name = "主卡号", orderNum = "1", width = 20)
    private String masterOilCardNo;

    @ApiModelProperty("主卡所属公司")
    @Excel(name = "主卡所属公司", orderNum = "1", width = 20)
    private String masterOilCardEnterpriseName;

    @ApiModelProperty("订单创建人")
    private String orderCreateUserName;

    @ApiModelProperty("箱号")
    @Excel(name = "箱号", orderNum = "1", width = 20)
    private String containerNumber;

    @ApiModelProperty("提箱时间")
    @Excel(name = "提箱时间", orderNum = "1", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliverGoodsTime;

    @ApiModelProperty("当前审批人")
    @Excel(name = "当前审批人", orderNum = "3", width = 20)
    private String checkUserName;
}