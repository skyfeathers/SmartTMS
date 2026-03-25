package net.lab1024.tms.driver.module.business.driver.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

/***
 * 银行卡详细信息
 *
 * @author zhaoxinyang
 * @date 2023/09/01 15:41
 */
@Data
public class DriverBankDetailVO extends DriverBankVO {

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String bankCardFrontAttachment;

    @ApiModelProperty("是否为纳税人信息")
    private String taxpayerFlag;

}
