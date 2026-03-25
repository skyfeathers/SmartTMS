package net.lab1024.tms.admin.module.business.fleet.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetBankDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;
import net.lab1024.tms.admin.module.business.fleet.domain.form.FleetAddForm;
import net.lab1024.tms.admin.module.business.fleet.domain.form.FleetUpdateForm;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.fleet.domain.FleetBankEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lidoudou
 * @date 2022/6/27 下午5:16
 */
@Service
public class FleetManager extends ServiceImpl<FleetDao, FleetEntity> {

    @Autowired
    private FleetDao fleetDao;

    @Autowired
    private FleetBankManager fleetBankManager;
    @Autowired
    private FleetBankDao fleetBankDao;
    @Autowired
    private DictCacheService dictCacheService;


    @Transactional(rollbackFor = Throwable.class)
    public Long addHandle(FleetAddForm addDTO, Long requestUserId, String requestUserName) {
        FleetEntity fleetEntity = SmartBeanUtil.copy(addDTO, FleetEntity.class);
        fleetEntity.setCreateUserId(requestUserId);
        fleetEntity.setCreateUserName(requestUserName);
        fleetDao.insert(fleetEntity);
        this.saveBank(addDTO.getBankList(), fleetEntity.getFleetId());
        return fleetEntity.getFleetId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateHandle(FleetUpdateForm updateDTO, Long requestUserId, String requestUserName) {
        Long fleetId = updateDTO.getFleetId();

        FleetEntity fleetEntity = SmartBeanUtil.copy(updateDTO, FleetEntity.class);
        fleetEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        fleetEntity.setUpdateUserId(requestUserId);
        fleetEntity.setUpdateUserName(requestUserName);
        fleetDao.updateById(fleetEntity);
        fleetBankManager.getBaseMapper().deleteByFleetId(fleetId);
        this.saveBank(updateDTO.getBankList(), fleetId);
    }

    /**
     * 删除车队
     *
     * @param fleetIdList
     * @param requestUserId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteFleet(List<Long> fleetIdList, Long requestUserId) {
        fleetDao.updateDeletedFlagByFleetId(fleetIdList, Boolean.TRUE, requestUserId);
        fleetBankManager.getBaseMapper().updateDeletedFlagByFleetId(fleetIdList, Boolean.TRUE);
    }

    /**
     * 删除关联信息
     *
     * @param fleetId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRelateInfo(Long fleetId) {
        fleetBankManager.getBaseMapper().deleteByFleetId(fleetId);
    }

    /**
     * 保存
     *
     * @param bankList
     * @param fleetId
     */
    private void saveBank(List<FleetBankDTO> bankList, Long fleetId) {
        if (CollectionUtils.isEmpty(bankList)) {
            return;
        }
        List<FleetBankEntity> bankEntityList = SmartBeanUtil.copyList(bankList, FleetBankEntity.class);
        Boolean defaultFlag = Boolean.FALSE;
        for (FleetBankEntity e : bankEntityList) {
            e.setFleetId(fleetId);
            if (defaultFlag) {
                e.setDefaultFlag(Boolean.FALSE);
            } else {
                defaultFlag = e.getDefaultFlag();
            }
            String bankName = StringConst.EMPTY;
            if (SmartStringUtil.isNotBlank(e.getBankType())) {
                bankName = dictCacheService.selectValueNameByValueCode(e.getBankType());
            }
            e.setBankName(bankName);
        }
        fleetBankManager.saveBatch(bankEntityList);
    }


    /**
     * 添加银行
     * @param fleetBankEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void bankAdd(FleetBankEntity fleetBankEntity) {
        Long fleetId = fleetBankEntity.getFleetId();
        FleetBankEntity defaultBank = fleetBankDao.selectDefaultByFleetId(fleetId,true);
        if(defaultBank == null){
            fleetBankDao.insert(fleetBankEntity);
            return;
        }
        FleetBankEntity updateDefaultBank = new FleetBankEntity();
        updateDefaultBank.setDefaultFlag(false);
        updateDefaultBank.setBankId(defaultBank.getBankId());
        fleetBankDao.updateById(updateDefaultBank);

        fleetBankDao.insert(fleetBankEntity);
    }
}
