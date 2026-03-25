package net.lab1024.tms.driver.module.business.carcost.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostCategoryDao extends BaseMapper<CarCostCategoryEntity> {

    /**
     * 根据分类IDList获取分类集合
     *
     * @param categoryIdList
     * @return
     */
    List<CarCostCategoryEntity> selectByIds(@Param("categoryIdList") List<Long> categoryIdList);

    /**
     * 根据费用类型查询费用分类
     *
     * @param costType
     * @param deletedFlag
     * @return
     */
    List<CarCostCategoryVO> selectByCostType(@Param("costType") Integer costType, @Param("deletedFlag") Boolean deletedFlag);

}
