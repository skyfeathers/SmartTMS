package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/12/15 9:25
 */
@Data
public class ShipperReportQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keyWords;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelPropertyEnum(value = ShipperNatureEnum.class, desc = "货主性质")
    private Integer shipperNature;

    @ApiModelPropertyEnum(value = ShipperTypeEnum.class, desc = "货主类型")
    private List<Integer> shipperTypeList;

    @ApiModelProperty(value = "删除标识",hidden = true)
    private Boolean deletedFlag;
}
