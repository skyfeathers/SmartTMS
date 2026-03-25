package net.lab1024.tms.common.module.business.driver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.common.enumeration.GenderEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverExpireEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 司机表
 *
 * @author lidoudou
 * @date 2022/6/21 上午09:44
 */
@Data
@TableName("t_driver")
public class DriverEntity {

    /**
     * 司机ID
     */
    @TableId(type = IdType.AUTO)
    private Long driverId;

    private Long enterpriseId;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 司机手机号
     */
    private String telephone;

    /**
     * @see BusinessModeEnum
     */
    private Integer businessMode;
    /**
     * 速记码
     */
    private String shorthandCode;

    /**
     * 紧急联系人姓名
     */
    private String emergencyContactName;
    /**
     * 紧急联系人手机号
     */
    private String emergencyContactPhone;
    /**
     * 家庭住址
     */
    private String homeAddress;
    /**
     * 司机大头照
     */
    private String photo;
    /**
     * 民族
     */
    private String national;
    /**
     * 性别
     *
     * @see GenderEnum
     */
    private Integer gender;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 身份证号
     */
    private String drivingLicense;
    /**
     * 身份证国徽面图片
     */
    private String idCardFrontPic;
    /**
     * 身份证人像面图片
     */
    private String idCardBackPic;
    /**
     * 签证机关
     */
    private String issue;
    /**
     * 身份证是否长期有效  0否  true是
     */
    private Boolean idCardEndlessFlag;
    /**
     * 身份证有效期开始日期
     */
    private LocalDate idCardEffectiveStartDate;
    /**
     * 身份证有效期结束日期
     */
    private LocalDate idCardEffectiveEndDate;
    /**
     * 驾驶证号
     */
    private String drivingLicenseNo;
    /**
     * 驾驶证正本图
     */
    private String drivingLicenseFrontPic;
    /**
     * 驾驶证副本图
     */
    private String drivingLicenseBackPic;

    /**
     * 首次驾照签发日期
     */
    private LocalDate licenseFirstGetDate;
    /**
     * 驾驶证有效期自
     */
    private LocalDate validPeriodFrom;
    /**
     * 驾驶证有效期至
     */
    private LocalDate validPeriodTo;
    /**
     * 驾驶证是否长期有效 0否 true是
     */
    private Boolean drivingLicenseEndlessFlag;
    /**
     * 驾驶证准驾车型
     *
     * @see VehicleClassEnum
     */
    private Integer vehicleClass;
    /**
     * 驾驶证发证机关
     */
    private String issuingOrganizations;
    /**
     * 从业资格证图片
     */
    private String qualificationCertificatePic;
    /**
     * 从业资格证号
     */
    private String qualificationCertificate;
    /**
     * 从业资格证有效期自
     */
    private LocalDate qualificationCertificateStartDate;
    /**
     * 从业资格证有效期到
     */
    private LocalDate qualificationCertificateEndDate;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 备注
     */
    private String remark;
    /**
     * 禁用标志 1激活 2禁用
     *
     * @see DriverStatusEnum
     */
    private Integer status;
    /**
     * 审核状态 1待审核  2审核通过  3审核驳回
     *
     * @see AuditStatusEnum
     */
    private Integer auditStatus;
    /**
     * 审核备注
     */
    private String auditRemark;
    /**
     * 身份证证到期状态(默认 0未到期  1已到期)
     *
     * @see DriverExpireEnum
     */
    private Integer idCardLicenseStatus;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 创建人ID
     */
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
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新人ID
     */
    private Long updateUserId;

    private String updateUserName;
}
