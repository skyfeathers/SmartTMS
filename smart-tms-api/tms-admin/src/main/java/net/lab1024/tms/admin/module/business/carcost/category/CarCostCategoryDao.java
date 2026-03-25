package net.lab1024.tms.admin.module.business.carcost.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.admin.module.business.carcost.category.domain.CarCostCategoryQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostCategoryDao extends BaseMapper<CarCostCategoryEntity> {

    /**
     * 查询所有费用分类
     *
     * @param deletedFlag
     * @return
     */
    List<CarCostCategoryVO> selectAll(@Param("deletedFlag") Boolean deletedFlag);

    /**
     * 分页查询费用分类
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CarCostCategoryVO> selectByPage(Page page, @Param("queryForm") CarCostCategoryQueryForm queryForm);

    /**
     * 根据费用类型查询费用分类
     *
     * @param costType
     * @param deletedFlag
     * @return
     */
    List<CarCostCategoryVO> selectByCostType(@Param("costType") Integer costType, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除费用分类
     *
     * @param categoryId
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("categoryId") Long categoryId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据分类名字查询费用分类
     *
     * @param categoryName
     * @param costType
     * @param excludeIds
     * @param deletedFlag
     * @return
     */
    CarCostCategoryEntity selectByCategoryName(@Param("categoryName") String categoryName,
                                               @Param("costType") Integer costType,
                                               @Param("excludeIds") List<Long> excludeIds,
                                               @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据IDList查询分类
     *
     * @param categoryIdList
     * @param deletedFlag
     * @return
     */
    List<CarCostCategoryEntity> selectByIdList(@Param("categoryIdList") List<Long> categoryIdList, @Param("deletedFlag") Boolean deletedFlag);
}