package net.lab1024.tms.common.module.business.carcost.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;

import java.math.BigDecimal;

/**
 * 自有车列表 VO
 *
 * @author zhaoxinyang
 * @date 2023/11/02 09:57
 */
@Data
public class CarCostTabulationVO extends CarCostTabulationSimpleVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机名字")
    private String driverName;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("分类ID")
    private String categoryId;

    @ApiModelProperty("分类名字")
    private String categoryName;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡号")
    private String oilCardNo;

    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用类型")
    private Integer costType;

    @ApiModelProperty("升数")
    private BigDecimal oilConsumption;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

}
