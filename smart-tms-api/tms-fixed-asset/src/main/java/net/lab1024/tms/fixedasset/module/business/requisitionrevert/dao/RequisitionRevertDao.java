package net.lab1024.tms.fixedasset.module.business.requisitionrevert.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertEntity;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertQueryForm;
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
public interface RequisitionRevertDao extends BaseMapper<RequisitionRevertEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<RequisitionRevertEntity> queryPage(Page page, @Param("queryForm") RequisitionRevertQueryForm queryForm);
}
