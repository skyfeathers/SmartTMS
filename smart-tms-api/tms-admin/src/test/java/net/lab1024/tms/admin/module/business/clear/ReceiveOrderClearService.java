package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.receive.dao.*;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午3:50
 */
@Service
public class ReceiveOrderClearService {
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private ReceiveOrderMailAddressDao receiveOrderMailAddressDao;
    @Autowired
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;
    @Autowired
    private DataTracerClearService dataTracerClearService;

    private final Long PAGE_SIZE = 100L;

    void clear(List<Long> enterpriseIdList) {
        QueryWrapper receiveOrderQw = new QueryWrapper();
        receiveOrderQw.notIn("enterprise_id", enterpriseIdList);

        Integer receiveOrderCount = queryCount(receiveOrderQw);
        List<ReceiveOrderEntity> receiveOrderEntityList = receiveOrderDao.selectList(receiveOrderQw);

        // 分页
        // 取余 同样也是有一页条数
        long lastPageNum = receiveOrderCount % PAGE_SIZE;
        // 取整 获取总共页数
        long pages = receiveOrderCount / PAGE_SIZE;
        // 取余若有值 则页数加一
        if (lastPageNum > 0) {
            pages++;
        }

        for (long i = 0; i < pages; i++) {
            List<ReceiveOrderEntity> limitPayOrderList = receiveOrderEntityList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
            List<Long> receiveOrderIdList = limitPayOrderList.stream().map(ReceiveOrderEntity::getReceiveOrderId).collect(Collectors.toList());
            QueryWrapper qw = new QueryWrapper();
            qw.in("receive_order_id", receiveOrderIdList);

            // 删除应收操作记录
            dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.RECEIVE_ORDER, receiveOrderIdList);

            // 删除主表
            receiveOrderDao.delete(qw);
            // 删除费用明细
            receiveOrderWaybillDao.delete(qw);
            // 删除开票信息
            receiveOrderInvoiceDao.delete(qw);
            // 删除收货地址信息
            receiveOrderMailAddressDao.delete(qw);
            // 删除核销记录
            receiveOrderVerificationDao.delete(qw);
        }
    }

    private Integer queryCount(QueryWrapper qw) {
        Integer totalCount = receiveOrderDao.selectCount(qw);
        return totalCount;
    }
}