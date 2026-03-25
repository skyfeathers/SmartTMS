package net.lab1024.tms.driver.module.business.carcost.cashreceive;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
import net.lab1024.tms.driver.module.business.carcost.cashreceive.domain.CarCostCashReceiveVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostCashReceiveDao extends BaseMapper<CarCostCashReceiveEntity> {

    /**
     * 现金收入详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单查询现金收入
     *
     * @param waybillId
     * @return
     */
    List<CarCostCashReceiveVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
