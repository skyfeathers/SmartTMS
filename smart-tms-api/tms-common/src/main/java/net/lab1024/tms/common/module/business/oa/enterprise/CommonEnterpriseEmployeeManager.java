package net.lab1024.tms.common.module.business.oa.enterprise;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseEmployeeDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEmployeeEntity;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/8/21 3:26 下午
 */
@Service
public class CommonEnterpriseEmployeeManager extends ServiceImpl<CommonEnterpriseEmployeeDao, EnterpriseEmployeeEntity> {
}