package net.lab1024.tms.common.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 
 * [ 角色 ]
 * 
 * @author 罗伊
 * @date
 */
@Data
@TableName("t_role")
public class RoleEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    private Long enterpriseId;

    private String roleName;

    private String roleCode;

    private Boolean systemFlag;

    private String remark;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
