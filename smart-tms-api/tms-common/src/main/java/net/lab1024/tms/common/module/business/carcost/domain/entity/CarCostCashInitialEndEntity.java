package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_car_cost_cash_initial_end")
public class CarCostCashInitialEndEntity {

    /**
     * 现金期初期末ID
     */
    @TableId(type = IdType.AUTO)
    private Long cashInitialEndId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 司机ID
     */
    private Long driverId;

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
