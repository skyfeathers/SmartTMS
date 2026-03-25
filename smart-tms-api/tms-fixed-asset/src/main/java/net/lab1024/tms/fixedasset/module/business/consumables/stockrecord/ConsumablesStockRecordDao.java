package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.entity.ConsumablesStockRecordEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.form.ConsumablesStockRecordQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库存变动记录
 *
 * @author lidoudou
 * @date 2023/4/18 下午5:12
 */
@Mapper
@Component
public interface ConsumablesStockRecordDao extends BaseMapper<ConsumablesStockRecordEntity> {

    List<ConsumablesStockRecordEntity> queryPage(Page page, @Param("queryForm") ConsumablesStockRecordQueryForm queryForm);
}
