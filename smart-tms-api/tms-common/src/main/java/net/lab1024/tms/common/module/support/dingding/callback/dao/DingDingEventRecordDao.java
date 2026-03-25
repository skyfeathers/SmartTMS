package net.lab1024.tms.common.module.support.dingding.callback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.dingding.callback.domain.entity.DingDingEventRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DingDingEventRecordDao extends BaseMapper<DingDingEventRecordEntity> {
}
