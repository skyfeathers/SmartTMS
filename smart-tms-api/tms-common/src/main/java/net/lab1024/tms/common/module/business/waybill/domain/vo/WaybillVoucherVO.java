package net.lab1024.tms.common.module.business.waybill.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运单凭证
 *
 * @author zhuoda
 * @Date 2022-07-13
 */
@Data
public class WaybillVoucherVO {

    @ApiModelProperty("凭证id")
    private Long waybillVoucherId;

    @ApiModelPropertyEnum(WaybillVoucherTypeEnum.class)
    private Integer type;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("人车合影")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String sceneAttachment;

    @ApiModelProperty("磅单凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String poundListAttachment;

    @ApiModelProperty("磅单重量")
    private BigDecimal poundListQuantity;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("纬度")
    private BigDecimal longitude;

    @ApiModelProperty("经度")
    private BigDecimal latitude;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
