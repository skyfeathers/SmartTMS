package net.lab1024.tms.driver.module.business.carcost.oilcardreceive;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.driver.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostOilCardReceiveDao extends BaseMapper<CarCostOilCardReceiveEntity> {

    /**
     * 油卡收入详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID获取油卡收入列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilCardReceiveVO> selectByWaybillId(@Param("waybillId") Long waybillId);
}
