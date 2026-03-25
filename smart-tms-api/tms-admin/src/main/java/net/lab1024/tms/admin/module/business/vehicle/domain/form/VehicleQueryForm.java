package net.lab1024.tms.admin.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.OwnerTypeEnum;

import java.time.LocalDate;
import java.util.List;

/***
 * 列表查询
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:40
 */
@Data
public class VehicleQueryForm extends PageParam {

    @ApiModelProperty("车牌号/速记码")
    private String vehicleKeyWords;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("速记码")
    private String shorthand;

    @ApiModelProperty("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty("道路运输证号")
    private String roadTransportCertificateNumber;

    @ApiModelProperty("车辆类型")
    private String vehicleType;

    @ApiModelPropertyEnum(desc = "经营方式", value = BusinessModeEnum.class)
    private Integer businessMode;

    @ApiModelProperty("删除标识")
    private Boolean  deletedFlag;

    @ApiModelProperty("禁用标识")
    private Boolean  disabledFlag;

    @ApiModelProperty("挂靠企业名称")
    private String relyEnterpriseName;

    @ApiModelPropertyEnum(desc = "所属人性质", value = OwnerTypeEnum.class)
    private Integer ownerType;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("车辆所属公司")
    private Long enterpriseId;

    @ApiModelProperty(value = "年审时间-开始时间", example = "2022-01-01")
    private LocalDate vehicleAuditExpireStartTime;

    @ApiModelProperty(value = "年审时间-结束时间", example = "2022-01-01")
    private LocalDate vehicleAuditExpireEndTime;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "行驶证到期-开始时间", example = "2022-01-01")
    private LocalDate drivingLicenseExpireStartTime;

    @ApiModelProperty(value = "行驶证到期-结束时间", example = "2022-01-01")
    private LocalDate drivingLicenseExpireEndTime;

    @ApiModelProperty(value = "挂靠到期-开始时间", example = "2022-01-01")
    private LocalDate relyEnterpriseStartTime;

    @ApiModelProperty(value = "挂靠到期-结束时间", example = "2022-01-01")
    private LocalDate relyEnterpriseEndTime;

    @ApiModelProperty(value = "道路运输证到期时间-开始时间", example = "2022-01-01")
    private LocalDate roadTransportCertificateExpireDateStartTime;

    @ApiModelProperty(value = "道路运输证到期时间-结束时间", example = "2022-01-01")
    private LocalDate roadTransportCertificateExpireDateEndTime;

    @ApiModelProperty(hidden = true)
    private List<Long> excludeIdList;

    @ApiModelProperty(hidden = true)
    private List<Long> includeIdList;
}