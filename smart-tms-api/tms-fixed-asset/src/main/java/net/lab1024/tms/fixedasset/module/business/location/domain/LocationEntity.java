package net.lab1024.tms.fixedasset.module.business.location.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("t_location")
public class LocationEntity {

    /**
     * 位置ID
     */
    @TableId(type = IdType.AUTO)
    private Long locationId;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 位置名称
     */
    private String locationName;

    /**
     * 存放类型
     */
    private String type;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 备注
     */
    private String remark;

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
