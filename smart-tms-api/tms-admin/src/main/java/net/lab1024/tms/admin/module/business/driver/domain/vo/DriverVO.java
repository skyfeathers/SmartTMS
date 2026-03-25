package net.lab1024.tms.admin.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverBaseForm;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 * 司机列表展示
 *
 * @author lidoudou
 * @date 2022/6/22 下午5:48
 */
@Data
public class DriverVO extends DriverBaseForm {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("所属企业名称")
    private String enterpriseName;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = " 审核状态")
    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer createUserType;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("负责人姓名")
    private String managerName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelPropertyEnum(value = DriverStatusEnum.class, desc = "状态")
    private Integer status;

    @ApiModelProperty("关联车辆信息")
    private List<DriverVehicleVO> driverVehicleVOList;

    @ApiModelProperty("删除标识")
    private Boolean deletedFlag;

    @ApiModelProperty("司机余额")
    private BigDecimal balance;
}
