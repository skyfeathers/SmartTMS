package net.lab1024.tms.driver.module.business.carcost.oilcardinitialend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardInitialEndEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostOilCardInitialEndVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostOilCardInitialEndDao extends BaseMapper<CarCostOilCardInitialEndEntity> {

    /**
     * 查询运单油卡期初期末
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilCardInitialEndVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
