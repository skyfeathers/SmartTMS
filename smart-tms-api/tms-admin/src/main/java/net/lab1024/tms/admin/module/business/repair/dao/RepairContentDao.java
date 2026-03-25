package net.lab1024.tms.admin.module.business.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.repair.entity.RepairContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/***
 * 车辆维修子表Dao
 *
 * @author zhaoxinyang
 * @date 2023/10/17 14:40
 */
@Mapper
@Component
public interface RepairContentDao extends BaseMapper<RepairContentEntity> {

    /**
     * 根据维修主表IDList获取维修内容
     *
     * @param repairIdList
     * @return
     */
    List<RepairContentEntity> selectByRepairIds(@Param("repairIdList") List<Long> repairIdList);

    /**
     * 根据维修主表ID删除子表维修内容
     *
     * @param repairId
     */
    void deleteByRepairId(@Param("repairId") Long repairId);

    /**
     * 根据维修ID查询维修总金额
     *
     * @param repairIdList
     * @return
     */
    BigDecimal selectSumAmountByRepairIdList(@Param("repairIdList") List<Long> repairIdList);

}
