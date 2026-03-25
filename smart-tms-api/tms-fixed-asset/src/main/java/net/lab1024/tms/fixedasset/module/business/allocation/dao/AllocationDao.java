package net.lab1024.tms.fixedasset.module.business.allocation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationEntity;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资产采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:15
 */
@Mapper
@Component
public interface AllocationDao extends BaseMapper<AllocationEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AllocationEntity> queryPage(Page page, @Param("queryForm") AllocationQueryForm queryForm);
}
