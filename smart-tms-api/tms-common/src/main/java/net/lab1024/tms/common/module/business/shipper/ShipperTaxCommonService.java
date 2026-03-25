package net.lab1024.tms.common.module.business.shipper;

import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2023/2/25 5:31 下午
 */
@Service
public class ShipperTaxCommonService {

    /**
     *  8300 % (1-0.06) * 0.06
     * @param payableTotalAmount
     * @param shipperNftRate
     * @return
     */
    public BigDecimal getNftAmount(BigDecimal payableTotalAmount, BigDecimal shipperNftRate) {
        BigDecimal nftRate = shipperNftRate.divide(new BigDecimal(100));
        BigDecimal priceTax = BigDecimal.ONE.subtract(nftRate);
        BigDecimal priceTaxTotal = SmartBigDecimalUtil.divide(payableTotalAmount, priceTax, 8);
        return SmartBigDecimalUtil.setScale(priceTaxTotal.multiply(nftRate), 0);
    }
}