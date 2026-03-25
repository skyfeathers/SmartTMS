package net.lab1024.tms.fixedasset.module.business.scrap.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 固定资产-报废 实体类
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
@TableName("t_asset_scrap")
public class ScrapEntity {

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long scrapId;

    /**
     * 维修单号
     */
    private String scrapCode;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 报废时间
     */
    private LocalDateTime scrapTime;


    /**
     * 单据状态
     */
    private Integer status;

    /**
     * 说明
     */
    private String scrapExplain;

    /**
     * 申请维修人员
     */
    private Long applyUserId;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}