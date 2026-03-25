package net.lab1024.tms.common.module.business.oa.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.EnterpriseTypeEnum;

import java.time.LocalDateTime;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:50
 */
@Data
@TableName("t_oa_enterprise")
public class EnterpriseEntity {

    /**
     * 企业ID
     */
    @TableId(type = IdType.AUTO)
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 域名列表(逗号分割)
     */
    private String domainName;

    /**
     * 是否是平台端账号
     */
    private Boolean platformFlag;

    /**
     * 企业logo
     */
    private String enterpriseLogo;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 道路运输经营许可证
     */
    private String networkFreightTransportCode;

    /**
     * 类型
     *
     * @see EnterpriseTypeEnum
     */
    private Integer type;

    /**
     * 网络货运接入key
     */
    private String accessKey;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市
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
     * 详细地址
     */
    private String address;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 企业简称
     */
    private String enterpriseShortName;

    /**
     * 企业简称
     */
    private String websiteName;

    /**
     * 企业介绍
     */
    private String websiteDesc;

    /**
     * 备案号
     */
    private String beiAnNo;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
