package net.lab1024.tms.fixedasset.module.business.borrowback.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 借用、归还
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:50
 */
@Data
@TableName("t_asset_borrow_back_item")
public class BorrowBackItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 领用
     */
    private Long borrowBackId;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
