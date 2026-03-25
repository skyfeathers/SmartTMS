package net.lab1024.tms.admin.module.business.oa.enterprise.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseBusinessSettingDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.form.BusinessSettingUpdateForm;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseBusinessSettingVO;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.BusinessSettingEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseBusinessSettingEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午3:21
 */
@Slf4j
@Service
public class EnterpriseBusinessSettingService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EnterpriseBusinessSettingDao enterpriseBusinessSettingDao;

    /**
     * 获取设置信息
     *
     * @param enterpriseId
     * @param settingKey
     * @return
     */
    public ResponseDTO<EnterpriseBusinessSettingVO> getSetting(Long enterpriseId, String settingKey) {
        EnterpriseBusinessSettingEntity entity = getSettingByEnterpriseAndKey(enterpriseId, settingKey);
        if (null == entity) {
            return ResponseDTO.ok();
        }
        return ResponseDTO.ok(SmartBeanUtil.copy(entity, EnterpriseBusinessSettingVO.class));
    }

    public EnterpriseBusinessSettingEntity getSettingByEnterpriseAndKey(Long enterpriseId, String settingKey) {
        EnterpriseBusinessSettingEntity entity = enterpriseBusinessSettingDao.selectByKey(enterpriseId, settingKey);
        return entity;
    }


    /**
     * 更新业务设置
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> saveSetting(BusinessSettingUpdateForm updateForm) {
        Long enterpriseId = updateForm.getEnterpriseId();
        if (null == enterpriseDao.selectById(enterpriseId)) {
            return ResponseDTO.userErrorParam("公司不存在");
        }
        if (!SmartBaseEnumUtil.checkEnum(updateForm.getSettingKey(), BusinessSettingEnum.class)) {
            return ResponseDTO.userErrorParam("设置类型不存在");
        }

        EnterpriseBusinessSettingEntity alreadyEntity = enterpriseBusinessSettingDao.selectByKey(enterpriseId, updateForm.getSettingKey());
        if (null != alreadyEntity) {
            alreadyEntity.setSettingValue(updateForm.getSettingValue());
            enterpriseBusinessSettingDao.updateById(alreadyEntity);
            return ResponseDTO.ok();
        }
        // 设置内容不存在，则新增
        EnterpriseBusinessSettingEntity insertEntity = SmartBeanUtil.copy(updateForm, EnterpriseBusinessSettingEntity.class);
        insertEntity.setSettingName(SmartBaseEnumUtil.getEnumDescByValue(updateForm.getSettingKey(), BusinessSettingEnum.class));
        enterpriseBusinessSettingDao.insert(insertEntity);
        return ResponseDTO.ok();
    }

    public Boolean valueContain(Long enterpriseId, BusinessSettingEnum businessSettingEnum, Long employeeId){
        EnterpriseBusinessSettingEntity enterpriseBusinessSettingEntity = this.getSettingByEnterpriseAndKey(enterpriseId, businessSettingEnum.getValue());
        if (enterpriseBusinessSettingEntity == null){
            return false;
        }
        String settingValue = enterpriseBusinessSettingEntity.getSettingValue();
        if (StringUtils.isBlank(settingValue)) {
            return false;
        }
        List<Long> employeeIdList = SmartStringUtil.splitConverToLongList(settingValue, ",");
        return employeeIdList.contains(employeeId);
    }

}
