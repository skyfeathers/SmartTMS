package net.lab1024.tms.common.module.business.expiredcertificate.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 到期证件 实体类
 *
 * @author Turbolisten
 * @date 2022/7/19 10:41
 */
@Data
@TableName("t_expire_reminder_time")
public class ExpiredReminderTimeEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long enterpriseId;

    private String config;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
