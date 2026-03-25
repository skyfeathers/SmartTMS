package net.lab1024.tms.admin.module.business.receive.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 应收帐款 展示信息
 *
 * @author lidoudou
 * @date 2022/7/21 上午9:47
 */
@Data
public class ReceiveOrderVO {

    @ApiModelProperty("收款单ID")
    private Long receiveOrderId;

    @ApiModelProperty("收款单号")
    @Excel(name = "收款单号", width = 20)
    private String receiveOrderNumber;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    @Excel(name = "货主名称", width = 30)
    private String shipperName;

    @ApiModelProperty("货主简称")
    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @ApiModelProperty("负责人ID")
    private Long managerId;

    @ApiModelProperty("负责人名字")
    private String managerName;

    @ApiModelProperty("逾期天数")
    private Integer overdueDays;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("订单所属公司")
    @Excel(name = "订单所属公司", orderNum = "10", width = 20)
    private String enterpriseName;

    @ApiModelProperty("业务日期，账单月份")
    @Excel(name = "业务日期", format = "yyyy-MM")
    private LocalDate businessDate;

    @ApiModelProperty("是否需要开票")
    @Excel(name = "是否需要开票", orderNum = "6", width = 20, replace = {"是_true", "否_false"})
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("应收合计")
    @Excel(name = "应收总额", orderNum = "5", width = 20)
    private BigDecimal totalAmount;

    @ApiModelProperty("已销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("未销金额")
    private BigDecimal unpaidAmount;

    @ApiModelProperty("税点")
    @Excel(name = "税点", orderNum = "7", width = 20)
    private BigDecimal taxPoint;

    @ApiModelProperty("应收对账备注")
    @Excel(name = "应收对账备注", orderNum = "8", width = 20)
    private String remark;

    @Excel(name = "作废备注", orderNum = "9", width = 20)
    private String cancelRemark;

    @ApiModelProperty("核算人")
    private String checkUserName;

    @ApiModelPropertyEnum(desc = "是否核算", value = CheckStatusEnum.class)
    private Integer checkStatus;

    @ApiModelProperty("核算凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String checkAttachment;

    @ApiModelProperty("核算备注")
    private String checkRemark;

    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", orderNum = "12", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty("创建人")
    @Excel(name = "创建人", orderNum = "11", width = 15)
    private String createUserName;

    @ApiModelProperty("账期")
    private LocalDate accountPeriodDate;

    @ApiModelProperty("开票状态")
    private Integer invoiceStatus;

    @ApiModelProperty("受票方")
    private String invoiceName;

    @ApiModelProperty("开票备注")
    private String invoiceRemark;

    @ApiModelProperty("核销状态")
    private Boolean verificationFlag;

    @ApiModelProperty("对账单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String billAttachment;

    @ApiModelProperty("核算附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("核销记录")
    private List<ReceiveOrderVerificationVO> recordList;

    @ApiModelProperty("邮寄信息")
    private ReceiveOrderMailAddressVO mailAddressVO;

}
