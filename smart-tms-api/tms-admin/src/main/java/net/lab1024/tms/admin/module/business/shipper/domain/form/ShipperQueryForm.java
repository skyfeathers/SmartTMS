package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperGradeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTagEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:58
 */
@Data
public class ShipperQueryForm extends PageParam {

    @ApiModelProperty("名称/简称/创建人")
    private String keyWords;

    @ApiModelProperty("名称")
    private String consignor;

    @ApiModelProperty("简称")
    private String shortName;

    @ApiModelProperty(value = "创建人名字")
    private String createUserName;

    @ApiModelProperty("校验状态")
    private Boolean verifyFlag;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty("禁用状态")
    private Boolean disableFlag;

    @ApiModelPropertyEnum(value = ShipperTypeEnum.class, desc = "货主类型")
    private List<Integer> shipperTypeList;

    @ApiModelPropertyEnum(value = ShipperGradeEnum.class, desc = "等级集合")
    private List<Integer> gradeList;

    @ApiModelPropertyEnum(value = ShipperTagEnum.class, desc = "标签集合")
    private List<Integer> tagTypeList;

    @ApiModelPropertyEnum(value = ShipperNatureEnum.class, desc = "类型")
    private Integer shipperNature;

    @ApiModelProperty("业务负责人")
    private Long managerId;

    @ApiModelProperty("是否查询无业务负责人数据")
    private Boolean noManagerFlag;

    @ApiModelProperty("客服负责人")
    private Long customerServiceId;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("地区")
    private List<String> areaList;

    @ApiModelProperty("查询开始时间")
    private LocalDate startTime;

    @ApiModelProperty("查询结束是时间")
    private LocalDate endTime;

    @ApiModelPropertyEnum(desc = "审核状态", value = AuditStatusEnum.class)
    private Integer auditStatus;
}
