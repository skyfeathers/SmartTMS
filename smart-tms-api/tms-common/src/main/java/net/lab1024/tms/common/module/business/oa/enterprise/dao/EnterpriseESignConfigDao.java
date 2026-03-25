package net.lab1024.tms.common.module.business.oa.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseESignConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * OA企业e签宝配置模块
 *
 * @author lihaifan
 * @date 2022/9/26 16:11
 */
@Mapper
@Component
public interface EnterpriseESignConfigDao extends BaseMapper<EnterpriseESignConfigEntity> {

    /**
     * 根据企业ID查询配置
     * @param enterpriseId
     * @return
     */
    EnterpriseESignConfigEntity getByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
