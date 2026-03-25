package net.lab1024.tms.common.module.business.carcost.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CommonCarCostOilPayDao extends BaseMapper<CarCostOilPayEntity> {

    /**
     * 根据运单ID以及燃油类型查询
     *
     * @param waybillId
     * @param fuelType
     * @return
     */
    OilCardEntity selectByWaybillIdAndFuelType(@Param("waybillId") Long waybillId, @Param("fuelType") Integer fuelType, @Param("excludeAuditStatus") Integer excludeAuditStatus);
}
