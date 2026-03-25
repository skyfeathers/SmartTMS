package net.lab1024.tms.common.module.support.dingding.sync.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("t_ding_ding_employee")
@NoArgsConstructor
public class DingDingEmployeeEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;
    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 钉钉UserId
     */
    private String userId;

    /**
     * 唯一ID
     */
    private String unionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
