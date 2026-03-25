package net.lab1024.tms.fixedasset.module.business.scrap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.entity.ScrapEntity;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapQueryForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapAssetVO;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 固定资产-报废 Dao
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Mapper
@Component
public interface ScrapDao extends BaseMapper<ScrapEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrapVO> queryPage(Page page, @Param("queryForm") ScrapQueryForm queryForm);


    /**
     * 批量插入维修资产
     *
     * @param scrapId
     * @param assetIdList
     */
    void batchInsertScrapAsset(@Param("scrapId") Long scrapId, @Param("assetIdList") List<Long> assetIdList);

    /**
     * 批量修改状态
     *
     * @param assetIdList
     * @param status
     */
    void batchUpdateStatus(@Param("scrapIdList") List<Long> assetIdList, @Param("status") Integer status);

    /**
     * 报废id
     *
     * @param scrapId
     * @return
     */
    List<Long> selectRelateAssetIdList(@Param("scrapId") Long scrapId);

    /**
     * 根据
     *
     * @param scrapIdList
     * @return
     */
    List<ScrapAssetVO> selectRelateAssetList(@Param("scrapIdList") List<Long> scrapIdList);
}
