package net.lab1024.tms.common.module.support.dingding.sync;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingEmployeeDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingEmployeeEntity;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/8/21 3:26 下午
 */
@Service
public class DingDingEmployeeManager extends ServiceImpl<DingDingEmployeeDao, DingDingEmployeeEntity> {
}