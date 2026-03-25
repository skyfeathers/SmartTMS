package net.lab1024.tms.admin.module.business.etc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 交易记录查询
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:45
 */
@Data
public class EtcBalanceRecordQueryForm extends PageParam {

    @NotNull(message = "etc ID不能为空")
    @ApiModelProperty("etc ID")
    private Long etcId;

    @ApiModelProperty("流水单号/创建人")
    private String keyWords;

    @ApiModelProperty("收入标识 0消耗 1充值")
    private Boolean incomeFlag;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;
}
