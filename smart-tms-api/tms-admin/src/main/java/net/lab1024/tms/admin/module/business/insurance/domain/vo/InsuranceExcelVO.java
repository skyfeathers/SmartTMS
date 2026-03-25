package net.lab1024.tms.admin.module.business.insurance.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.insurance.domain.dto.InsuranceBaseDTO;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 保险基本信息
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:35
 */
@Data
public class InsuranceExcelVO{

    @Excel(name = "保单号", width = 20)
    private String policyNumber;

    @Excel(name = "保险类型", width = 20, replace = {"商业险_1", "交强险_2", "超赔险_3","雇主责任险_4", "车船税_5", "驾乘险_6"})
    private Integer insuranceType;

    @Excel(name = "保险金额", width = 20, type = 10, numFormat = "0.00")
    private BigDecimal insuranceAmount;

    @Excel(name = "保险单费用", width = 20, type = 10, numFormat = "0.00")
    private BigDecimal policyInsuranceAmount;

    @Excel(name = "投保时间", format = "yyyy-MM-dd", width = 20)
    private LocalDate effectDate;

    @Excel(name = "有效期至", format = "yyyy-MM-dd", width = 20)
    private LocalDate expireDate;

    @Excel(name = "登记日期", format = "yyyy-MM-dd", width = 20)
    private LocalDate enrollDate;

    @Excel(name = "备注", width = 20)
    private String remark;

    @Excel(name = "保险公司", width = 30)
    private String insuranceCompanyName;

    @Excel(name = "保险对象", width = 20)
    private String moduleName;

    @Excel(name = "创建人", width = 20)
    private String createUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd", width = 20)
    private LocalDateTime createTime;
}
