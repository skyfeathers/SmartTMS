package net.lab1024.tms.driver.module.business.carcost.basicinfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CarCostBasicInfoDao extends BaseMapper<CarCostBasicInfoEntity> {

    /**
     * 根据运单获取确认状态
     *
     * @param waybillId
     * @return
     */
    Boolean selectConfirmFlagByWaybillId(@Param("waybillId") Long waybillId);

}
