package net.lab1024.tms.admin.module.business.carcost.cashInitialend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashInitialEndEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostCashInitialEndDao extends BaseMapper<CarCostCashInitialEndEntity> {

    /**
     * 获取现金期初期末记录
     *
     * @param waybillId
     * @return
     */
    CarCostCashInitialEndEntity selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 获取ID之后的司机期初期末记录
     *
     * @param cashInitialEndId
     * @param driverId
     * @return
     */
    List<CarCostCashInitialEndEntity> selectByIdAfter(@Param("cashInitialEndId") Long cashInitialEndId, @Param("driverId") Long driverId);

    /**
     * 根据运单ID查询现金期初期末记录
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostCashInitialEndEntity> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);


}
