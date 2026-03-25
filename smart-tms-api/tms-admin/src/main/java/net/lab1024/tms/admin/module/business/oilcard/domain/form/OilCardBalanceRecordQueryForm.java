package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 交易记录查询
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:45
 */
@Data
public class OilCardBalanceRecordQueryForm extends PageParam {

    @NotNull(message = "油卡ID不能为空")
    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("流水单号/创建人")
    private String keyWords;

    @ApiModelPropertyEnum(desc = "交易记录类型", value = OilCardBalanceRecordTypeEnum.class)
    private Integer type;

    @ApiModelPropertyEnum(desc = "交易记录类型", value = OilCardBalanceRecordTypeEnum.class)
    private List<Integer> typeList;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate endTime;
}
