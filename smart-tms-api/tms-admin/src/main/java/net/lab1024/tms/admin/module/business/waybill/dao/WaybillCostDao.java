package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillAmountUpdateBO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostSumVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:18 下午
 */
@Mapper
@Component
public interface WaybillCostDao extends BaseMapper<WaybillCostEntity> {

    /**
     * 批量更新运单费用项已支付金额
     *
     * @param updateWaybillList
     */
    void batchUpdateWaybillCostAmount(@Param("list") List<WaybillAmountUpdateBO> updateWaybillList);


    /**
     * 查询运单费用信息
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillCostEntity> selectByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList);

    /**
     * 查询运单费用信息
     *
     * @param waybillId
     * @return
     */
    List<WaybillCostEntity> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 根据查询运单应收费用合计
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillCostSumVO> sumByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList);

    /**
     * 根据运单分类查询运单应收费用合计
     *
     * @param waybillIdList
     * @param costItemType
     * @return
     */
    List<WaybillCostSumVO> sumByWaybillIdListAndType(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("costItemType") Integer costItemType);

    /**
     * 根据订单统计费用
     *
     * @param orderIdList
     * @param costItemCategory 可为空 CostItemCategoryEnum
     * @return
     */
    BigDecimal sumByWaybillIdListAndCategory(@Param("waybillIdList") List<Long> orderIdList, @Param("costItemCategory") Integer costItemCategory);

    /**
     * 根据运单ID和分类查询运单费用
     *
     * @param waybillIdList
     * @param categoryList
     * @return
     */
    List<WaybillCostEntity> selectByWaybillIdListAndCategoryList(@Param("waybillIdList") List<Long> waybillIdList, @Param("categoryList") List<Object> categoryList);

}
