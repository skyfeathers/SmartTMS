package net.lab1024.tms.common.module.support.dingding.config;

import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigForm;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigVO;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 钉钉配置
 *
 * @author lidoudou
 * @date 2023/4/28 下午3:53
 */
@Service
public class DingDingConfigService {

    @Autowired
    private DingDingConfigDao dingDingConfigDao;

    public ResponseDTO<String> saveConfig(DingDingConfigForm configForm) {
        Long enterpriseId = configForm.getEnterpriseId();
        DingDingConfigEntity dingDingConfigEntity = dingDingConfigDao.selectByEnterpriseId(enterpriseId);
        if (null == dingDingConfigEntity) {
            dingDingConfigEntity = SmartBeanUtil.copy(configForm, DingDingConfigEntity.class);
            dingDingConfigDao.insert(dingDingConfigEntity);
        } else {
            dingDingConfigEntity = SmartBeanUtil.copy(configForm, DingDingConfigEntity.class);
            dingDingConfigDao.updateById(dingDingConfigEntity);
        }
        return ResponseDTO.ok();
    }

    /**
     * 根据企业获取配置
     *
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<DingDingConfigVO> getByEnterpriseId(Long enterpriseId) {
        DingDingConfigVO dingDingConfigVO = new DingDingConfigVO();

        DingDingConfigEntity dingDingConfigEntity = dingDingConfigDao.selectByEnterpriseId(enterpriseId);
        if (null != dingDingConfigEntity) {
            dingDingConfigVO = SmartBeanUtil.copy(dingDingConfigEntity, DingDingConfigVO.class);
        }
        return ResponseDTO.ok(dingDingConfigVO);
    }
}
