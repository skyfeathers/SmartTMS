package net.lab1024.tms.admin.module.business.carcost.oilcardinitialend;

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
     * 根据运单ID和油卡ID获取油卡期初期末
     *
     * @param waybillId
     * @param oilCardId
     */
    CarCostOilCardInitialEndEntity selectByWaybillIdAndOilCardId(@Param("waybillId") Long waybillId, @Param("oilCardId") Long oilCardId);

    /**
     * 查询运单油卡期初期末
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilCardInitialEndVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 获取ID之后的记录
     *
     * @param oilCardInitialEndId
     * @param oilCardId
     * @return
     */
    List<CarCostOilCardInitialEndEntity> selectByIdAfter(@Param("oilCardInitialEndId") Long oilCardInitialEndId, @Param("oilCardId") Long oilCardId);


    /**
     * 查询运单油卡期初期末
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostOilCardInitialEndVO> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);



}
