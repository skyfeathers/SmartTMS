package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;

import java.time.LocalDate;
import java.util.List;

/***
 * 司机列表查询dto
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:54
 */
@Data
public class DriverQueryForm extends PageParam {

    @ApiModelProperty("姓名/电话/身份证号/创建人")
    private String keyWords;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("身份证号")
    private String drivingLicense;

    @ApiModelProperty("速记码")
    private String shorthandCode;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "业务类型")
    private Integer businessMode;

    @ApiModelPropertyEnum(value = DriverStatusEnum.class, desc = "状态")
    private Integer status;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer createUserType;

    @ApiModelProperty(value = "创建人名字")
    private String createUserName;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "身份证到期-开始时间", example = "2022-01-01")
    private LocalDate idCardStartTime;

    @ApiModelProperty(value = "身份证到期-结束时间", example = "2022-01-01")
    private LocalDate idCardEndTime;

    @ApiModelProperty(value = "出生年月-开始时间", example = "2022-01-01")
    private LocalDate birthdayStartTime;

    @ApiModelProperty(value = "出生年月-结束时间", example = "2022-01-01")
    private LocalDate birthdayEndTime;

    @ApiModelProperty(hidden = true)
    private List<Long> excludeIdList;

    @ApiModelProperty(hidden = true)
    private List<Long> includeIdList;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
