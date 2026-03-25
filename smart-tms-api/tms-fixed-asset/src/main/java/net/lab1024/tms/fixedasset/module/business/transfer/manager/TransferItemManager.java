package net.lab1024.tms.fixedasset.module.business.transfer.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.transfer.dao.TransferItemDao;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferItemEntity;
import org.springframework.stereotype.Service;

/**
 * 借用-归还manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class TransferItemManager extends ServiceImpl<TransferItemDao, TransferItemEntity> {

}
