package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 查询待收款的订单信息
 *
 * @author lidoudou
 * @date 2022/8/24 下午3:53
 */
@Data
public class WaitReceiveQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keyWords;

    private Long shipperId;

    @ApiModelProperty(value = "有效期开始时间", example = "2022-01-01")
    private LocalDate startDate;

    @ApiModelProperty(value = "有效期结束时间", example = "2022-01-01")
    private LocalDate endDate;

    @ApiModelProperty("销售人ID列表")
    private List<Long> saleIdList;

    @ApiModelProperty("是否存在待核销数据")
    private Boolean existWaitVerificationFlag;

    @ApiModelProperty(value = "货主销售类型", hidden = true)
    private Integer shipperSaleType;

    @ApiModelProperty(hidden = true)
    private Integer checkStatus;

    @ApiModelProperty(hidden = true)
    private List<Integer> invoiceStatusList;

    @ApiModelProperty(hidden = true)
    private Boolean verificationFlag;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;
}
