package net.lab1024.tms.common.module.business.oilcard.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("t_oil_card_enterprise")
public class OilCardEnterpriseEntity {


    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 油卡ID
     */
    private Long oilCardId;

    private Long enterpriseId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
