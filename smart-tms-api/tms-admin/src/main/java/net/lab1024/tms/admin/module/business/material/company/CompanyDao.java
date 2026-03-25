package net.lab1024.tms.admin.module.business.material.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyQueryForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyVO;
import net.lab1024.tms.common.module.business.material.company.CompanyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务资料-公司管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component
public interface CompanyDao extends BaseMapper<CompanyEntity> {

    /**
     * 根据公司编号查询
     *
     * @param companyCode
     * @param excludeCompanyId
     * @param deletedFlag
     * @return
     */
    CompanyEntity queryByCompanyCode(@Param("companyCode") String companyCode, @Param("excludeCompanyId") Long excludeCompanyId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除公司
     *
     * @param companyId
     * @param deletedFlag
     */
    void deleteCompany(@Param("companyId") Long companyId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 公司分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CompanyVO> queryPage(Page page, @Param("queryForm") CompanyQueryForm queryForm);

    /**
     * 查询公司详情
     *
     * @param companyId
     * @return
     */
    CompanyVO getDetail(@Param("companyId") Long companyId, @Param("deletedFlag") Boolean deletedFlag);
}
