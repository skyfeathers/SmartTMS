package net.lab1024.tms.admin.module.business.expiredcertificate;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.*;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateEntity;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigUpdateForm;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 到期证件 业务
 *
 * @author Turbolisten
 * @date 2022/7/19 11:03
 */
@Service
public class ExpiredCertificateService {

    @Autowired
    private ExpiredCertificateDao expiredCertificateDao;

    @Autowired
    private SystemConfigService systemConfigService;

    private static final Interner<String> SYNC = Interners.newWeakInterner();

    /**
     * 证件到期提醒设置 key
     */
    private static final SystemConfigKeyEnum EXPIRE_REMINDER_TIME_CONFIG_KEY = SystemConfigKeyEnum.EXPIRED_CERTIFICATE_EXPIRE_REMINDER_TIME;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ExpiredCertificateVO>> query(ExpiredCertificateQueryForm queryForm) {
        // 分页查询
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ExpiredCertificateVO> list = expiredCertificateDao.query(page, queryForm);
        PageResult<ExpiredCertificateVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询 - 到期提醒时间
     *
     * @return
     */
    public ResponseDTO<ExpiredCertificateReminderTimeDTO> queryReminderTime() {
        String value = systemConfigService.getConfigValue(EXPIRE_REMINDER_TIME_CONFIG_KEY);
        ExpiredCertificateReminderTimeDTO timeDTO = JSON.parseObject(value, ExpiredCertificateReminderTimeDTO.class);
        return ResponseDTO.ok(timeDTO);
    }

    /**
     * 更新 - 到期提醒时间
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateReminderTime(ExpiredCertificateReminderTimeDTO updateForm) {
        updateForm.setUpdateTime(LocalDateTime.now());
        // 更新设置
        SystemConfigVO configVO = systemConfigService.getConfig(EXPIRE_REMINDER_TIME_CONFIG_KEY);
        SystemConfigUpdateForm configUpdateForm = new SystemConfigUpdateForm();
        configUpdateForm.setSystemConfigId(configVO.getSystemConfigId());
        configUpdateForm.setConfigKey(EXPIRE_REMINDER_TIME_CONFIG_KEY.getValue());
        configUpdateForm.setConfigValue(JSON.toJSONString(updateForm));
        configUpdateForm.setConfigName(EXPIRE_REMINDER_TIME_CONFIG_KEY.getDesc());
        systemConfigService.updateSystemConfig(configUpdateForm);
        return ResponseDTO.ok();
    }

    /**
     * 到期证件删除
     *
     * @param delDTO
     */
    public void del(ExpiredCertificateDelDTO delDTO) {
        // 校验参数
        String verify = SmartBeanUtil.verify(delDTO);
        if (null != verify) {
            throw new BusinessException("到期证件删除失败：" + verify);
        }
        Integer moduleType = delDTO.getModuleType().getValue();

        ExpiredCertificateTypeEnum expiredCertificateTypeEnum = delDTO.getType();
        if (expiredCertificateTypeEnum != null) {
            this.delByType(moduleType, delDTO.getModuleId(), expiredCertificateTypeEnum.getValue());
        } else {
            expiredCertificateDao.deleteByModuleId(moduleType, delDTO.getModuleId());
        }

    }

    private void delByType(Integer moduleType, Long moduleId, Integer type) {
        ExpiredCertificateEntity certificateEntity = expiredCertificateDao.selectByModule(moduleType, moduleId, type);
        if (null == certificateEntity) {
            return;
        }
        // 删除
        expiredCertificateDao.deleteById(certificateEntity.getId());
    }


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
            ExpiredCertificateEntity certificateEntity = expiredCertificateDao.selectByModule(moduleType, addDTO.getModuleId(), type);
            if (null == certificateEntity) {
                // 新增
                expiredCertificateDao.insert(newCertificateEntity);
                return;
            }
            // 更新
            newCertificateEntity.setId(certificateEntity.getId());
            expiredCertificateDao.updateById(newCertificateEntity);
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
