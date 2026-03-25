package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.order.dao.*;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2023/5/30 9:03 上午
 */
@Service
public class OrderClearService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCostDao orderCostDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private OrderMailAddressDao orderMailAddressDao;
    @Autowired
    private OrderPathDao orderPathDao;

    @Autowired
    private DataTracerClearService dataTracerClearService;

    private final Long PAGE_SIZE = 100L;

    public void orderClear(List<Long> enterpriseIdList){
        QueryWrapper searchQw = new QueryWrapper();
        searchQw.notIn("enterprise_id", enterpriseIdList);

        Integer totalCount = orderDao.selectCount(searchQw);
        List<OrderEntity> orderEntityList = orderDao.selectList(searchQw);

        // 分页
        // 取余 同样也是有一页条数
        long lastPageNum = totalCount % PAGE_SIZE;
        // 取整 获取总共页数
        long pages = totalCount / PAGE_SIZE;
        // 取余若有值 则页数加一
        if (lastPageNum > 0) {
            pages++;
        }

        for (long i = 0; i < pages; i++) {
            List<OrderEntity> limitEntityList = orderEntityList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
            List<Long> orderIdList = limitEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
            QueryWrapper qw = new QueryWrapper();
            qw.in("order_id", orderIdList);

            // 删除应收操作记录
            dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.ORDER, orderIdList);

            // 删除应收应付费用明细
            orderCostDao.delete(qw);
            // 删除商品信息
            orderGoodsDao.delete(qw);
            // 删除凭证记录
            orderMailAddressDao.delete(qw);
            // 路线信息
            orderPathDao.delete(qw);
            // 删除订单
            orderDao.delete(qw);
        }
    }
}