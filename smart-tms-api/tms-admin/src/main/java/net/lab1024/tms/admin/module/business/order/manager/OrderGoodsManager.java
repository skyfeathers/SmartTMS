package net.lab1024.tms.admin.module.business.order.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.order.dao.OrderGoodsDao;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import org.springframework.stereotype.Service;

/**
 * 订单关联商品
 *
 * @author lidoudou
 * @date 2022/7/14 下午3:16
 */
@Service
public class OrderGoodsManager extends ServiceImpl<OrderGoodsDao, OrderGoodsEntity> {
}
