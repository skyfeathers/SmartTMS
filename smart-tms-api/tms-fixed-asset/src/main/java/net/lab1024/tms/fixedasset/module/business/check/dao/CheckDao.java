package net.lab1024.tms.fixedasset.module.business.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEntity;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 盘点
 *
 * @author lidoudou
 * @date 2023/3/24 上午09:15
 */
@Mapper
@Component
public interface CheckDao extends BaseMapper<CheckEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CheckEntity> queryPage(Page page, @Param("queryForm") CheckQueryForm queryForm);
}
