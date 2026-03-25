package net.lab1024.tms.admin.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.bracket.domain.dto.BracketBaseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 列表展示
 *
 * @author lidoudou
 * @date 2022/6/29 上午11:40
 */
@Data
public class BracketListVO extends BracketBaseDTO {

    @ApiModelProperty("挂车ID")
    private Long bracketId;

    @ApiModelProperty("负责人姓名")
    @DataTracerFieldDoc("负责人")
    private String managerName;

    @ApiModelProperty("所属企业名称")
    private String enterpriseName;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer createUserType;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("商业险到期时间")
    private LocalDate commercialExpireTime;

    @ApiModelProperty("交强险到期时间")
    private LocalDate compulsoryTrafficExpireTime;
}
