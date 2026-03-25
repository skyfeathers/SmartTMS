package net.lab1024.tms.driver.module.business.driver.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

/**
 * 司机纳税登记人
 *
 * @author lidoudou
 * @date 2022/8/25 下午5:38
 */
@Data
public class DriverTaxRegisterForm {

    @DataTracerFieldDoc("姓名")
    @ApiModelProperty("姓名")
    private String name;

    @DataTracerFieldDoc("纳税人身份证号")
    @ApiModelProperty("纳税人身份证号")
    private String idCard;

    @DataTracerFieldDoc("纳税人手机号")
    @ApiModelProperty("纳税人手机号")
    private String phone;

    @DataTracerFieldDoc("纳税人地址")
    @ApiModelProperty("纳税人地址")
    private String address;

    @ApiModelProperty("纳税人身份证正面")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String idCardFrontAttachment;

}
