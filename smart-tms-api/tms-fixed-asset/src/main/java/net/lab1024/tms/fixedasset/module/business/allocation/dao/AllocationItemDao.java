package net.lab1024.tms.fixedasset.module.business.allocation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/21 上午10:38
 */
@Mapper
@Component
public interface AllocationItemDao extends BaseMapper<AllocationItemEntity> {

    /**
     * 根据ID查询关联资产
     *
     * @param allocationIdList
     * @return
     */
    List<AllocationItemEntity> queryByAllocationIdList(@Param("allocationIdList") List<Long> allocationIdList);
}
