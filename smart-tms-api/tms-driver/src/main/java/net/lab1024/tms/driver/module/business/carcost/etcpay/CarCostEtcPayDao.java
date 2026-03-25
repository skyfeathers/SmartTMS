package net.lab1024.tms.driver.module.business.carcost.etcpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.driver.module.business.carcost.etcpay.domain.CarCostEtcPayVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostEtcPayDao extends BaseMapper<CarCostEtcPayEntity> {

    /**
     * ETC支出详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID获取ETC支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostEtcPayVO> selectByWaybillId(Long waybillId);
}
