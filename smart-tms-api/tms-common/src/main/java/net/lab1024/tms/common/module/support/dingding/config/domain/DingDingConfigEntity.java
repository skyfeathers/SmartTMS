package net.lab1024.tms.common.module.support.dingding.config.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 钉钉企业配置
 *
 * @author lidoudou
 * @date 2023/4/20 下午4:48
 */
@Data
@TableName("t_ding_ding_config")
public class DingDingConfigEntity {
    /**
     * 企业ID
     */
    @TableId(type = IdType.INPUT)
    private Long enterpriseId;

    /**
     * 钉钉企业ID
     */
    private String corpId;

    /**
     * 应用Key
     */
    private String appKey;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 加密key
     */
    private String aesKey;

    /**
     * 签名
     */
    private String aesToken;

    /**
     * 钉钉企业对应的部门id
     */
    private Long departmentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
