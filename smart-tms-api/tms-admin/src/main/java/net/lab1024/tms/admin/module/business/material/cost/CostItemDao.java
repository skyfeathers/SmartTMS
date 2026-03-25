package net.lab1024.tms.admin.module.business.material.cost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemQueryForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemVO;
import net.lab1024.tms.common.module.business.material.cost.domain.CostItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */

@Mapper
@Component
public interface CostItemDao extends BaseMapper<CostItemEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryForm
     * @return
     */
    List<CostItemVO> queryPage(Page<?> page, @Param("queryForm") CostItemQueryForm queryForm);

    /**
     * 修改删除状态
     *
     * @param costItemId
     * @param deletedFlag
     */
    void updateDeleteFlag(@Param("costItemId") Long costItemId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 按类型查询
     * @param typeList
     * @param deletedFlag
     * @return
     */
    List<CostItemEntity> selectByType(@Param("typeList")List<Integer> typeList, @Param("deletedFlag") Boolean deletedFlag);
}
