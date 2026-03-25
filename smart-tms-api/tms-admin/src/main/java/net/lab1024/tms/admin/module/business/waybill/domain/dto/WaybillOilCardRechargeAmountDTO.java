package net.lab1024.tms.admin.module.business.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/10/31 10:04 下午
 */
@Data
public class WaybillOilCardRechargeAmountDTO {

    private Long waybillId;

    private BigDecimal amount;
}