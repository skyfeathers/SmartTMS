package net.lab1024.tms.common.module.business.expiredcertificate.domain;

import lombok.Data;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


/**
 * 到期证件 添加
 *
 * @author Turbolisten
 * @date 2022/7/19 10:41
 */
@Data
public class ExpiredCertificateAddDTO {

    @NotNull(message = "模块类型不能为空")
    private ExpiredCertificateModuleTypeEnum moduleType;

    @NotNull(message = "模块id不能为空")
    private Long moduleId;

    @NotBlank(message = "模块业务名称不能为空")
    @Length(max = 50, message = "模块业务名称最多50字符")
    private String moduleName;

    @NotNull(message = "证件类型不能为空")
    private ExpiredCertificateTypeEnum type;

    @NotNull(message = "到期时间不能为空")
    private LocalDate expiredTime;

    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
