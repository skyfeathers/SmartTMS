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
@TableName("t_expired_certificate")
public class ExpiredCertificateEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long enterpriseId;

    /**
     * 模块业务类型
     *
     * @see ExpiredCertificateModuleTypeEnum
     */
    private Integer moduleType;

    /**
     * 模块业务 id
     */
    private Long moduleId;

    /**
     * 模块业务名称
     */
    private String moduleName;

    /**
     * 证件类型
     *
     * @see ExpiredCertificateTypeEnum
     */
    private Integer type;

    /**
     * 到期状态
     *
     * @see ExpiredCertificateStatusEnum
     */
    private Integer status;

    /**
     * 到期时间
     */
    private LocalDate expiredTime;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
