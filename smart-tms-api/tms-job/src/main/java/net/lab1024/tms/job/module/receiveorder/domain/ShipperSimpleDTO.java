package net.lab1024.tms.job.module.receiveorder.domain;

import lombok.Data;

/**
 * 货主信息
 */
@Data
public class ShipperSimpleDTO {

    /**
     * 货主ID
     */
    private Long shipperId;

    private Long enterpriseId;

    /**
     * 货主名称
     */
    private String consignor;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 负责人ID
     */
    private Long managerId;
}
