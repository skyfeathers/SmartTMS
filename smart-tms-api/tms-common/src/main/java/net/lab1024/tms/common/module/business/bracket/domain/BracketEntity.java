package net.lab1024.tms.common.module.business.bracket.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 挂车对象
 *
 * @author lidoudou
 * @date 2022/6/28 下午5:50
 */
@Data
@TableName("t_bracket")
public class BracketEntity {

    /**
     * 挂车ID
     */
    @TableId(type = IdType.AUTO)
    private Long bracketId;

    private Long enterpriseId;
    /**
     * @see BusinessModeEnum
     */
    private Integer businessMode;

    /**
     * 挂车编号
     */
    private String bracketNo;

    /**
     * 速记码
     */
    private String shorthandCode;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 载重（吨）
     */
    private BigDecimal tonnage;

    /**
     * 型号
     */
    private String type;

    /**
     * 使用性质
     */
    private String nature;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 行驶证图片
     */
    private String drivingLicensePic;

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
    private LocalDate registerTime;

    /**
     * 发证日期
     */
    private LocalDate issueTime;

    /**
     * 到期日期
     */
    private LocalDate expireTime;

    /**
     * 所属人
     */
    private String owner;

    /**
     * 所属人性质
     */
    private Integer ownerType;

    /**
     * 住址
     */
    private String address;

    /**
     * 车辆识别代码
     */
    private String vin;

    /**
     * 车牌颜色
     */
    private Integer plateColorCode;

    /**
     * 行驶证副本
     */
    private String drivingLicenseEctypePic;

    /**
     * 副本附件
     */
    private String drivingLicenseEctypeAttachment;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
