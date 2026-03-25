package net.lab1024.tms.common.module.business.oa.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yandy
 */
@Component
@Mapper
public interface CommonEnterpriseDao extends BaseMapper<EnterpriseEntity> {


    String selectAccessKey(@Param("enterpriseId")Long enterpriseId);


    EnterpriseEntity selectByDomainName(@Param("domainName") String domainName,@Param("disabledFlag") Boolean disabledFlag,  @Param("deletedFlag") Boolean deletedFlag);

    List<EnterpriseEntity> selectEnterpriseList(@Param("disabledFlag") Boolean disabledFlag,  @Param("deletedFlag") Boolean deletedFlag);
}
