package net.lab1024.tms.admin.module.business.expiredcertificate.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 到期证件 分页查询
 *
 * @author Turbolisten
 * @date 2022/7/19 10:41
 */
@Data
public class ExpiredCertificateQueryForm extends PageParam {

    @ApiModelPropertyEnum(ExpiredCertificateModuleTypeEnum.class)
    @CheckEnum(value = ExpiredCertificateModuleTypeEnum.class, message = "模块类型错误")
    private Integer moduleType;

    @ApiModelProperty("搜索")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @ApiModelPropertyEnum(ExpiredCertificateTypeEnum.class)
    @CheckEnum(value = ExpiredCertificateTypeEnum.class, message = "证件类型错误")
    private Integer type;

    @ApiModelPropertyEnum(ExpiredCertificateStatusEnum.class)
    @CheckEnum(value = ExpiredCertificateStatusEnum.class, message = "到期状态错误")
    private Integer status;

    @ApiModelProperty("开始时间")
    private LocalDate startDate;

    @ApiModelProperty("截止时间")
    private LocalDate endDate;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
