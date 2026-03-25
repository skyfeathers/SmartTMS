package net.lab1024.tms.common.module.business.material.businesstype;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
@TableName("t_material_business_type")
public class BusinessTypeEntity {

    /**
     * 业务类型ID
     */
    @TableId(type = IdType.AUTO)
    private Long businessTypeId;

    private Long enterpriseId;
    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 业务代码
     */
    private String businessTypeCode;

    /**
     * 运输类型
     */
    private Integer tripType;

    /**
     * 默认状态
     */
    private Boolean defaultFlag;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
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
