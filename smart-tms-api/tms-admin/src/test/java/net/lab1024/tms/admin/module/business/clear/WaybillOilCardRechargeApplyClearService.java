package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillOilCardRechargeApplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午5:20
 */
@Service
public class WaybillOilCardRechargeApplyClearService {
    @Autowired
    private WaybillOilCardRechargeApplyDao waybillOilCardRechargeApplyDao;


    private final Long PAGE_SIZE = 100L;

    void clear(List<Long> enterpriseIdList) {
        QueryWrapper applyQw = new QueryWrapper();
        applyQw.notIn("enterprise_id", enterpriseIdList);
        waybillOilCardRechargeApplyDao.delete(applyQw);

//        Integer applyCount = queryCount(applyQw);
//        List<WaybillOilCardRechargeApplyEntity> waybillOilCardRechargeApplyList = waybillOilCardRechargeApplyDao.selectList(applyQw);
//
//        // 分页
//        // 取余 同样也是有一页条数
//        long lastPageNum = applyCount % PAGE_SIZE;
//        // 取整 获取总共页数
//        long pages = applyCount / PAGE_SIZE;
//        // 取余若有值 则页数加一
//        if (lastPageNum > 0) {
//            pages++;
//        }
//
//        for (long i = 0; i < pages; i++) {
//            List<WaybillOilCardRechargeApplyEntity> limitPayOrderList = waybillOilCardRechargeApplyList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
//            List<Long> rechargeApplyIdList = limitPayOrderList.stream().map(WaybillOilCardRechargeApplyEntity::getRechargeApplyId).collect(Collectors.toList());
//            waybillOilCardRechargeApplyDao.deleteBatchIds(rechargeApplyIdList);
//        }
    }


    private Integer queryCount(QueryWrapper qw) {
        Integer totalCount = waybillOilCardRechargeApplyDao.selectCount(qw);
        return totalCount;
    }

}