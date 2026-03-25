package net.lab1024.tms.admin.module.business.expiredcertificate.domain;

import lombok.Data;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 到期证件 删除
 *
 * @author Turbolisten
 * @date 2022/7/19 14:38
 */
@Data
public class ExpiredCertificateDelDTO {

    @NotNull(message = "模块类型不能为空")
    private ExpiredCertificateModuleTypeEnum moduleType;

    @NotNull(message = "模块id不能为空")
    private Long moduleId;

    private ExpiredCertificateTypeEnum type;
}
