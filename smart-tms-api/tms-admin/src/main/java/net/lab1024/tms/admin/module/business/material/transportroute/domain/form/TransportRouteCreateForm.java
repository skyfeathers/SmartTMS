package net.lab1024.tms.admin.module.business.material.transportroute.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.dto.TransportRoutePathDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 业务资料-运输路线-新建
 *
 * @author lidoudou
 * @date 2022/7/12 下午2:30
 */
@Data
public class TransportRouteCreateForm {

    @ApiModelProperty("运输路线名称")
    @NotBlank(message = "运输路线名称不能为空")
    @Length(max = 200, message = "运输路线名称最多200字符")
    private String transportRouteName;

    @ApiModelPropertyEnum(desc = "运输类型", value = TransportationTypeEnum.class)
    @CheckEnum(message = "运输类型不正确", value = TransportationTypeEnum.class)
    @NotNull(message = "运输类型不能为空")
    private Integer transportRouteType;

    @ApiModelProperty("地点列表")
    private List<TransportRoutePathDTO> pathList;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "路线状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("里程")
    private BigDecimal mileage;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
