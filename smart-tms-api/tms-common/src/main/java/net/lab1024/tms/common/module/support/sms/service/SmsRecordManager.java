package net.lab1024.tms.common.module.support.sms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.support.sms.SmsRecordDao;
import net.lab1024.tms.common.module.support.sms.domain.SmsRecordEntity;
import org.springframework.stereotype.Service;

/**
 * 短信记录 mapper
 *
 * @author lihaifan
 * @date 2022/2/11 11:26
 */
@Service
public class SmsRecordManager extends ServiceImpl<SmsRecordDao, SmsRecordEntity> {
}
