package net.lab1024.tms.driver.module.business.carcost.ureapay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.driver.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostUreaPayDao extends BaseMapper<CarCostUreaPayEntity> {

    /**
     * 尿素支出详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID获取尿素支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostUreaPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
