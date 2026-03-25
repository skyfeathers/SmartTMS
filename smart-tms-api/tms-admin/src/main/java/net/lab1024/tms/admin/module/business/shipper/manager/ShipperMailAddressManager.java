package net.lab1024.tms.admin.module.business.shipper.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperMailAddressDao;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperMailAddressEntity;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/7/15 10:01 上午
 */
@Service
public class ShipperMailAddressManager extends ServiceImpl<ShipperMailAddressDao, ShipperMailAddressEntity> {
}