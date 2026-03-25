package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.carcostold.dao.CarCostDao;
import net.lab1024.tms.admin.module.business.carcostold.dao.CarCostInitialEndDao;
import net.lab1024.tms.admin.module.business.carcostold.dao.CarCostItemDao;
import net.lab1024.tms.admin.module.business.carcostold.domain.entity.CarCostEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午5:09
 */
@Service
public class CarCostClearService {

    @Autowired
    private CarCostDao carCostDao;
    @Autowired
    private CarCostItemDao carCostItemDao;
    @Autowired
    private CarCostInitialEndDao carCostInitialEndDao;
    @Autowired
    private DataTracerClearService dataTracerClearService;


    void clear(List<Long> enterpriseIdList) {
        QueryWrapper payOrderQw = new QueryWrapper();
        payOrderQw.notIn("enterprise_id", enterpriseIdList);

        List<CarCostEntity> carCostEntityList = carCostDao.selectList(payOrderQw);
        if (CollectionUtils.isEmpty(carCostEntityList)) {
            return;
        }

        List<Long> carCostIdList = carCostEntityList.stream().map(CarCostEntity::getCarCostId).collect(Collectors.toList());
        QueryWrapper qw = new QueryWrapper();
        qw.in("car_cost_id", carCostIdList);

        dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.CAR_COST, carCostIdList);

        // 删除主表
        carCostDao.delete(qw);
        // 删除明细
        carCostItemDao.delete(qw);

        // 删除期初期末
        carCostInitialEndDao.delete(qw);

    }
}