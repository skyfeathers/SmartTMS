package net.lab1024.tms.admin.module.business.flow.waithandle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/20 11:57
 */
@Data
@TableName("t_flow_wait_handle")
public class FlowWaitHandleEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;

    private Integer flowType;

    private Integer num;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
