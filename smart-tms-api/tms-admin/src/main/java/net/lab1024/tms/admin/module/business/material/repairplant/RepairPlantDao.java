package net.lab1024.tms.admin.module.business.material.repairplant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantQueryForm;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantEntity;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 维修厂 dao
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:53
 */
@Mapper
@Component
public interface RepairPlantDao extends BaseMapper<RepairPlantEntity> {

    /**
     * 分页查询维修厂家列表
     *
     * @param queryForm
     * @return
     */
    List<RepairPlantEntity> query(Page page, @Param("query") RepairPlantQueryForm queryForm);

    /**
     * 根据 名称 查询
     *
     * @param repairPlantName
     * @param deletedFlag
     * @return
     */
    RepairPlantEntity selectByName(@Param("enterpriseId") Long enterpriseId, @Param("repairPlantName") String repairPlantName, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询不分页的列表
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<RepairPlantSimpleVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
