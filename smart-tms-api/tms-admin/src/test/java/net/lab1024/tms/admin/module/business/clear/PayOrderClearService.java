package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.pay.dao.*;
import net.lab1024.tms.common.module.business.pay.domain.*;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午2:57
 */
@Service
public class PayOrderClearService {
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private PayOrderPaymentDao payOrderPaymentDao;
    @Autowired
    private PayOrderReceiveDao payOrderReceiveDao;
    @Autowired
    private PayOrderVerificationDao payOrderVerificationDao;
    @Autowired
    private DataTracerClearService dataTracerClearService;

    private final Long PAGE_SIZE = 100L;

    void clear(List<Long> enterpriseIdList) {
        QueryWrapper payOrderQw = new QueryWrapper();
        payOrderQw.notIn("enterprise_id", enterpriseIdList);

        Integer payOrderCount = queryCount(payOrderQw);
        List<PayOrderEntity> payOrderEntityList = payOrderDao.selectList(payOrderQw);

        // 分页
        // 取余 同样也是有一页条数
        long lastPageNum = payOrderCount % PAGE_SIZE;
        // 取整 获取总共页数
        long pages = payOrderCount / PAGE_SIZE;
        // 取余若有值 则页数加一
        if (lastPageNum > 0) {
            pages++;
        }

        for (long i = 0; i < pages; i++) {
            List<PayOrderEntity> limitPayOrderList = payOrderEntityList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
            List<Long> payOrderIdList = limitPayOrderList.stream().map(PayOrderEntity::getPayOrderId).collect(Collectors.toList());
            QueryWrapper qw = new QueryWrapper();
            qw.in("pay_order_id", payOrderIdList);

            // 删除操作记录
            dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.PAY_ORDER, payOrderIdList);

            // 删除主表
            payOrderDao.delete(qw);
            // 删除费用
            payOrderCostDao.delete(qw);
            // 删除付款信息
            payOrderPaymentDao.delete(qw);
            // 删除收款信息
            payOrderReceiveDao.delete(qw);
            // 删除核销信息
            payOrderVerificationDao.delete(qw);
        }
    }

    private Integer queryCount(QueryWrapper payOrderQw) {
        Integer totalCount = payOrderDao.selectCount(payOrderQw);
        return totalCount;
    }
}