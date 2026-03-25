package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [ 货主跟进 ]
 *
 * @author yandanyang
 * @date 2020/10/23 19:00
 */
@Data
@TableName("t_shipper_track")
public class ShipperTrackEntity {

    @TableId(type = IdType.AUTO)
    private Long trackId;

    /**
     * 跟进时间
     */
    private LocalDateTime trackTime;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextTrackTime;

    /**
     * 谁跟进的
     */
    private Long employeeId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 被拜访人名称
     */
    private String intervieweeName;

    /**
     * 跟进方式
     */
    private Integer trackWay;

    /**
     * 跟进内容
     */
    private String trackContent;

    /**
     * 备注
     */
    private String remark;
    /**
     * 附件
     */
    private String attachment;

    /**
     * ip
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
