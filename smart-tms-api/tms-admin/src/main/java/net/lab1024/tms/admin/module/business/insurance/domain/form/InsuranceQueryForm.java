package net.lab1024.tms.admin.module.business.insurance.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;

import java.time.LocalDate;
import java.util.List;

/***
 * 保险列表查询dto
 *
 * @author lidoudou
 * @date 2022/6/21 下午4:04
 */
@Data
public class InsuranceQueryForm extends PageParam {

    @ApiModelProperty("保单号/保险对象名称")
    private String keyWord;

    @ApiModelPropertyEnum(value = InsuranceTypeEnum.class, desc = "保险类型")
    private Integer insuranceType;

    @ApiModelPropertyEnum(value = InsuranceModuleTypeEnum.class, desc = "保险对象类型")
    private Integer moduleType;

    @ApiModelProperty("保险对象ID")
    private Long moduleId;

    @ApiModelProperty(value = "保险对象ID列表")
    private List<Long> moduleIdList;

    @ApiModelProperty("保险公司")
    private String insuranceCompanyCode;

    @ApiModelProperty(value = "有效期开始时间", example = "2022-01-01")
    private LocalDate startDate;

    @ApiModelProperty(value = "有效期结束时间", example = "2022-01-01")
    private LocalDate endDate;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartDate;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndDate;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

}
