package net.lab1024.tms.fixedasset.module.business.depreciation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationQueryForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 资产折旧
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:48
 */
@Mapper
@Component
public interface DepreciationDao extends BaseMapper<DepreciationEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<DepreciationVO> queryPage(Page page, @Param("queryForm") DepreciationQueryForm queryForm);

    /**
     * 查询是否存在相同日期
     *
     * @param enterpriseId
     * @param localDate
     * @param deletedFlag
     * @return
     */
    DepreciationEntity selectByDepreciationDate(@Param("enterpriseId") Long enterpriseId, @Param("depreciationDate") LocalDate localDate, @Param("deletedFlag") Boolean deletedFlag);
}
