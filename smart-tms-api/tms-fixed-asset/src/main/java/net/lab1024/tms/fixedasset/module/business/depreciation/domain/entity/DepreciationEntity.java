package net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 折旧表
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:23
 */
@Data
@TableName("t_asset_depreciation")
public class DepreciationEntity {

    @TableId(type = IdType.AUTO)
    private Long depreciationId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    /**
     * 折旧编号
     */
    private String depreciationNo;

    /**
     * 计提日期
     */
    private LocalDate depreciationDate;

    private String remark;

    private Boolean deletedFlag;

    private Long createUserId;

    private String createUserName;

    private Long updateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
