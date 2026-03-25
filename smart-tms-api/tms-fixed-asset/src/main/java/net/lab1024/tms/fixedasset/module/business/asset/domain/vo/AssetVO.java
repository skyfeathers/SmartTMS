package net.lab1024.tms.fixedasset.module.business.asset.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDict;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产 列表VO
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Data
public class AssetVO {

    private Long assetId;

    @ApiModelProperty(value = "所属公司")
    private Long enterpriseId;

    @DataTracerFieldDoc("所属公司")
    @ApiModelProperty(value = "所属公司 - 名称")
    private String enterpriseName;

    @ApiModelProperty(value = "所属分类")
    private Long categoryId;

    @DataTracerFieldDoc("所属分类")
    @ApiModelProperty(value = "所属分类 - 名称")
    private String categoryName;

    @DataTracerFieldDoc("资产编号")
    @ApiModelProperty(value = "资产编号")
    private String assetNo;

    @DataTracerFieldDoc("资产名称")
    @ApiModelProperty(value = "资产名称 - 名称")
    private String assetName;

    @DataTracerFieldDoc("资产来源")
    @DataTracerFieldDict
    @ApiModelProperty("资产来源-数据字典（ASSET-SOURCE）")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String sourceId;

    @DataTracerFieldDoc("品牌")
    @ApiModelProperty(value = "品牌")
    private String brand;

    @DataTracerFieldDoc("规格型号")
    @ApiModelProperty(value = "规格型号")
    private String model;

    @DataTracerFieldDoc("设备序列号")
    @ApiModelProperty(value = "设备序列号")
    private String serialId;

    @DataTracerFieldDoc("单位")
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "存放位置")
    private Long locationId;

    @DataTracerFieldDoc("存放位置")
    @ApiModelProperty(value = "存放位置 - 名称")
    private String locationName;

    @DataTracerFieldDoc("供应商")
    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @DataTracerFieldDoc("使用期限（月）")
    @ApiModelProperty(value = "使用期限")
    private Integer monthCount;

    @ApiModelProperty(value = "物品照片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelPropertyEnum(value = AssetStatusEnum.class, desc = "状态")
    private Integer status;

    @ApiModelProperty("使用部门")
    private Long departmentId;

    @DataTracerFieldDoc("使用部门")
    @ApiModelProperty("使用部门 - 名称")
    private String departmentName;

    @ApiModelProperty("使用人")
    private Long userId;

    @DataTracerFieldDoc("使用人")
    @ApiModelProperty("使用人 - 名称")
    private String userName;

    @DataTracerFieldDoc("购入时间")
    @ApiModelProperty("购入时间")
    private LocalDate purchaseTime;

    @DataTracerFieldDoc("购买价格")
    @ApiModelProperty("购买价格")
    private BigDecimal price;

    @ApiModelProperty("残值率")
    private BigDecimal ResidualValueRate;

    @DataTracerFieldDoc("过期时间")
    @ApiModelProperty("过期时间")
    private LocalDate expireTime;

    @DataTracerFieldDoc("供应商联系人")
    @ApiModelProperty("供应商联系人")
    private String supplierContactName;

    @DataTracerFieldDoc("供应商联系电话")
    @ApiModelProperty("供应商联系人电话")
    private String supplierContactPhone;

    @ApiModelProperty("负责人")
    private Long managerId;

    @DataTracerFieldDoc("负责人")
    @ApiModelProperty("负责人 - 名称")
    private String managerName;

    @DataTracerFieldDoc("维保到期日")
    @ApiModelProperty("维保到期日")
    private LocalDate repairExpireTime;

    @ApiModelProperty("维保说明")
    private String supplierRemark;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}