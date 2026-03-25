package net.lab1024.tms.admin.module.business.oilcard.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oilcard.dao.BalanceRecordDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceChangeForm;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardBalanceRecordEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/19 3:51 下午
 */
@Service
public class OilCardBalanceService {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private BalanceRecordDao balanceRecordDao;
    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 余额变动
     * 副卡余额影响主卡余额，主卡不会影响副卡
     *
     * @param balanceChangeDTO
     */
    public Long balanceChange(OilCardBalanceChangeForm balanceChangeDTO) {
        OilCardEntity oilCardEntity = oilCardDao.selectById(balanceChangeDTO.getOilCardId());
        if (oilCardEntity == null) {
            throw new BusinessException("油卡不存在");
        }
        String lock = "oilCardBalanceChange_" + balanceChangeDTO.getOilCardId();
        synchronized (StringConst.STRING_POOL.intern(lock)) {
            OilCardEntity relationOilCardEntity = null;
            if (OilCardTypeEnum.SLAVE.equalsValue(oilCardEntity.getType())) {
                relationOilCardEntity = oilCardDao.selectById(oilCardEntity.getMasterOilCardId());
            }
            //余额变动
            return this.balanceChangeHandle(oilCardEntity, relationOilCardEntity, balanceChangeDTO);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public Long balanceChangeHandle(OilCardEntity oilCardEntity, OilCardEntity relationOilCardEntity, OilCardBalanceChangeForm balanceChangeDTO) {
        // 直接操作的油卡
        Long recordId = this.oilCardChangeBalanceAndRecord(oilCardEntity, balanceChangeDTO);

        if (BooleanUtils.isTrue(balanceChangeDTO.getDisabledOperateCardFlag())) {
            OilCardEntity cancelEntity = new OilCardEntity();
            cancelEntity.setOilCardId(oilCardEntity.getOilCardId());
            cancelEntity.setDisabledFlag(Boolean.TRUE);
            oilCardDao.updateById(cancelEntity);
        }
        if (relationOilCardEntity != null) {
            //副卡增加 主卡减少
            OilCardBalanceChangeForm relationChangeForm = SmartBeanUtil.copy(balanceChangeDTO, OilCardBalanceChangeForm.class);
            relationChangeForm.setChangeAmount(balanceChangeDTO.getChangeAmount().negate());
            Long relationRecordId = this.oilCardChangeBalanceAndRecord(relationOilCardEntity, relationChangeForm);
            //修改各记录关联记录id
            OilCardBalanceRecordEntity updateRecordEntity1 = new OilCardBalanceRecordEntity();
            updateRecordEntity1.setBalanceRecordId(recordId);
            updateRecordEntity1.setRelatedRecordId(relationRecordId);
            balanceRecordDao.updateById(updateRecordEntity1);

            OilCardBalanceRecordEntity updateRecordEntity2 = new OilCardBalanceRecordEntity();
            updateRecordEntity2.setBalanceRecordId(relationRecordId);
            updateRecordEntity2.setRelatedRecordId(recordId);
            balanceRecordDao.updateById(updateRecordEntity2);
        }
        return recordId;
    }

    private Long oilCardChangeBalanceAndRecord(OilCardEntity oilCardEntity, OilCardBalanceChangeForm balanceChangeDTO) {
        //余额计算
        BigDecimal changeAmount = balanceChangeDTO.getChangeAmount();
        BigDecimal beforeAmount = oilCardEntity.getBalance();
        BigDecimal afterAmount = beforeAmount.add(changeAmount);
        //流水号,记录类型
        String serialNumber = serialNumberService.generate(SerialNumberIdEnum.OIL_CARD_BALANCE_RECORD);
        OilCardBalanceRecordTypeEnum recordTypeEnum = SmartBaseEnumUtil.getEnumByValue(balanceChangeDTO.getRecordType(), OilCardBalanceRecordTypeEnum.class);
        //构建流水记录
        OilCardBalanceRecordEntity oilCardBalanceRecordEntity = new OilCardBalanceRecordEntity();
        oilCardBalanceRecordEntity.setBalanceRecordNo(serialNumber);
        oilCardBalanceRecordEntity.setOilCardId(oilCardEntity.getOilCardId());
        oilCardBalanceRecordEntity.setOilCardType(oilCardEntity.getType());

        oilCardBalanceRecordEntity.setRecordType(recordTypeEnum.getValue());
        oilCardBalanceRecordEntity.setRecordTypeDesc(recordTypeEnum.getDesc());

        oilCardBalanceRecordEntity.setBeforeBalance(beforeAmount);
        oilCardBalanceRecordEntity.setChangeBalance(changeAmount);
        oilCardBalanceRecordEntity.setAfterBalance(afterAmount);
        oilCardBalanceRecordEntity.setRemark(balanceChangeDTO.getRemark());
        oilCardBalanceRecordEntity.setCreateUserId(balanceChangeDTO.getOperatorId());
        oilCardBalanceRecordEntity.setCreateUserName(balanceChangeDTO.getOperatorName());
        // 如果是充值类型，则记录交易时间，其他类型交易时间默认为当前时间
        if(OilCardBalanceRecordTypeEnum.RECHARGE.getValue().equals(balanceChangeDTO.getRecordType())){
            oilCardBalanceRecordEntity.setTransactionTime(balanceChangeDTO.getTransactionTime());
        }
        //修改余额
        OilCardEntity updateOilCardEntity = new OilCardEntity();
        updateOilCardEntity.setOilCardId(oilCardEntity.getOilCardId());
        updateOilCardEntity.setBalance(afterAmount);

        balanceRecordDao.insert(oilCardBalanceRecordEntity);
        oilCardDao.updateById(updateOilCardEntity);

        return oilCardBalanceRecordEntity.getBalanceRecordId();
    }
}