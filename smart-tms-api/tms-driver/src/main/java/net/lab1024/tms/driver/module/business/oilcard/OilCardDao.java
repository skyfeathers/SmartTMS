package net.lab1024.tms.driver.module.business.oilcard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.driver.module.business.oilcard.domain.OilCardSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OilCardDao extends BaseMapper<OilCardEntity> {

    /**
     * 根据车辆ID查询绑定油卡
     *
     * @param vehicleId
     * @param deletedFlag
     * @return
     */
    List<OilCardSimpleVO> queryListByVehicleId(@Param("vehicleId") Long vehicleId, @Param("type") Integer type, @Param("fixedPointFlag") Boolean fixedPointFlag, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
