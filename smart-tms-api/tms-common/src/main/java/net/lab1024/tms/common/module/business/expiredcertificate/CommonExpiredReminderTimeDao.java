package net.lab1024.tms.common.module.business.expiredcertificate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredReminderTimeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 到期证件 dao
 *
 * @author Turbolisten
 * @date 2022/7/19 11:03
 */
@Mapper
@Component
public interface CommonExpiredReminderTimeDao extends BaseMapper<ExpiredReminderTimeEntity> {

    /**
     * 根据模块类型查询
     *
     * @param enterpriseId
     * @return
     */
    ExpiredReminderTimeEntity selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
