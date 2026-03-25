package net.lab1024.tms.fixedasset.module.business.asset.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 固定资产 新建表单
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Data
public class AssetAddForm {

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "所属分类", required = true)
    @NotNull(message = "所属分类不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "资产编号", required = true)
    private String assetNo;

    @ApiModelProperty(value = "资产名称", required = true)
    @NotBlank(message = "资产名称不能为空")
    private String assetName;

    @ApiModelProperty("资产来源")
    private String sourceId;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("设备序列号")
    private String serialId;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("存放位置")
    private Long locationId;

    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("使用期限")
    private Integer monthCount;

    @ApiModelProperty("物品照片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("使用部门")
    private Long departmentId;

    @ApiModelProperty("使用人")
    private Long userId;

    @ApiModelProperty("购入时间")
//    @NotNull(message = "购入时间不能为空")
    private LocalDate purchaseTime;

    @ApiModelProperty("过期时间")
    private LocalDate expireTime;

    @ApiModelProperty("供应商联系人")
    private String supplierContactName;

    @ApiModelProperty("供应商联系人电话")
    private String supplierContactPhone;

    @ApiModelProperty("负责人")
    private Long managerId;

    @ApiModelProperty("维保到期日")
    private LocalDate repairExpireTime;

    @ApiModelProperty("维保说明")
    private String supplierRemark;

    @ApiModelProperty("购买价格")
    @NotNull(message = "购买价格不能为空")
    private BigDecimal price;

    @ApiModelProperty("残值率")
    @NotNull(message = "残值率不能为空")
    private BigDecimal residualValueRate;

    @ApiModelProperty(value = "创建人ID", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}