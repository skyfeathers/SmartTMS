package net.lab1024.tms.admin.module.business.material.cabinet;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.material.cabinet.CabinetEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 14:39
 */
@Service
public class CabinetManage extends ServiceImpl<CabinetDao, CabinetEntity> {

    /**
     * 新增柜型
     * @param insertCabinet
     * @param updateDefaultList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void createCabinet(CabinetEntity insertCabinet, List<CabinetEntity> updateDefaultList){
        save(insertCabinet);
        if(CollectionUtils.isNotEmpty(updateDefaultList)){
            updateBatchById(updateDefaultList);
        }
    }

    /**
     * 编辑柜型
     * @param updateCabinet
     * @param updateDefaultList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateCabinet(CabinetEntity updateCabinet, List<CabinetEntity> updateDefaultList){
        updateById(updateCabinet);
        if(CollectionUtils.isNotEmpty(updateDefaultList)){
            updateBatchById(updateDefaultList);
        }
    }
}
