package net.lab1024.tms.admin.module.business.expiredcertificate.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 到期证件 VO
 *
 * @author Turbolisten
 * @date 2022/7/19 10:41
 */
@Data
public class ExpiredCertificateVO {

    private Long id;

    @ApiModelPropertyEnum(ExpiredCertificateModuleTypeEnum.class)
    private Integer moduleType;

    @ApiModelProperty("模块业务id")
    private Long moduleId;

    @ApiModelProperty("模块业务名称")
    private String moduleName;

    @ApiModelPropertyEnum(ExpiredCertificateTypeEnum.class)
    private Integer type;

    @ApiModelPropertyEnum(ExpiredCertificateStatusEnum.class)
    private Integer status;

    @ApiModelProperty("到期时间")
    private LocalDate expiredTime;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
