package net.lab1024.tms.fixedasset.module.business.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 盘点资产关联表
 *
 * @author lidoudou
 * @date 2023/3/24 上午09:20
 */
@Mapper
@Component
public interface CheckItemDao extends BaseMapper<CheckItemEntity> {

    /**
     * 根据采购ID查询关联资产
     *
     * @param checkId
     * @return
     */
    List<CheckItemEntity> queryByCheckId(@Param("checkId") Long checkId);

    /**
     * 根据盘点ID查询关联资产
     *
     * @param checkIdList
     * @return
     */
    List<CheckItemEntity> queryByCheckIdList(@Param("checkIdList") List<Long> checkIdList);
}
