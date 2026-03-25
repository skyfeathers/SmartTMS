package net.lab1024.tms.admin.module.business.shipper.domain.bo;

import lombok.Data;
import net.lab1024.tms.common.module.business.shipper.domain.*;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/27 14:31
 */
@Data
public class ShipperOriginBO {
    /**
     * 货主原始实体
     */
    private ShipperEntity originShipperEntity;

    private List<ShipperContactEntity> originContactEntityList;

    private List<ShipperPaymentWayEntity> originPaymentWayEntityList;

    private List<ShipperInvoiceEntity> originInvoiceEntity;

    private List<ShipperMailAddressEntity> originMailAddressEntityList;

}
