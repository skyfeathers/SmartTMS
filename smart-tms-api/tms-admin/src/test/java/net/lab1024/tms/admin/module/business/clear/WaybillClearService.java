package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.waybill.dao.*;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
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
public class WaybillClearService {

    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private WaybillReceiveCostDao waybillReceiveCostDao;
    @Autowired
    private WaybillVoucherDao waybillVoucherDao;

    @Autowired
    private DataTracerClearService dataTracerClearService;

    private final Long PAGE_SIZE = 100L;

    public void waybillClear(List<Long> enterpriseIdList){
        QueryWrapper searchQw = new QueryWrapper();
        searchQw.notIn("enterprise_id", enterpriseIdList);

        Integer totalCount = waybillDao.selectCount(searchQw);
        List<WaybillEntity> waybillEntityList = waybillDao.selectList(searchQw);

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
            List<WaybillEntity> limitWaybillEntityList = waybillEntityList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
            List<Long> waybillIdList = limitWaybillEntityList.stream().map(WaybillEntity::getWaybillId).collect(Collectors.toList());
            QueryWrapper qw = new QueryWrapper();
            qw.in("waybill_id", waybillIdList);

            // 删除应收操作记录
            dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.WAYBILL, waybillIdList);

            // 删除应收应付费用明细
            waybillCostDao.delete(qw);
            waybillReceiveCostDao.delete(qw);
            // 删除商品信息
            waybillGoodsDao.delete(qw);
            // 删除凭证记录
            waybillVoucherDao.delete(qw);
            // 删除运单
            waybillDao.delete(qw);
        }
    }
}