package net.lab1024.tms.common.module.support.dingding.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DingDingConfigDao extends BaseMapper<DingDingConfigEntity> {

    DingDingConfigEntity selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
