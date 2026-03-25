package net.lab1024.tms.driver.module.business.waybill.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillVoucherEntity;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillVoucherDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class WaybillManager extends ServiceImpl<WaybillDao, WaybillEntity> {
    
    @Resource
    private WaybillDao waybillDao;
    
    @Resource
    private WaybillVoucherDao waybillVoucherDao;
    
    /**
     * 保存运输凭证并设置运单装卸货时间
     *
     * @param waybillVoucherEntity
     * @param waybillEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void addVoucher(WaybillVoucherEntity waybillVoucherEntity, WaybillEntity waybillEntity) {
        waybillVoucherDao.insert(waybillVoucherEntity);
    
        Integer voucherType = waybillVoucherEntity.getType();
        if (WaybillVoucherTypeEnum.LOAD.equalsValue(voucherType) && ObjectUtil.isEmpty(waybillEntity.getDeliverGoodsTime())) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillEntity.getWaybillId());
            updateWaybillEntity.setDeliverGoodsTime(waybillVoucherEntity.getCreateTime());
            waybillDao.updateById(updateWaybillEntity);
        }
        if (WaybillVoucherTypeEnum.UNLOAD.equalsValue(voucherType) && ObjectUtil.isEmpty(waybillEntity.getReceiveGoodsTime())) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillEntity.getWaybillId());
            updateWaybillEntity.setReceiveGoodsTime(waybillVoucherEntity.getCreateTime());
            updateWaybillEntity.setWaybillStatus(WaybillStatusEnum.COMPLETE.getValue());
            waybillDao.updateById(updateWaybillEntity);
        }
    }
}
