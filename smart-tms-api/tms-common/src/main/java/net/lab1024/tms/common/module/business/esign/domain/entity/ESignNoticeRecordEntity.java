package net.lab1024.tms.common.module.business.esign.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * e签宝回调记录
 *
 * @author lihaifan
 * @date 2022/9/16 18:40
 */
@Data
@TableName("t_esign_notice_record")
public class ESignNoticeRecordEntity {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 事件名称
     */
    private String action;

    /**
     * 事件名称描述
     */
    private String actionDesc;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 请求参数
     */
    private String requestBody;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
