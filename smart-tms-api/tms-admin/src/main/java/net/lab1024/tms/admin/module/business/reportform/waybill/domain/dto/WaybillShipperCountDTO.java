package net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
/**
 * 根据派车方式统计数量
 *
 * @Author lidoudou
 * @Date 2023/5/11 上午9:33
 */
@Data
public class WaybillShipperCountDTO {

    /**
     * 货主ID
     */
    private Long shipperId;

    /**
     * 所属公司ID
     */
    private Long enterpriseId;

    /**
     * 派车方式
     */
    private Integer settleMode;

    /**
     * 运单数量
     */
    private Integer count;

}
