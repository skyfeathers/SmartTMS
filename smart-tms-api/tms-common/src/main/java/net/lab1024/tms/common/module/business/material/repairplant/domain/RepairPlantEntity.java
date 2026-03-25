package net.lab1024.tms.common.module.business.material.repairplant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 维修厂 实体类
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:17
 */
@Data
@TableName("t_repair_plant")
public class RepairPlantEntity {

    @TableId(type = IdType.AUTO)
    private Integer repairPlantId;

    private Long enterpriseId;

    /**
     * 维修厂家名称
     */
    private String repairPlantName;

    /**
     * 联系方式
     */
    private String contactName;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 省份code
     */
    private Integer provinceCode;

    /**
     * 城市code
     */
    private Integer cityCode;

    /**
     * 地区code
     */
    private Integer areaCode;

    /**
     * 地区名称
     */
    private String addressArea;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 备注
     */
    private String remark;

    private Boolean disabledFlag;

    private Boolean deletedFlag;

    private String updateUserName;

    private String createUserName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
