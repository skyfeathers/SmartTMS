package net.lab1024.tms.common.module.support.sms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.sms.domain.SmsRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 短信记录 dao
 *
 * @author Turbolisten
 * @date 2020/3/16 17:00
 */
@Component
@Mapper
public interface SmsRecordDao extends BaseMapper<SmsRecordEntity> {
}
