package net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/16 17:34
 */
@Data
public class CarCostVehicleMonthOilAmountDTO {

    private Long vehicleId;

    private BigDecimal amount;

    private Integer fuelType;
}