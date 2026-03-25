package net.lab1024.tms.common.module.business.pic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */
@Data
@TableName("t_pic")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long picId;

    private Long enterpriseId;

    /**
     * 投放类型
     *
     * @see PicTypeEnum
     */
    private Integer type;


    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片key
     */
    private String imgKey;

    /**
     * 是否启用
     */
    private Boolean enableFlag;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 开始有效时间
     */
    private LocalDateTime startTime;

    /**
     * 结束有效时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
     */
    private String createUserName;

}
