package net.lab1024.tms.admin.module.business.material.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsQueryForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsVO;
import net.lab1024.tms.common.module.business.material.goods.GoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务资料-货物管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component

public interface GoodsDao extends BaseMapper<GoodsEntity> {

    /**
     * 删除
     *
     * @param goodsId
     * @param deletedFlag
     */
    void deleteGoods(@Param("goodsId") Long goodsId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<GoodsVO> queryPage(Page page, @Param("queryForm") GoodsQueryForm queryForm);

    /**
     * 查询堆场详情
     *
     * @param goodsId
     * @return
     */
    GoodsVO getDetail(@Param("goodsId") Long goodsId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 列表查询
     *
     * @param deletedFlag
     * @return
     */
    List<GoodsVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);
}
