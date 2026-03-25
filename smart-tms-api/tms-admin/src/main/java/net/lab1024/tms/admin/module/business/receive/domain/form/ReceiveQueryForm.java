package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

/**
 * 查询form
 *
 * @author lidoudou
 * @date 2022/7/20 下午9:27
 */
@Data
public class ReceiveQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("关键字")
    private String waybillNumber;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主ID列表")
    private List<Long> shipperIdList;

    @ApiModelProperty("创建人ID列表")
    private List<Long> employeeIdList;

    @ApiModelProperty("销售人ID列表")
    private List<Long> saleIdList;

    @ApiModelProperty(value = "货主销售类型", hidden = true)
    private Integer shipperSaleType;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;

    @ApiModelProperty(value = "业务日期")
    private LocalDate businessDateBegin;

    @ApiModelProperty(value = "业务日期")
    private LocalDate businessDateEnd;

    @ApiModelProperty("是否需要开票")
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("是否逾期")
    private Boolean overdue;

    @ApiModelProperty("是否开票")
    private Integer invoiceStatus;

    @ApiModelProperty("创建-开始时间")
    private LocalDate createTime;

    @ApiModelProperty("创建-结束时间")
    private LocalDate endTime;

    @ApiModelProperty("账期-开始时间")
    private LocalDate accountPeriodStartTime;

    @ApiModelProperty("账期-结束时间")
    private LocalDate accountPeriodEndTime;

    @ApiModelProperty("核销状态")
    private Boolean verificationFlag;

    @ApiModelProperty("业务时间-月份开始时间")
    private LocalDate businessStartDate;

    @ApiModelProperty("业务时间-月份结束时间")
    private LocalDate businessEndDate;


    @ApiModelPropertyEnum(value = CheckStatusEnum.class, desc = "核算状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;
}
