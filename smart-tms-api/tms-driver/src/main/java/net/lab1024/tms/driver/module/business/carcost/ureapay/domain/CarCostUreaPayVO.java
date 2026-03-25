package net.lab1024.tms.driver.module.business.carcost.ureapay.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车尿素支出 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/26 14:33
 */
@Data
public class CarCostUreaPayVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("油费支出表ID")
    private Long ureaPayId;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡号")
    @DataTracerFieldDoc("油卡号")
    private String oilCardNo;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    @DataTracerFieldDoc("分类名字")
    private String categoryName;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支出方式")
    @DataTracerFieldDoc("支出方式")
    @DataTracerFieldEnum(enumClass = CarCostCategoryPayModeEnum.class)
    private Integer payMode;

    @ApiModelProperty("支出金额")
    @DataTracerFieldDoc("支出金额")
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
