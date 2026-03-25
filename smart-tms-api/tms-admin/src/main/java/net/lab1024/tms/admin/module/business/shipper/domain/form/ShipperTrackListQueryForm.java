package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTagEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * [ 货主跟进查询对象信息 ]
 *
 * @author yandanyang
 * @date 2020/10/24 10:27
 */
@Data
public class ShipperTrackListQueryForm extends PageParam {

    @ApiModelPropertyEnum(value = ShipperTagEnum.class, desc = "标签")
    @CheckEnum(value = ShipperTagEnum.class, message = "标签类型错误")
    private List<Integer> tagTypeList;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("跟进人id")
    private Long trackEmployeeId;

    @ApiModelProperty("省编码")
    private Integer provinceCode;

    @ApiModelProperty("市编码")
    private Integer cityCode;

    @ApiModelProperty("地区关键字")
    private String areaKeywords;

    @ApiModelProperty(value = "商务经理ID", hidden = true)
    private Long managerId;

    @ApiModelProperty(value = "分校id列表", hidden = true)
    private List<Long> shipperIdList;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "公海标识", hidden = true)
    private Boolean commonFlag;

    @ApiModelPropertyEnum(value = ShipperTypeEnum.class, desc = "货主类型", hidden = true)
    private List<Integer> shipperTypeList;

    @ApiModelPropertyEnum(value = ShipperNatureEnum.class, desc = "货主性质", hidden = true)
    private Integer shipperNature;

    @ApiModelProperty(value = "以创建时间查询-开始时间", example = "2021-01-01")
    private LocalDate startTime;

    @ApiModelProperty(value = "以创建时间查询-截至时间", example = "2022-10-15")
    private LocalDate endTime;
}
