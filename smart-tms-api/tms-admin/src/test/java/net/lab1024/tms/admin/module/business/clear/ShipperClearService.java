package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.shipper.dao.*;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/30 上午9:18
 */
@Service
public class ShipperClearService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperContactDao shipperContactDao;
    @Autowired
    private ShipperInvoiceDao shipperInvoiceDao;
    @Autowired
    private ShipperMailAddressDao shipperMailAddressDao;
    @Autowired
    private ShipperPaymentWayDao shipperPaymentWayDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private ShipperTrackDao shipperTrackDao;
    @Autowired
    private ShipperTypeDao shipperTypeDao;
    @Autowired
    private DataTracerClearService dataTracerClearService;

    private final Long PAGE_SIZE = 100L;

    public void clear(List<Long> notInEnterpriseIdList) {
        QueryWrapper qw = new QueryWrapper();
        qw.notIn("enterprise_id", notInEnterpriseIdList);
        List<ShipperEntity> shipperEntityList = shipperDao.selectList(qw);
        if (CollectionUtils.isEmpty(shipperEntityList)) {
            return;
        }

        Integer receiveOrderCount = queryCount(qw);
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
            List<ShipperEntity> limitShipperList = shipperEntityList.stream().skip(i * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList());
            List<Long> shipperIdList = limitShipperList.stream().map(ShipperEntity::getShipperId).collect(Collectors.toList());
            QueryWrapper shipperQw = new QueryWrapper();
            shipperQw.in("shipper_id", shipperIdList);

            shipperDao.delete(shipperQw);
            shipperContactDao.delete(shipperQw);
            shipperInvoiceDao.delete(shipperQw);
            shipperMailAddressDao.delete(shipperQw);
            shipperPaymentWayDao.delete(shipperQw);
            shipperPrincipalDao.delete(shipperQw);
            shipperTrackDao.delete(shipperQw);
            shipperTypeDao.delete(shipperQw);

            dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.SHIPPER, shipperIdList);
        }
    }


    private Integer queryCount(QueryWrapper qw) {
        Integer totalCount = shipperDao.selectCount(qw);
        return totalCount;
    }
}