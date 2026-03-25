package net.lab1024.tms.fixedasset.module.business.asset.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产 实体类
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Data
@TableName("t_asset")
public class AssetEntity {

    /**
     * 资产ID
     */
    @TableId(type = IdType.AUTO)
    private Long assetId;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 所属分类
     */
    private Long categoryId;

    /**
     * 资产编号
     */
    private String assetNo;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 资产来源
     */
    private String sourceId;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 规格型号
     */
    private String model;

    /**
     * 设备序列号
     */
    private String serialId;

    /**
     * 单位
     */
    private String unit;

    /**
     * 存放位置
     */
    private Long locationId;

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 使用期限
     */
    private Integer monthCount;

    /**
     * 物品照片
     */
    private String attachment;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用部门
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long departmentId;

    /**
     * 使用人
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long userId;

    /**
     * 购买时间
     */
    private LocalDate purchaseTime;

    /**
     * 购买价格
     */
    private BigDecimal price;

    /**
     * 残值率
     */
    private BigDecimal residualValueRate;

    /**
     * 过期时间
     */
    private LocalDate expireTime;

    /**
     * 供应商联系人
     */
    private String supplierContactName;

    /**
     * 供应商联系人电话
     */
    private String supplierContactPhone;

    /**
     * 负责人
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long managerId;

    /**
     * 维保到期日
     */
    private LocalDate repairExpireTime;

    /**
     * 维保说明
     */
    private String supplierRemark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}