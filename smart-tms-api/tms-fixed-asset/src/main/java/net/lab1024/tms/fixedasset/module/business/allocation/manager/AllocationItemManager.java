package net.lab1024.tms.fixedasset.module.business.allocation.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.allocation.dao.AllocationItemDao;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationItemEntity;
import org.springframework.stereotype.Service;

/**
 * 借用-归还manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class AllocationItemManager extends ServiceImpl<AllocationItemDao, AllocationItemEntity> {

}
