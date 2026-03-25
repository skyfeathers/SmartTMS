package net.lab1024.tms.fixedasset.module.business.repair.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产-维修登记 实体类
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
@TableName("t_asset_repair")
public class RepairEntity {

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long repairId;

    /**
     * 维修单号
     */
    private String repairCode;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 维修厂家
     */
    private String repairCompany;

    /**
     * 单据状态
     */
    private Integer status;

    /**
     * 业务日期
     */
    private LocalDate businessDate;

    /**
     * 维修内容
     */
    private String content;

    /**
     * 申请维修人员
     */
    private Long applyUserId;

    /**
     * 维修花费
     */
    private BigDecimal repairCost;

    /**
     * 备注
     */
    private String remark;

    /**
     * 维修配件附件
     */
    private String mountingsFiles;

    /**
     * 发票附件
     */
    private String invoiceFiles;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}