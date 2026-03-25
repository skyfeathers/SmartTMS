package net.lab1024.tms.admin.module.business.material.businesstype;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeQueryForm;
import net.lab1024.tms.admin.module.business.material.businesstype.domain.BusinessTypeVO;
import net.lab1024.tms.common.module.business.material.businesstype.BusinessTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component
public interface BusinessTypeDao extends BaseMapper<BusinessTypeEntity> {

    /**
     * 根据业务代码查询
     *
     * @param businessTypeCode
     * @param excludeBusinessTypeId
     * @param deletedFlag
     * @return
     */
    BusinessTypeEntity queryByBusinessTypeCode(@Param("enterpriseId") Long enterpriseId, @Param("businessTypeCode") String businessTypeCode, @Param("excludeBusinessTypeId") Long excludeBusinessTypeId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据业务代码查询
     *
     * @param businessTypeName
     * @param excludeBusinessTypeId
     * @param deletedFlag
     * @return
     */
    BusinessTypeEntity queryByBusinessTypeName(@Param("enterpriseId") Long enterpriseId, @Param("businessTypeName") String businessTypeName, @Param("excludeBusinessTypeId") Long excludeBusinessTypeId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除业务类型
     *
     * @param businessTypeId
     * @param deletedFlag
     */
    void deleteBusinessType(@Param("businessTypeId") Long businessTypeId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 业务类型分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<BusinessTypeVO> queryPage(Page page, @Param("queryForm") BusinessTypeQueryForm queryForm);


    /**
     * 业务类型查询
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<BusinessTypeVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 查询业务类型详情
     *
     * @param businessTypeId
     * @return
     */
    BusinessTypeVO getDetail(@Param("businessTypeId") Long businessTypeId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询默认业务类型
     *
     * @param defaultFlag
     * @param deletedFlag
     * @param excludeBusinessTypeId
     * @return
     */
    List<BusinessTypeEntity> queryByDefault(@Param("enterpriseId") Long enterpriseId, @Param("defaultFlag") Boolean defaultFlag, @Param("deletedFlag") Boolean deletedFlag, @Param("excludeBusinessTypeId") Long excludeBusinessTypeId);


    /**
     * 业务类型查询
     *
     * @param businessTypeNameList
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<BusinessTypeEntity> selectByBusinessTypeNameList(@Param("enterpriseId") Long enterpriseId, @Param("businessTypeNameList") Collection<String> businessTypeNameList, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
