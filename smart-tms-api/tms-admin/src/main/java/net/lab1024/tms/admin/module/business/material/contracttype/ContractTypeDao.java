package net.lab1024.tms.admin.module.business.material.contracttype;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.contracttype.domain.ContractListVO;
import net.lab1024.tms.admin.module.business.material.contracttype.domain.ContractTypeQueryForm;
import net.lab1024.tms.admin.module.business.material.contracttype.domain.ContractTypeVO;
import net.lab1024.tms.common.module.business.material.contracttype.ContractTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */

@Mapper
@Component
public interface ContractTypeDao extends BaseMapper<ContractTypeEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryForm
     * @return
     */
    List<ContractTypeVO> queryPage(Page<?> page, @Param("queryForm") ContractTypeQueryForm queryForm);

    List<ContractListVO> queryAll(@Param("deletedFlag") Boolean deletedFlag);

    /**
     * 修改删除状态
     *
     * @param contractTypeId
     * @param deletedFlag
     */
    void updateDeleteFlag(@Param("contractTypeId") Long contractTypeId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 名称查询
     *
     * @param name
     * @param excludeIds
     * @param deletedFlag
     * @return
     */
    List<ContractTypeEntity> selectByNameAndExcludeIds(@Param("name") String name, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

}
