package net.lab1024.tms.fixedasset.module.business.consumables.stock.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 库存
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:25
 */
@Mapper
@Component
public interface ConsumablesDao extends BaseMapper<ConsumablesEntity> {

    /**
     * 分页
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ConsumablesVO> queryPage(Page page, @Param("queryForm") ConsumablesQueryForm queryForm);

    /**
     * 名称查询
     *
     * @param consumablesName
     * @param deletedFlag
     * @return
     */
    List<ConsumablesEntity> selectByNameExcludeIds(@Param("consumablesName") String consumablesName, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag, @Param("enterpriseId") Long enterpriseId);

    /**
     * 根据删除状态查询
     *
     * @param deletedFlag
     * @param consumablesIdList
     * @return
     */
    List<ConsumablesEntity> selectByIdList(@Param("consumablesIdList") List<Long> consumablesIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据公司以及所属位置查询
     *
     * @param enterpriseId
     * @param locationId
     * @return
     */
    List<ConsumablesEntity> selectByEnterpriseLocationId(@Param("enterpriseId") Long enterpriseId, @Param("locationId") Long locationId);
}
