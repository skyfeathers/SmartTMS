package net.lab1024.tms.admin.module.business.reportform.employee;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.entity.EmployeeSalesTargetEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工 业绩目标 manager
 *
 * @author Turbolisten
 * @date 2021/8/14 11:26
 */
@Service
public class EmployeeSalesTargetManager extends ServiceImpl<EmployeeSalesTargetDao, EmployeeSalesTargetEntity> {

    /**
     * 更新 目标数据
     *
     * @param insertList
     * @param updateList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateData(List<EmployeeSalesTargetEntity> insertList,
                           List<EmployeeSalesTargetEntity> updateList) {

        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
        }

        if (CollectionUtils.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }

    }

}
