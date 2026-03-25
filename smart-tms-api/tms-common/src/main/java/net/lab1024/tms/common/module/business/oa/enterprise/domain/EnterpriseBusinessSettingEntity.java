package net.lab1024.tms.common.module.business.oa.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公司业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午2:31
 */
@Data
@TableName("t_oa_enterprise_business_setting")
public class EnterpriseBusinessSettingEntity {

    @TableId(type = IdType.AUTO)
    private Long businessSettingId;

    /**
     * 公司ID
     */
    private Long enterpriseId;

    /**
     * 业务设置名称
     */
    private String settingName;

    /**
     * 业务设置key
     */
    private String settingKey;

    /**
     * 业务设置内容
     */
    private String settingValue;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
