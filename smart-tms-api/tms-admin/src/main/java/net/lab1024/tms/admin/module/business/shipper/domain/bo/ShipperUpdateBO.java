package net.lab1024.tms.admin.module.business.shipper.domain.bo;

import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperUpdateForm;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/27 14:43
 */
@Data
public class ShipperUpdateBO {
    /**
     * 更新请求信息
     */
    private ShipperUpdateForm updateDTO;
    /**
     *
     */
    private ShipperRecordParseBO recordParseBO;
    /**
     * 待更新的实体对象
     */
    private ShipperEntity waitUpdateEntity;
}
