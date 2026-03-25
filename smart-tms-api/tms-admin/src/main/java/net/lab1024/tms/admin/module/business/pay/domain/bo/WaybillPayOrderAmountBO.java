package net.lab1024.tms.admin.module.business.pay.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/30 下午4:09
 */
@Data
public class WaybillPayOrderAmountBO {

    private Long waybillId;

    private BigDecimal payAmount;

    private BigDecimal waitPayAmount;
}