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
public class ShipperRecordParseBO {
    /**
     * 货主原始实体
     */
    private ShipperEntity originShipperEntity;

    private List<ShipperContactEntity> originContactEntityList;

    private List<ShipperPaymentWayEntity> originPaymentWayEntityList;

    private List<ShipperInvoiceEntity> originInvoiceEntityList;

    private List<ShipperMailAddressEntity> originMailAddressEntityList;
    
    private List<ShipperTypeEntity> originShipperTypeEntityList;
    /**
     * 更新（新增）实体
     */
    private ShipperEntity newShipperEntity;

    private List<ShipperContactEntity> newContactEntityList;

    private List<ShipperPaymentWayEntity> newPaymentWayEntityList;

    private List<ShipperInvoiceEntity> newInvoiceEntityList;

    private List<ShipperMailAddressEntity> newMailAddressEntityList;

    private List<ShipperTypeEntity> newShipperTypeEntityList;
}
