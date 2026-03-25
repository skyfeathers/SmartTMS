package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_car_cost_oil_card_initial_end")
public class CarCostOilCardInitialEndEntity {

    /**
     * 油卡期初期末ID
     */
    @TableId(type = IdType.AUTO)
    private Long oilCardInitialEndId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 油卡ID
     */
    private Long oilCardId;

    /**
     * 期初金额
     */
    private BigDecimal initialAmount;

    /**
     * 期末金额
     */
    private BigDecimal endAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
