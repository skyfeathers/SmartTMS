package net.lab1024.tms.admin.module.business.oa.cost.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.oa.cost.domain.entity.CostApplyItemEntity;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyItemAddForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/29 下午4:45
 */
@Mapper
@Component
public interface CostApplyItemDao extends BaseMapper<CostApplyItemEntity> {


    /**
     * 批量插入
     *
     * @param applyId
     * @param itemList
     */
    void batchInsertApplyItem(@Param("applyId") Long applyId, @Param("itemList") List<CostApplyItemAddForm> itemList);

    /**
     * 根据ID查询
     *
     * @param applyIdList
     * @return
     */
    List<CostApplyItemEntity> selectByApplyIdList(@Param("applyIdList") List<Long> applyIdList);
}
