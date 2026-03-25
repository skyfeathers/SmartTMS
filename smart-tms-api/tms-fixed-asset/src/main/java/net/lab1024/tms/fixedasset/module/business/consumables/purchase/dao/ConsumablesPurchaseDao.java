package net.lab1024.tms.fixedasset.module.business.consumables.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 易耗品采购dao
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:11
 */
@Mapper
@Component
public interface ConsumablesPurchaseDao extends BaseMapper<ConsumablesPurchaseEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ConsumablesPurchaseVO> queryPage(Page page, @Param("queryForm") ConsumablesPurchaseQueryForm queryForm);
}
