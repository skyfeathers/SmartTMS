package net.lab1024.tms.common.module.business.mobileapp.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;

import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2023/5/16 11:08 下午
 */
@Data
@TableName("t_mobile_app")
public class MobileAppEntity {


    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 平台类型
     *
     * @see MobileAppPlatformTypeEnum
     */
    private Integer platformType;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本更新描述
     */
    private String updateDesc;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdateFlag;

    /**
     * 是否是最新版
     */
    private Boolean newestFlag;

    /**
     * app文件key
     */
    private String appFile;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}