package net.lab1024.tms.admin.module.business.material.businesstype;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.material.businesstype.BusinessTypeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 14:39
 */
@Service
public class BusinessTypeManage extends ServiceImpl<BusinessTypeDao, BusinessTypeEntity> {

    /**
     * 新增业务类型
     * @param insertBusinessType
     * @param updateDefaultList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void createBusinessType(BusinessTypeEntity insertBusinessType, List<BusinessTypeEntity> updateDefaultList){
        save(insertBusinessType);
        if(CollectionUtils.isNotEmpty(updateDefaultList)){
            updateBatchById(updateDefaultList);
        }
    }

    /**
     * 编辑业务类型
     * @param updateBusinessType
     * @param updateDefaultList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateBusinessType(BusinessTypeEntity updateBusinessType, List<BusinessTypeEntity> updateDefaultList){
        updateById(updateBusinessType);
        if(CollectionUtils.isNotEmpty(updateDefaultList)){
            updateBatchById(updateDefaultList);
        }
    }
}
