package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 副卡导入
 *
 * @Author lidoudou
 * @Date 2023/5/15 上午8:53
 */
@Data
public class SlaveOilCardImportForm {

    @ApiModelProperty(value = "公司ID")
    private Long enterpriseId;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    @NotNull(message = "燃料类型不能为空")
    @CheckEnum(value = OilCardFuelTypeEnum.class, message = "燃料类型错误")
    private Integer fuelType;

}
