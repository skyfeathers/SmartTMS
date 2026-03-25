package net.lab1024.tms.admin.module.business.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 5:29 下午
 */
@Data
public class WaybillAmountUpdateBO {

    private Long id;

    private BigDecimal amount;
}