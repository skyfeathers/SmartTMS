package net.lab1024.tms.admin.module.business.fleet.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 车队的基础信息
 *
 * @author lidoudou
 * @date 2022/6/27 下午3:47
 */
@Data
public class FleetBaseDTO {

    @ApiModelProperty("速记码")
    @DataTracerFieldDoc("速记码")
    @Length(max = 100, message = "速记码不能超过100字符")
    private String shorthandCode;

    @ApiModelProperty("车队名称")
    @DataTracerFieldDoc("车队名称")
    @NotBlank(message = "车队名称不能为空")
    private String fleetName;

    @ApiModelProperty("车队长名称")
    @DataTracerFieldDoc("车队长名称")
    private String captainName;

    @FieldEncrypt
    @ApiModelProperty("车队长联系方式")
    @DataTracerFieldDoc("车队长联系方式")
    private String captainPhone;

    @FieldEncrypt
    @ApiModelProperty("车队长身份证号")
    @DataTracerFieldDoc("车队长身份证号")
    private String captainIdCard;

    @ApiModelProperty("车队长身份证国徽面")
    @DataTracerFieldDoc("车队长身份证国徽面")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String captainIdCardFrontPic;

    @ApiModelProperty("车队长 人像面")
    @DataTracerFieldDoc("车队长身份证人像面")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String captainIdCardBackPic;

    @ApiModelProperty("负责人ID")
    private Long managerId;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    @DataTracerFieldDoc("省份")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("城市名称")
    @DataTracerFieldDoc("城市")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    @DataTracerFieldDoc("区县")
    private String districtName;
}
