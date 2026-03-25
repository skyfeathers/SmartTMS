package net.lab1024.tms.common.module.business.material.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-货物管理
 *
 * @author yandy
 * @date 2022/6/24 11:32
 */
@Data
@TableName("t_material_goods")
public class GoodsEntity {

    /**
     * 货物ID
     */
    @TableId(type = IdType.AUTO)
    private Long goodsId;


    private Long enterpriseId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物类型【数据字典】
     */
    private String goodsType;

    /**
     * 货物排序
     */
    private Integer sort;

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
