package net.lab1024.tms.common.module.support.reload.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * t_reload_result 数据表 实体类
 *
 * @author 胡克
 * @date 2018/02/10 09:29
 */
@Data
@TableName("t_reload_result")
public class ReloadResultEntity {

    /**
     * 加载项标签
     */
    private String tag;

    /**
     * 运行标识
     */
    private String identification;

    /**
     * 参数
     */
    private String args;

    /**
     * 运行结果
     */
    private Boolean result;

    /**
     * 异常
     */
    private String exception;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
