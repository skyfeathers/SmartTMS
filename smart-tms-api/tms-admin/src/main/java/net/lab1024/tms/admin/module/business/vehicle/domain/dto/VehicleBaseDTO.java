package net.lab1024.tms.admin.module.business.vehicle.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.OwnerTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.*;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/***
 * 车辆基础信息
 *
 * @author lidoudou
 * @date 2022/6/25 上午10:50
 */
@Data
public class VehicleBaseDTO {

    @ApiModelProperty("速记码")
    @DataTracerFieldDoc("速记码")
    @Length(max = 100, message = "速记码不能超过100字符")
    private String shorthand;

    @ApiModelProperty("车籍地-省份")
    private Integer province;

    @ApiModelProperty("车籍地-省份")
    @DataTracerFieldDoc("车籍地-省份")
    private String provinceName;

    @ApiModelProperty("车籍地-市")
    private Integer city;

    @ApiModelProperty(" 车籍地-市")
    @DataTracerFieldDoc("车籍地-市")
    private String cityName;

    @ApiModelProperty("车籍地-区")
    private Integer district;

    @ApiModelProperty("车籍地-区")
    @DataTracerFieldDoc("车籍地-区")
    private String districtName;

    @FieldEncrypt
    @ApiModelProperty("车牌号")
    @DataTracerFieldDoc("车牌号")
    @NotBlank(message = "车牌号不能为空")
    private String vehicleNumber;

    @ApiModelPropertyEnum(desc = "车牌颜色代码", value = VehiclePlateColorEnum.class)
    @DataTracerFieldDoc("车牌颜色")
    @DataTracerFieldEnum(enumClass = VehiclePlateColorEnum.class)
    private Integer vehiclePlateColorCode;

    @ApiModelProperty("车辆类型代码")
    @DataTracerFieldDoc("车辆类型")
    @DataTracerFieldDict
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String vehicleType;

    @ApiModelProperty("总质量")
    @DataTracerFieldDoc("总质量")
    @DataTracerFieldBigDecimal
    private BigDecimal grossMass;

    @ApiModelProperty("核定载质量")
    @DataTracerFieldDoc("核定载质量")
    @DataTracerFieldBigDecimal
    private BigDecimal vehicleTonnage;

    @ApiModelProperty("整备质量")
    @DataTracerFieldDoc("整备质量")
    @DataTracerFieldBigDecimal
    private BigDecimal curbWeight;

    @ApiModelProperty("牵引总质量")
    @DataTracerFieldDoc("牵引总质量")
    @DataTracerFieldBigDecimal
    private BigDecimal tractionWeight;

    @ApiModelProperty("外廓尺寸")
    @DataTracerFieldDoc("外廓尺寸")
    private String gabarite;

    @ApiModelProperty("所属人")
    @DataTracerFieldDoc("所属人")
    private String owner;

    @ApiModelPropertyEnum(desc = "所属人性质", value = OwnerTypeEnum.class)
    @DataTracerFieldDoc("所属人性质")
    @DataTracerFieldEnum(enumClass = OwnerTypeEnum.class)
    private Integer ownerType;

    @ApiModelProperty("车辆识别代码")
    @DataTracerFieldDoc("车辆识别代码")
    private String vin;

    @ApiModelProperty("车辆行驶证档案编号")
    @DataTracerFieldDoc("车辆行驶证档案编号")
    private String licenseNo;

    @ApiModelProperty("车辆能源类型")
    @DataTracerFieldDoc("车辆能源类型")
    private String vehicleEnergyType;

    @ApiModelProperty("发动机号")
    @DataTracerFieldDoc("发动机号")
    private String engineNumber;

    @ApiModelProperty("机动车登记证书编号")
    @DataTracerFieldDoc("机动车登记证书编号")
    private String vehicleRegistrationCertificateNo;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "经营方式1内管车 2挂靠车 3外派车")
    @DataTracerFieldDoc("经营方式")
    @DataTracerFieldEnum(enumClass = BusinessModeEnum.class)
    private Integer businessMode;

    @ApiModelProperty("挂车车号")
    @DataTracerFieldDoc("挂车车号")
    private Long bracketId;

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
    private LocalDate registerDate;

    @ApiModelProperty("发证日期")
    @DataTracerFieldDoc("发证日期")
    private LocalDate issueDate;

    @ApiModelProperty("到期日期")
    @DataTracerFieldDoc("到期日期")
    private LocalDate expireDate;

    @ApiModelProperty("使用性质")
    @DataTracerFieldDoc("使用性质")
    private String nature;

    @ApiModelProperty("行驶证副本-附件")
    @DataTracerFieldDoc("行驶证副本-附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String drivingLicenseEctypeAttachment;

    @ApiModelProperty("道路运输证号")
    @DataTracerFieldDoc("道路运输证号")
    private String roadTransportCertificateNumber;

    @ApiModelProperty("道路运输经营许可证号")
    @DataTracerFieldDoc("道路运输经营许可证号")
    private String roadTransportBusinessLicenseNumber;

    @ApiModelProperty("道路运输证-附件")
    @DataTracerFieldDoc("道路运输证-附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String roadTransportCertificateAttachment;

    @ApiModelProperty("道运证有效期开始日期")
    @DataTracerFieldDoc("道运证有效期开始日期")
    private LocalDate roadTransportCertificateStartDate;

    @ApiModelProperty("道路运输证到期时间")
    @DataTracerFieldDoc("道路运输证到期时间")
    private LocalDate roadTransportCertificateExpireDate;

    @ApiModelProperty("车辆审验到期时间")
    @DataTracerFieldDoc("车辆审验到期时间")
    private LocalDate vehicleAuditExpireDate;

    @ApiModelProperty("挂靠企业名称")
    @DataTracerFieldDoc("挂靠企业名称")
    private String relyEnterpriseName;

    @ApiModelProperty("挂靠企业协议附件")
    @DataTracerFieldDoc("挂靠企业协议附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String relyEnterpriseAttachment;

    @ApiModelProperty("挂靠到期时间")
    @DataTracerFieldDoc("挂靠到期时间")
    private LocalDate relyEnterpriseExpireDate;

    @ApiModelProperty("挂靠企业营业执照-附件")
    @DataTracerFieldDoc("挂靠企业营业执照-附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String relyEnterpriseBusinessLicenseAttachment;

    @ApiModelProperty("挂靠企业营业统一社会信用代码")
    @DataTracerFieldDoc("挂靠企业营业统一社会信用代码")
    private String relyEnterpriseUnifiedSocialCreditCode;

    @ApiModelProperty("挂靠企业-道路运输经营许可证号")
    @DataTracerFieldDoc("挂靠企业-道路运输经营许可证号")
    private String relyEnterpriseRoadTransportBusinessLicenseNumber;

    @ApiModelProperty("挂靠企业-道路运输经营许可证附件")
    @DataTracerFieldDoc("挂靠企业-道路运输经营许可证附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String relyEnterpriseRoadTransportBusinessLicenseAttachment;

    @ApiModelProperty("挂靠企业-道路运输经营许可证-发证日期")
    @DataTracerFieldDoc("挂靠企业-道路运输经营许可证-发证日期")
    private LocalDate relyEnterpriseRoadTransportBusinessLicenseIssueDate;

    @ApiModelProperty("挂靠企业-道路运输经营许可证-结束日期")
    @DataTracerFieldDoc("挂靠企业-道路运输经营许可证-结束日期")
    private LocalDate relyEnterpriseRoadTransportBusinessLicenseExpireDate;

    @ApiModelProperty("负责人")
    private Long managerId;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

    @ApiModelProperty("品牌型号")
    @DataTracerFieldDoc("品牌型号")
    private String model;

    @ApiModelProperty("是否经过NFT信息验证")
    private Boolean validateNftFlag;

    @ApiModelProperty("收款人")
    private String receiveUserName;

    @ApiModelProperty("收款银行")
    private String receiveBankName;

    @ApiModelProperty("收款账号")
    private String receiveBankNo;
}
