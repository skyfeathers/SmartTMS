package net.lab1024.tms.admin.module.business.oa.cost.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.oa.cost.dao.CostApplyDao;
import net.lab1024.tms.admin.module.business.oa.cost.dao.CostApplyItemDao;
import net.lab1024.tms.admin.module.business.oa.cost.domain.entity.CostApplyEntity;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyItemAddForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:59
 */
@Service
public class CostApplyManager extends ServiceImpl<CostApplyDao, CostApplyEntity> {

    @Autowired
    private CostApplyItemDao costApplyItemDao;

    @Transactional(rollbackFor = Exception.class)
    public void save(CostApplyEntity costApplyEntity, List<CostApplyItemAddForm> itemList) {
        getBaseMapper().insert(costApplyEntity);
        costApplyItemDao.batchInsertApplyItem(costApplyEntity.getApplyId(), itemList);
    }

}
