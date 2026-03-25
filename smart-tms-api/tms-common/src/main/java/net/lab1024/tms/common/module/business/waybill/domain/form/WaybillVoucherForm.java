package net.lab1024.tms.common.module.business.waybill.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaybillVoucherForm {

    @ApiModelProperty("运单Id")
    @NotNull(message = "运单不能为空")
    private Long waybillId;

    @ApiModelPropertyEnum(WaybillVoucherTypeEnum.class)
    @NotNull(message = "凭证类型不能为空")
    private Integer type;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @NotNull(message = "附件不能为空")
    private String attachment;

    @ApiModelProperty("人车合影")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String sceneAttachment;

    @ApiModelProperty("磅单凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String poundListAttachment;

    @ApiModelProperty("磅单重量")
    private BigDecimal poundListQuantity;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("纬度")
    private BigDecimal longitude;

    @ApiModelProperty("经度")
    private BigDecimal latitude;

    @ApiModelProperty("上传时间")
    private LocalDateTime createTime;


}
