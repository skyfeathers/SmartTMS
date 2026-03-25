package net.lab1024.tms.driver.module.business.driver.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/***
 * 司机基本信息
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:07
 */
@Data
public class DriverBaseForm {

    @ApiModelProperty("司机姓名")
    @DataTracerFieldDoc("司机姓名")
    @NotBlank(message = "司机姓名不能为空")
    private String driverName;

    @ApiModelProperty("司机手机号")
    @DataTracerFieldDoc("司机手机号")
    @NotBlank(message = "司机手机号不能为空")
    private String telephone;

    @ApiModelProperty("身份证号")
    @DataTracerFieldDoc("身份证号")
    private String drivingLicense;

    @ApiModelProperty("紧急联系人姓名")
    @DataTracerFieldDoc("紧急联系人姓名")
    private String emergencyContactName;

    @ApiModelProperty("紧急联系人手机号")
    @DataTracerFieldDoc("紧急联系人手机号")
    private String emergencyContactPhone;

    @ApiModelProperty("家庭住址")
    @DataTracerFieldDoc("家庭住址")
    private String homeAddress;

    @ApiModelProperty("照片")
    @DataTracerFieldDoc("照片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String photo;

    @ApiModelProperty("民族")
    @DataTracerFieldDoc("民族")
    private String national;

    @ApiModelProperty("性别")
    @DataTracerFieldDoc("性别")
    private Integer gender;

    @ApiModelProperty("出生日期")
    @DataTracerFieldDoc("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("身份证国徽面图片")
    @DataTracerFieldDoc("身份证国徽面图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String idCardFrontPic;

    @ApiModelProperty("身份证人像面图片")
    @DataTracerFieldDoc("身份证人像面图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String idCardBackPic;

    @ApiModelProperty("签证机关")
    @DataTracerFieldDoc("签证机关")
    private String issue;

    @ApiModelProperty("身份证是否长期有效  false否  true是")
    @DataTracerFieldDoc("身份证是否长期有效")
    private Boolean idCardEndlessFlag;

    @ApiModelProperty("身份证有效期开始日期")
    @DataTracerFieldDoc("身份证有效期开始日期")
    private LocalDate idCardEffectiveStartDate;

    @ApiModelProperty("身份证有效期结束日期")
    @DataTracerFieldDoc("身份证有效期结束日期")
    private LocalDate idCardEffectiveEndDate;

    @ApiModelProperty("驾驶证号")
    @DataTracerFieldDoc("驾驶证号")
    private String drivingLicenseNo;

    @ApiModelProperty("驾驶证正本图")
    @DataTracerFieldDoc("驾驶证正本图")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseFrontPic;

    @ApiModelProperty("驾驶证副本图")
    @DataTracerFieldDoc("驾驶证副本图")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseBackPic;

    @ApiModelProperty("首次驾照签发日期")
    @DataTracerFieldDoc("首次驾照签发日期")
    private LocalDate licenseFirstGetDate;

    @ApiModelProperty("驾驶证有效期自")
    @DataTracerFieldDoc("驾驶证有效期自")
    private LocalDate validPeriodFrom;

    @ApiModelProperty("驾驶证有效期至")
    @DataTracerFieldDoc("驾驶证有效期至")
    private LocalDate validPeriodTo;

    @ApiModelProperty("驾驶证是否长期有效 false否true是")
    @DataTracerFieldDoc("驾驶证是否长期有效")
    private Boolean drivingLicenseEndlessFlag;

    @ApiModelPropertyEnum(value = VehicleClassEnum.class,desc = "驾驶证准驾车型")
    @DataTracerFieldDoc("驾驶证准驾车型")
    @DataTracerFieldEnum(enumClass = VehicleClassEnum.class)
    @CheckEnum(value = VehicleClassEnum.class,message = "驾驶证准驾车型错误")
    private Integer vehicleClass;

    @ApiModelProperty("驾驶证发证机关")
    @DataTracerFieldDoc("驾驶证发证机关")
    private String issuingOrganizations;

    @ApiModelProperty("从业资格证图片")
    @DataTracerFieldDoc("从业资格证图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String qualificationCertificatePic;

    @ApiModelProperty("从业资格证号")
    @DataTracerFieldDoc("从业资格证号")
    private String qualificationCertificate;

    @ApiModelProperty(value = "从业资格证有效期自", example = "2022-01-01")
    @DataTracerFieldDoc("从业资格证有效期自")
    private LocalDate qualificationCertificateStartDate;

    @ApiModelProperty(value = "从业资格证有效期到", example = "2022-01-01")
    @DataTracerFieldDoc("从业资格证有效期到")
    private LocalDate qualificationCertificateEndDate;

    @ApiModelProperty("税务登记人")
    private DriverTaxRegisterForm taxRegister;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市")
    private Integer city;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

}
