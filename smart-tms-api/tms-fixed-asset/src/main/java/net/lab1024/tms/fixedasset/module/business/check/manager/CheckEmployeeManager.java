package net.lab1024.tms.fixedasset.module.business.check.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.check.dao.CheckEmployeeDao;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEmployeeEntity;
import org.springframework.stereotype.Service;

/**
 * 盘点人
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:11
 */
@Service
public class CheckEmployeeManager extends ServiceImpl<CheckEmployeeDao, CheckEmployeeEntity> {

}
