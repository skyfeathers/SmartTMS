package net.lab1024.tms.fixedasset.module.business.depreciation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资产折旧
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:48
 */
@Mapper
@Component
public interface DepreciationItemDao extends BaseMapper<DepreciationItemEntity> {

    /**
     * 根据折旧ID查询
     *
     * @param depreciationIdList
     * @return
     */
    List<DepreciationItemEntity> selectByDepreciationIdList(@Param("depreciationIdList") List<Long> depreciationIdList);
}
