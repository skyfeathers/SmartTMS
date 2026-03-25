package net.lab1024.tms.common.module.business.expiredcertificate;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateEntity;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 到期证件 业务
 *
 * @author Turbolisten
 * @date 2022/7/19 11:03
 */
@Service
public class CommonExpiredCertificateService {

    @Autowired
    private CommomExpiredCertificateDao commomExpiredCertificateDao;

    private static final Interner<String> SYNC = Interners.newWeakInterner();

    /**
     * 证件到期提醒设置 key
     */
    private static final SystemConfigKeyEnum EXPIRE_REMINDER_TIME_CONFIG_KEY = SystemConfigKeyEnum.EXPIRED_CERTIFICATE_EXPIRE_REMINDER_TIME;

    /**
     * 保存/更新 到期证件
     * 其他业务调用 调用者请请注意 一言不合我就给你抛异常
     *
     * @param addDTO
     */
    public void save(ExpiredCertificateAddDTO addDTO) {
        // 校验参数
        String verify = SmartBeanUtil.verify(addDTO);
        if (null != verify) {
            throw new BusinessException("到期证件保存失败：" + verify);
        }

        // build
        Integer moduleType = addDTO.getModuleType().getValue();
        Integer type = addDTO.getType().getValue();
        ExpiredCertificateEntity newCertificateEntity = SmartBeanUtil.copy(addDTO, ExpiredCertificateEntity.class);
        newCertificateEntity.setModuleType(moduleType);
        newCertificateEntity.setType(type);
        // 设置到期状态
        ExpiredCertificateStatusEnum certificateStatusEnum = getStatus(addDTO.getExpiredTime());
        newCertificateEntity.setStatus(certificateStatusEnum.getValue());

        synchronized (SYNC.intern(moduleType + "-" + addDTO.getModuleId() + "-" + type)) {
            // 查询是否已存在
            ExpiredCertificateEntity certificateEntity = commomExpiredCertificateDao.selectByModule(moduleType, addDTO.getModuleId(), type);
            if (null == certificateEntity) {
                // 新增
                commomExpiredCertificateDao.insert(newCertificateEntity);
                return;
            }
            // 更新
            newCertificateEntity.setId(certificateEntity.getId());
            commomExpiredCertificateDao.updateById(newCertificateEntity);
        }
    }

    /**
     * 根据到期时间 返回到期状态
     *
     * @param expiredTime
     * @return
     */
    private static ExpiredCertificateStatusEnum getStatus(LocalDate expiredTime) {
        // 获取当前时间与到期时间之间的天数
        long between = ChronoUnit.DAYS.between(LocalDate.now(), expiredTime);
        if (between <= 0) {
            // 已到期
            return ExpiredCertificateStatusEnum.EXPIRED;
        }
        if (between <= 3) {
            return ExpiredCertificateStatusEnum.EXPIRED_3_DAYS;
        }
        if (between <= 7) {
            return ExpiredCertificateStatusEnum.EXPIRED_7_DAYS;
        }
        if (between <= 15) {
            return ExpiredCertificateStatusEnum.EXPIRED_15_DAYS;
        }
        if (between <= 30) {
            return ExpiredCertificateStatusEnum.EXPIRED_30_DAYS;
        }
        // 未到期
        return ExpiredCertificateStatusEnum.UNEXPIRED;
    }

    public static void main(String[] args) {
        System.out.println(getStatus(SmartLocalDateUtil.parseYMD("2022-06-18")));
    }
}
