package net.lab1024.tms.common.module.business.fleet.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import java.time.LocalDateTime;

/***
 *
 *
 * @author lidoudou
 * @date 2022/6/27 下午3:31
 */
@Data
@TableName("t_fleet")
public class FleetEntity {

    @TableId(type = IdType.AUTO)
    private Long fleetId;

    private Long enterpriseId;

    /**
     * 速记码
     */
    private String shorthandCode;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 车队长名称
     */
    private String captainName;

    /**
     * 车队长联系方式
     */
    private String captainPhone;

    /**
     * 车队长身份证号
     */
    private String captainIdCard;

    /**
     * 车队长身份证国徽面
     */
    private String captainIdCardFrontPic;

    /**
     * 车队长 人像面
     */
    private String captainIdCardBackPic;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市
     */
    private Integer city;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县
     */
    private Integer district;

    /**
     * 区县名称
     */
    private String districtName;

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
     * 负责人ID
     */
    private Long managerId;

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
