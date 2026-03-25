package net.lab1024.tms.admin.module.business.oa.cost.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oa.cost.domain.entity.CostApplyEntity;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/29 下午4:45
 */
@Mapper
@Component
public interface CostApplyDao extends BaseMapper<CostApplyEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CostApplyEntity> queryPage(Page page, @Param("queryForm") CostApplyQueryForm queryForm);

    /**
     * 批量修改状态
     *
     * @param assetIdList
     * @param status
     */
    void batchUpdateStatus(@Param("applyIdList") List<Long> assetIdList, @Param("status") Integer status);

}