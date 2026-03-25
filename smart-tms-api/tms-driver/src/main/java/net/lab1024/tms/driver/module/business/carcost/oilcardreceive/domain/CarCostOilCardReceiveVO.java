package net.lab1024.tms.driver.module.business.carcost.oilcardreceive.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车-油卡收入 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/24 10:54
 */
@Data
public class CarCostOilCardReceiveVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("油卡收入表ID")
    private Long oilCardReceiveId;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡号")
    @DataTracerFieldDoc("油卡号")
    private String oilCardNo;

    @ApiModelProperty("收入金额")
    @DataTracerFieldDoc("充值金额")
    @DataTracerFieldBigDecimal
    private BigDecimal amount;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @DataTracerFieldDoc("附件")
    private String attachment;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    @DataTracerFieldDoc("审核备注")
    private String auditRemark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}