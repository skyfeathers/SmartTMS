package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostSumVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:18 下午
 */
@Mapper
@Component
public interface WaybillReceiveCostDao extends BaseMapper<WaybillReceiveCostEntity> {

    /**
     * 查询运单应收费用信息
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillReceiveCostEntity> selectByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList);

    /**
     * 根据运单分类查询运单应收费用合计
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillCostSumVO> sumByWaybillIdListAndType(@Param("waybillIdList") Collection<Long> waybillIdList);

    /**
     * 查询运单应收费用信息
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillReceiveCostEntity> selectByWaybillIdListAndTypeCategory(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("costItemCategory") Integer costItemCategory, @Param("costItemType") Integer costItemType);

    /**
     * 根据运单ID和分类查询运单费用
     *
     * @param waybillIdList
     * @param costItemCategoryList
     * @return
     */
    List<WaybillReceiveCostEntity> selectByWaybillIdListAndCategoryList(@Param("waybillIdList") List<Long> waybillIdList, @Param("costItemCategoryList") List<Integer> costItemCategoryList);

}
