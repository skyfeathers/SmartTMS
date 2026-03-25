package net.lab1024.tms.common.module.support.dingding.sync.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门钉钉同步表
 *
 * @author lidoudou
 * @date 2023/4/22 下午1:55
 */
@Data
@TableName("t_ding_ding_department")
public class DingDingDepartmentEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 内部部门ID
     */
    private Long departmentId;

    /**
     * 钉钉部门ID
     */
    private Long deptId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
