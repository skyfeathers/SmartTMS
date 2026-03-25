package net.lab1024.tms.admin.module.business.bracket.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 查询
 *
 * @author lidoudou
 * @date 2022/6/28 下午5:14
 */
@Data
public class BracketQueryForm extends PageParam {

    @ApiModelProperty("挂车车牌号/速记码/创建人")
    private String keyWords;

    @ApiModelProperty("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty("负责人")
    private Long managerId;

    @ApiModelProperty("所属企业ID")
    private Long enterpriseId;

    @ApiModelProperty("速记码")
    private String shorthandCode;

    @ApiModelProperty("型号")
    private String type;

    @ApiModelProperty("重量")
    private BigDecimal weightStart;

    @ApiModelProperty("重量")
    private BigDecimal weightEnd;

    @ApiModelProperty("载重")
    private BigDecimal tonnageStart;

    @ApiModelProperty("载重")
    private BigDecimal tonnageEnd;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer createUserType;

    @ApiModelProperty(value = "创建人名字")
    private String createUserName;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty(value = "创建时间-开始时间", example = "2022-01-01")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间-结束时间", example = "2022-01-01")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "商业险到期-开始时间", example = "2022-01-01")
    private LocalDate commercialInsuranceStartTime;

    @ApiModelProperty(value = "商业险到期-结束时间", example = "2022-01-01")
    private LocalDate commercialInsuranceEndTime;

    @ApiModelProperty(value = "交强险到期-开始时间", example = "2022-01-01")
    private LocalDate compulsoryTrafficStartTime;

    @ApiModelProperty(value = "交强险到期-结束时间", example = "2022-01-01")
    private LocalDate compulsoryTrafficEndTime;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "业务类型")
    private Integer businessMode;

    @ApiModelProperty(hidden = true)
    private List<Long> excludeIdList;

    @ApiModelProperty(hidden = true)
    private List<Long> includeIdList;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

}