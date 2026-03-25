package net.lab1024.tms.fixedasset.module.business.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.entity.RepairEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairQueryForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairAssetVO;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 固定资产-维修登记 Dao
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Mapper
@Component
public interface RepairDao extends BaseMapper<RepairEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<RepairVO> queryPage(Page page, @Param("queryForm") RepairQueryForm queryForm);


    /**
     * 批量插入维修资产
     *
     * @param repairId
     * @param assetList
     */
    void batchInsertRepairAsset(@Param("repairId") Long repairId, @Param("assetIdList") List<AssetEntity> assetList);

    /**
     * 批量修改状态
     *
     * @param assetIdList
     * @param status
     */
    void batchUpdateStatus(@Param("repairIdList") List<Long> assetIdList, @Param("status") Integer status);

    /**
     * 维修登记id
     *
     * @param repairId
     * @return
     */
    List<Long> selectRelateAssetIdList(@Param("repairId") Long repairId);


    /**
     * 根据
     *
     * @param repairIdList
     * @return
     */
    List<RepairAssetVO> selectRelateAssetList(@Param("repairIdList") List<Long> repairIdList);
}
