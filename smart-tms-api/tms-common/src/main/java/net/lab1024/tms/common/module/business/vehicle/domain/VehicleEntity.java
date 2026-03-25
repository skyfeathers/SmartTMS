package net.lab1024.tms.common.module.business.vehicle.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.OwnerTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 车辆
 *
 * @author lidoudou
 * @date 2022/6/24 下午5:37
 */
@Data
@TableName("t_vehicle")
public class VehicleEntity {
    /**
     * 车辆ID
     */
    @TableId(type = IdType.AUTO)
    private Long vehicleId;

    private Long enterpriseId;
    /**
     * @see BusinessModeEnum
     */
    private Integer businessMode;

    /**
     * 速记码
     */
    private String shorthand;

    /**
     * 车牌号
     */
    private String vehicleNumber;

    /**
     * 车籍地-省份
     */
    private Integer province;

    /**
     * 车籍地-省份名称
     */
    private String provinceName;

    /**
     * 车籍地-市
     */
    private Integer city;

    /**
     * 车籍地-市名称
     */
    private String cityName;

    /**
     * 车籍地-区县
     */
    private Integer district;

    /**
     * 车籍地-区县名称
     */
    private String districtName;

    /**
     * 车牌颜色代码
     *
     * @see VehiclePlateColorEnum
     */
    private Integer vehiclePlateColorCode;
    /**
     * 车辆类型代码
     */
    private String vehicleType;
    /**
     * 总质量
     */
    private BigDecimal grossMass;
    /**
     * 整备质量
     */
    private BigDecimal curbWeight;
    /**
     * 核定载质量
     */
    private BigDecimal vehicleTonnage;
    /**
     * 牵引总质量
     */
    private BigDecimal tractionWeight;
    /**
     * 外廓尺寸
     */
    private String gabarite;
    /**
     * 所属人
     */
    private String owner;
    /**
     * 所属人性质
     *
     * @see OwnerTypeEnum
     */
    private Integer ownerType;
    /**
     * 车辆识别代码
     */
    private String vin;
    /**
     * 车辆行驶证档案编号
     */
    private String licenseNo;
    /**
     * 车辆能源类型
     */
    private String vehicleEnergyType;
    /**
     * 发动机号
     */
    private String engineNumber;
    /**
     * 机动车登记证书编号
     */
    private String vehicleRegistrationCertificateNo;

    /**
     * 挂车车号
     */
    private Long bracketId;
    /**
     * 行驶证附件
     */
    private String drivingLicenseAttachment;
    /**
     * 发证机关
     */
    private String issuingOrganizations;
    /**
     * 注册日期
     */
    private LocalDate registerDate;
    /**
     * 发证日期
     */
    private LocalDate issueDate;
    /**
     * 到期日期
     */
    private LocalDate expireDate;
    /**
     * 行驶证副本-附件
     */
    private String drivingLicenseEctypeAttachment;

    /**
     * 使用性质
     */
    private String nature;

    /**
     * 道路运输证号
     */
    private String roadTransportCertificateNumber;

    /**
     * 道路运输经营许可证号
     */
    private String roadTransportBusinessLicenseNumber;
    /**
     * 道路运输证-附件
     */
    private String roadTransportCertificateAttachment;

    /**
     * 道运证有效期开始日期
     */
    private LocalDate roadTransportCertificateStartDate;
    /**
     * 道路运输证到期时间
     */
    private LocalDate roadTransportCertificateExpireDate;
    /**
     * 车辆审验到期时间
     */
    private LocalDate vehicleAuditExpireDate;
    /**
     * 挂靠企业名称
     */
    private String relyEnterpriseName;
    /**
     * 挂靠企业协议附件
     */
    private String relyEnterpriseAttachment;
    /**
     * 挂靠到期时间
     */
    private LocalDate relyEnterpriseExpireDate;
    /**
     * 挂靠企业营业执照-附件
     */
    private String relyEnterpriseBusinessLicenseAttachment;
    /**
     * 挂靠企业营业统一社会信用代码
     */
    private String relyEnterpriseUnifiedSocialCreditCode;
    /**
     * 挂靠企业-道路运输经营许可证号
     */
    private String relyEnterpriseRoadTransportBusinessLicenseNumber;
    /**
     * 挂靠企业-道路运输经营许可证附件
     */
    private String relyEnterpriseRoadTransportBusinessLicenseAttachment;
    /**
     * 挂靠企业-道路运输经营许可证-发证日期
     */
    private LocalDate relyEnterpriseRoadTransportBusinessLicenseIssueDate;
    /**
     * 挂靠企业-道路运输经营许可证-结束日期
     */
    private LocalDate relyEnterpriseRoadTransportBusinessLicenseExpireDate;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 品牌型号
     */
    private String model;

    /**
     * 收款人
     */
    private String receiveUserName;

    /**
     * 收款银行
     */
    private String receiveBankName;

    /**
     * 收款账号
     */
    private String receiveBankNo;

    /**
     * 审核状态 1待审核  2审核通过  3审核驳回
     *
     * @see AuditStatusEnum
     */
    private Integer auditStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 禁用状态：0没禁用，1禁用
     */
    private Boolean disabledFlag;
    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    private Long createUserId;

    private String createUserName;

    /**
     * 创建人类型
     */
    private Integer createUserType;

    /**
     * 创建人类型描述
     */
    private String createUserTypeDesc;

    private Long updateUserId;

    private String updateUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
