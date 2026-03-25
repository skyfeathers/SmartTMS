package net.lab1024.tms.admin.module.business.etc.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.etc.constant.EtcTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @date 2022/6/30 上午10:52
 */
@Data
public class EtcBaseDTO {

    @ApiModelProperty("etc卡号")
    @DataTracerFieldDoc("ETC卡号")
    private String etcNo;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelPropertyEnum(desc = "类型", value = EtcTypeEnum.class)
    private Integer type;

    @ApiModelProperty("持卡人")
    private Long userId;

    @ApiModelProperty("持卡车")
    private Long useVehicleId;

    @ApiModelProperty("期初余额")
    @DataTracerFieldDoc("期初余额")
    private BigDecimal beginBalance;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;
}
