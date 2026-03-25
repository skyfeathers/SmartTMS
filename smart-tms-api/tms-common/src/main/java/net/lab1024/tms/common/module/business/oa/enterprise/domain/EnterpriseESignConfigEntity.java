package net.lab1024.tms.common.module.business.oa.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OA企业e签宝配置模块
 *
 * @author lihaifan
 * @date 2022/9/26 16:06
 */
@Data
@TableName("t_oa_enterprise_esign_config")
public class EnterpriseESignConfigEntity {
    /**
     * 企业ID
     */
    @TableId(type = IdType.INPUT)
    private Long enterpriseId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
