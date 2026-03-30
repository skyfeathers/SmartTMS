package net.lab1024.tms.driver.module.business.bracket.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.OwnerTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 基础信息
 *
 * @author lidoudou
 * @date 2022/6/29 上午11:04
 */
@Data
public class BracketBaseDTO {

    @ApiModelProperty("挂车编号")
    @DataTracerFieldDoc("挂车编号")
    @NotEmpty(message = "挂车车牌号不能为空")
    private String bracketNo;

    @ApiModelProperty("重量")
    @DataTracerFieldDoc("重量")
    @DataTracerFieldBigDecimal
    private BigDecimal weight;

    @ApiModelProperty("载重（kg）")
    @DataTracerFieldDoc("载重（kg）")
    @DataTracerFieldBigDecimal
    private BigDecimal tonnage;

    @ApiModelProperty("型号")
    @DataTracerFieldDoc("型号")
    private String type;

    @ApiModelProperty("使用性质")
    @DataTracerFieldDoc("使用性质")
    private String nature;

    @ApiModelProperty("行驶证图片")
    @DataTracerFieldDoc("行驶证图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicensePic;

    @ApiModelProperty("行驶证附件")
    @DataTracerFieldDoc("行驶证附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseAttachment;

    @ApiModelProperty("发证机关")
    @DataTracerFieldDoc("发证机关")
    private String issuingOrganizations;

    @ApiModelProperty("注册日期")
    @DataTracerFieldDoc("注册日期")
    private LocalDate registerTime;

    @ApiModelProperty("发证日期")
    @DataTracerFieldDoc("注册日期")
    private LocalDate issueTime;

    @ApiModelProperty("到期日期")
    @DataTracerFieldDoc("注册日期")
    private LocalDate expireTime;

    @ApiModelProperty("行驶证副本")
    @DataTracerFieldDoc("行驶证副本")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseEctypePic;

    @ApiModelProperty("副本附件")
    @DataTracerFieldDoc("副本附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseEctypeAttachment;

    @ApiModelProperty("道路运输证号")
    @DataTracerFieldDoc("道路运输证号")
    private String roadTransportCertificateNumber;

    @ApiModelProperty("道路运输证到期时间")
    @DataTracerFieldDoc("道路运输证到期时间")
    private LocalDate roadTransportCertificateExpireDate;

    @ApiModelProperty("车辆审验到期时间")
    @DataTracerFieldDoc("车辆审验到期时间")
    private LocalDate auditExpireTime;

    @ApiModelPropertyEnum(desc = "状态", value = AuditStatusEnum.class)
    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelProperty("所属人")
    @DataTracerFieldDoc("所属人")
    private String owner;

    @ApiModelPropertyEnum(desc = "所属人性质", value = OwnerTypeEnum.class)
    @DataTracerFieldDoc("所属人性质")
    @DataTracerFieldEnum(enumClass = OwnerTypeEnum.class)
    private Integer ownerType;

    @ApiModelProperty("住址")
    @DataTracerFieldDoc("住址")
    private String address;

    @ApiModelProperty("车辆识别代码")
    @DataTracerFieldDoc("车辆识别代码")
    private String vin;

    @ApiModelPropertyEnum(desc = "车牌颜色代码", value = VehiclePlateColorEnum.class)
    @DataTracerFieldDoc("车牌颜色")
    @DataTracerFieldEnum(enumClass = VehiclePlateColorEnum.class)
    private Integer plateColorCode;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

}
