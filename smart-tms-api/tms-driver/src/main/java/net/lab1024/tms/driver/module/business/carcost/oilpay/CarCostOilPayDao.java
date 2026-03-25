package net.lab1024.tms.driver.module.business.carcost.oilpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.driver.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostOilPayDao extends BaseMapper<CarCostOilPayEntity> {

    /**
     * 油费支出详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID查询油费记录
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
