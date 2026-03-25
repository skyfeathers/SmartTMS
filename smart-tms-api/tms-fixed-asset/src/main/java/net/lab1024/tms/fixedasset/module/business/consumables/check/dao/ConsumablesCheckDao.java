package net.lab1024.tms.fixedasset.module.business.consumables.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 易耗品盘点查询
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:34
 */
@Mapper
@Component
public interface ConsumablesCheckDao extends BaseMapper<ConsumablesCheckEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ConsumablesCheckEntity> queryPage(Page page, @Param("queryForm") ConsumablesCheckQueryForm queryForm);
}
