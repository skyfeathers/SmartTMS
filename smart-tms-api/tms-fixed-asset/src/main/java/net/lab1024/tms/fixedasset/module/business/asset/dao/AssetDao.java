package net.lab1024.tms.fixedasset.module.business.asset.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 固定资产 Dao
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Mapper
@Component
public interface AssetDao extends BaseMapper<AssetEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AssetEntity> queryPage(Page page, @Param("queryForm") AssetQueryForm queryForm);

    /**
     * 更新删除状态
     */
    long updateDeleted(@Param("assetId") Long assetId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 批量更新删除状态
     */
    void batchUpdateDeleted(@Param("idList") List<Long> idList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据ID查询
     *
     * @param assetIdList
     * @param deletedFlag
     * @return
     */
    List<AssetEntity> selectByIdList(@Param("assetIdList") List<Long> assetIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据位置查询
     *
     * @param locationId
     * @param enterpriseId
     * @param deletedFlag
     * @return
     */
    List<AssetEntity> selectByEnterpriseLocationId(@Param("enterpriseId") Long enterpriseId, @Param("locationId") Long locationId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 根据公司查询
     *
     * @param enterpriseId
     * @param deletedFlag
     * @return
     */
    List<AssetEntity> selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);
}
