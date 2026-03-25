package net.lab1024.tms.job.module.oa.enterprise;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseBusinessSettingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OA企业信息业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午2:35
 */
@Component
@Mapper
public interface EnterpriseBusinessSettingDao extends BaseMapper<EnterpriseBusinessSettingEntity> {

    /**
     * 根据key查询获取数据
     *
     * @param settingKey
     * @return
     */
    List<EnterpriseBusinessSettingEntity> selectByKey(@Param("settingKey") String settingKey);
}
