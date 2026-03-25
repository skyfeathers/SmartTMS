package net.lab1024.tms.admin.module.business.expiredcertificate.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 到期证件 到期提醒时间 更新
 *
 * @author Turbolisten
 * @date 2022/7/19 15:12
 */
@Data
public class ExpiredCertificateReminderTimeDTO {

    @NotEmpty(message = "设置不能为空")
    @Valid
    private List<Type> typeList;

    @ApiModelProperty(hidden = true)
    private String updateName;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    @Data
    public static class Type {

        @ApiModelPropertyEnum(ExpiredCertificateTypeEnum.class)
        @CheckEnum(value = ExpiredCertificateTypeEnum.class, required = true, message = "证件类型错误")
        private Integer type;

        @ApiModelProperty("天数")
        @NotNull(message = "天数不能为空")
        @Range(min = 0, message = "到期天数最小0")
        private Integer days;
    }


}
