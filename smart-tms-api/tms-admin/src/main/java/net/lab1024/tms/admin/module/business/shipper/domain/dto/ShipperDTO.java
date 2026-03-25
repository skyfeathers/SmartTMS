package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperGradeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTagEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * [ 货主 ]
 *
 * @author yandanyang
 * @date 2020/7/29 18:16
 */
@Data
public class ShipperDTO {

    @ApiModelProperty("货主名称")
    @DataTracerFieldDoc("货主名称")
    @NotNull(message = "货主名称不能为空")
    @Length(max = 50, message = "货主名称不能超过50字符")
    private String consignor;

    @ApiModelProperty("货主简称")
    @DataTracerFieldDoc("货主简称")
    @NotNull(message = "货主简称不能为空")
    @Length(max = 50, message = "货主简称不能超过50字符")
    private String shortName;

    @ApiModelProperty("货主电话")
    @DataTracerFieldDoc("货主电话")
    @Length(max = 50, message = "货主电话不能超过50字符")
    private String shipperPhone;

    @ApiModelProperty("所属公司")
    @DataTracerFieldDoc("所属公司")
    @DataTracerFieldSql(relateColumn = "enterpriseId", relateDisplayColumn = "enterpriseName", relateMapper = EnterpriseDao.class)
    private Long enterpriseId;

    @ApiModelPropertyEnum(value = ShipperGradeEnum.class, desc = "货主等级")
    @DataTracerFieldDoc("货主等级")
    @DataTracerFieldEnum(enumClass = ShipperGradeEnum.class)
    private Integer grade;

    @ApiModelPropertyEnum(value = ShipperTagEnum.class, desc = "货主标签")
    @DataTracerFieldDoc("货主标签")
    @DataTracerFieldEnum(enumClass = ShipperTagEnum.class)
    private Integer tagType;

    @ApiModelProperty("传真")
    @DataTracerFieldDoc("传真")
    private String fax;

    @ApiModelPropertyEnum(value = ShipperTypeEnum.class, desc = "业务关系")
    @DataTracerFieldDoc("业务关系")
    @DataTracerFieldEnum(enumClass = ShipperTypeEnum.class)
    private List<Integer> shipperTypeList;

    @ApiModelPropertyEnum(value = ShipperNatureEnum.class, desc = "货主类型")
    @DataTracerFieldDoc("货主类型")
    @DataTracerFieldEnum(enumClass = ShipperNatureEnum.class)
    @NotNull(message = "货主类型")
    private Integer shipperNature;

    @ApiModelProperty("营业执照号")
    @DataTracerFieldDoc("营业执照号")
    private String consignorId;

    @ApiModelProperty("营业执照图片")
    @DataTracerFieldDoc("营业执照图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String consignorImage;

    @ApiModelProperty("法人姓名")
    @DataTracerFieldDoc("法人姓名")
    private String legalPersonName;

    @ApiModelProperty("法人身份证号")
    @DataTracerFieldDoc("法人身份证号")
    private String legalPersonIdNumber;

    @ApiModelProperty("法人电话")
    @DataTracerFieldDoc("法人电话")
    private String legalPersonPhone;

    @ApiModelProperty("是否需要开票")
    @NotNull(message = "是否需要开票不能为空")
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("税点")
    @DataTracerFieldDoc("税点")
    @DataTracerFieldBigDecimal
    @Max(value = 100, message = "最大值不能超过100")
    @Min(value = 0, message = "最小值不能小于0")
    @NotNull(message = "税点不能为空")
    private BigDecimal taxPoint;

    @ApiModelProperty("是否扣税")
    @DataTracerFieldDoc("是否扣税")
    @NotNull(message = "是否扣税不能为空")
    private Boolean deductTaxFlag;

    @ApiModelProperty("扣税比例")
    @DataTracerFieldDoc("扣税比例")
    @NotNull(message = "扣税比例不能为空")
    @Max(value = 100, message = "最大值不能超过100")
    @Min(value = 0, message = "最小值不能小于0")
    private BigDecimal deductTaxRatio;

    @ApiModelProperty("附件")
    @DataTracerFieldDoc("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("所在地区-数据字典（AREA）")
    @DataTracerFieldDoc("所在地区")
    @DataTracerFieldDict
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String area;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    @Length(max = 5000, message = "备注信息不能超过5000字符")
    private String remark;

    @ApiModelProperty("禁用状态")
    @DataTracerFieldDoc("禁用状态")
    @DataTracerFieldBoolean
    private Boolean disableFlag;

    @ApiModelProperty("助记码")
    @DataTracerFieldDoc("助记码")
    private String mnemonicCode;

    @ApiModelProperty("账期")
    @DataTracerFieldDoc("账期")
    private Integer accountPeriod;

    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty(value = "创建人id", hidden = true)
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;
}
