package net.lab1024.tms.admin.module.business.pay.domain.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/10/26 2:20 下午
 */
@Data
public class PayOrderVehicleCostForm {

    /**
     * 车辆费用ID
     */
    private Long vehicleCostId;

    /**
     * 车辆费用
     */
    private BigDecimal costAmount;
}