package net.lab1024.tms.driver.module.business.carcost.cashpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.driver.module.business.carcost.cashpay.domain.CarCostCashPayVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostCashPayDao extends BaseMapper<CarCostCashPayEntity> {

    /**
     * 现金支出详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID获取现金支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostCashPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
