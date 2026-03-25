package net.lab1024.tms.admin.module.business.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/10/27 11:14 下午
 */
@Data
public class WaybillCostAmountUpdateBO {

    private Long waybillId;

    private BigDecimal costAmount;
}