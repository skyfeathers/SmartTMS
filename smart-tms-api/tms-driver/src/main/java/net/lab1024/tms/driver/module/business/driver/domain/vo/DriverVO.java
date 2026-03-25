package net.lab1024.tms.driver.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverBaseForm;

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

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = " 审核状态")
    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    private Integer auditStatus;

}
