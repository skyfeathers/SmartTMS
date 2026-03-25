package net.lab1024.tms.driver.module.business.carcost.cashInitialend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashInitialEndEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CarCostCashInitialEndDao extends BaseMapper<CarCostCashInitialEndEntity> {

    /**
     * 获取现金期初期末记录
     *
     * @param waybillId
     * @return
     */
    CarCostCashInitialEndEntity selectByWaybillId(@Param("waybillId") Long waybillId);
}
