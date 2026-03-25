package net.lab1024.tms.admin.module.business.oa.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.form.EnterpriseQueryForm;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Mapper
@Component
public interface EnterpriseDao extends BaseMapper<EnterpriseEntity> {

    /**
     * 根据企业名称查询
     *
     * @param enterpriseName
     * @param excludeEnterpriseId
     * @param deletedFlag
     * @return
     */
    EnterpriseEntity queryByEnterpriseName(@Param("enterpriseName") String enterpriseName, @Param("excludeEnterpriseId") Long excludeEnterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    List<String> queryByDomainName(@Param("excludeEnterpriseId") Long excludeEnterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除企业
     *
     * @param enterpriseId
     * @param deletedFlag
     */
    void deleteEnterprise(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 企业分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EnterpriseVO> queryPage(Page page, @Param("queryForm") EnterpriseQueryForm queryForm);

    /**
     * 查询企业详情
     *
     * @param enterpriseId
     * @return
     */
    EnterpriseVO getDetail(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询列表
     *
     * @param type
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<EnterpriseListVO> queryList(@Param("type") Integer type, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询列表
     *
     * @param type
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<EnterpriseListVO> queryNftList(@Param("type") Integer type, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据货主关联的企业ID列表查询
     *
     * @param enterpriseIdList
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<EnterpriseListVO> queryListByEnterpriseIdList(@Param("enterpriseIdList") List<Long> enterpriseIdList, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据企业名称查询
     *
     * @param enterpriseNameList
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<EnterpriseEntity> selectByEnterpriseNameList(@Param("enterpriseNameList") Collection<String> enterpriseNameList, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);


}
