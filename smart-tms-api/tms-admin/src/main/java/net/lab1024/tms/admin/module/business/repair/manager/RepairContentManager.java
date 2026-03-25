package net.lab1024.tms.admin.module.business.repair.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.repair.dao.RepairContentDao;
import net.lab1024.tms.common.module.business.repair.entity.RepairContentEntity;
import org.springframework.stereotype.Service;

@Service
public class RepairContentManager extends ServiceImpl<RepairContentDao, RepairContentEntity> {
}
