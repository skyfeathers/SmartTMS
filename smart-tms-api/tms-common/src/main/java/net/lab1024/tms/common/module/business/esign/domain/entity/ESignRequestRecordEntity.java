package net.lab1024.tms.common.module.business.esign.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * e签宝请求记录
 *
 * @author lihaifan
 * @date 2022/9/16 16:16
 */
@Data
@TableName("t_esign_request_record")
public class ESignRequestRecordEntity {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String httpMethod;

    /**
     * 参数
     */
    private String params;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 返回值
     */
    private String responseBody;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
