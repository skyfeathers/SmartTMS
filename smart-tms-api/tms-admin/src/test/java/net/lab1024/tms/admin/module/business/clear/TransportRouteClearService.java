package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.material.transportroute.TransportRouteDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/31 上午8:39
 */
@Service
public class TransportRouteClearService {

    @Autowired
    private TransportRouteDao transportRouteDao;

    void clear(List<Long> enterpriseIdList) {
        QueryWrapper employeeQw = new QueryWrapper();
        employeeQw.in("enterprise_id", enterpriseIdList);
        transportRouteDao.delete(employeeQw);

    }
}