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
public class ShipperNewBO {
    /**
     * 更新（新增）实体
     */
    private ShipperEntity newShipperEntity;

    private List<ShipperContactEntity> newContactEntityList;

    private List<ShipperPaymentWayEntity> newPaymentWayEntityList;

    private List<ShipperInvoiceEntity> newInvoiceEntityList;

    private List<ShipperMailAddressEntity> newMailAddressEntityList;

}
