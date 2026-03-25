package net.lab1024.tms.admin.module.business.repair.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/***
 * @author lidoudou
 * @date 2022/6/24 下午5:47
 */
@Data
public class RepairBaseDTO {

    @ApiModelProperty("模块ID")
    private Long moduleId;

    @ApiModelPropertyEnum(value = RepairModuleTypeEnum.class, desc = "模块类型")
    private Integer moduleType;

    @ApiModelProperty("维修人")
    @NotBlank(message = "维修人不能为空")
    private String repairPerson;

    @ApiModelProperty("维修厂家")
    @NotNull(message = "维修厂家不能为空")
    private Integer repairPlantId;

    @ApiModelProperty(value = "维修时间", example = "2023-01-01")
    private LocalDate repairDate;

    @ApiModelProperty("附件")
    @Length(max = 500, message = "附件不能超过500字符")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;
}
