package net.lab1024.tms.fixedasset.module.business.consumables.requisition.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 领用
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:47
 */
@Mapper
@Component
public interface ConsumablesRequisitionDao extends BaseMapper<ConsumablesRequisitionEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ConsumablesRequisitionEntity> queryPage(Page page, @Param("queryForm") ConsumablesRequisitionQueryForm queryForm);
}
