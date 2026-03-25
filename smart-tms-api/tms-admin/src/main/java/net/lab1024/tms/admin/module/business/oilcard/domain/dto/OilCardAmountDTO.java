package net.lab1024.tms.admin.module.business.oilcard.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/7/1 上午10:02
 */
@Data
public class OilCardAmountDTO {

    /**
     * 油卡ID
     */
    private Long oilCardId;

    /**
     * 金额统计
     */
    private BigDecimal balance;
}