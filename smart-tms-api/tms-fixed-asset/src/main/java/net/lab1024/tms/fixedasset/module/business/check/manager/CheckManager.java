package net.lab1024.tms.fixedasset.module.business.check.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.check.dao.CheckDao;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEntity;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 盘点
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:09
 */
@Service
public class CheckManager extends ServiceImpl<CheckDao, CheckEntity> {

    @Autowired
    private CheckDao checkDao;
    @Autowired
    private CheckItemManager checkItemManager;
    @Autowired
    private CheckEmployeeManager checkEmployeeManager;
    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 保存
     *
     * @param checkEntity
     * @param employeeIdList
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(CheckEntity checkEntity, List<Long> employeeIdList, List<Long> assetIdList) {
        checkEntity.setCheckNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_CHECK));
        checkDao.insert(checkEntity);

        List<CheckEmployeeEntity> checkEmployeeList = employeeIdList.stream().map(employeeId -> {
            CheckEmployeeEntity checkEmployeeEntity = new CheckEmployeeEntity();
            checkEmployeeEntity.setCheckId(checkEntity.getCheckId());
            checkEmployeeEntity.setEmployeeId(employeeId);
            return checkEmployeeEntity;
        }).collect(Collectors.toList());
        checkEmployeeManager.saveBatch(checkEmployeeList);

        List<CheckItemEntity> itemList = assetIdList.stream().map(assetId -> {
            CheckItemEntity checkItemEntity = new CheckItemEntity();
            checkItemEntity.setAssetId(assetId);
            checkItemEntity.setCheckId(checkEntity.getCheckId());
            checkItemEntity.setStatus(CheckStatusEnum.NOT_CHECK.getValue());
            return checkItemEntity;
        }).collect(Collectors.toList());
        checkItemManager.saveBatch(itemList);
    }

}
