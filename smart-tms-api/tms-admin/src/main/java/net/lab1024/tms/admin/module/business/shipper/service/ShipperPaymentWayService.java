package net.lab1024.tms.admin.module.business.shipper.service;

import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPaymentWayDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPaymentWayDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperPaymentWayAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperPaymentWayUpdateForm;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPaymentWayEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/1/22 上午10:48
 */
@Service
public class ShipperPaymentWayService {

    @Autowired
    private ShipperPaymentWayDao shipperPaymentWayDao;

    /**
     * 查询
     * @param shipperId
     * @return
     */
    public ResponseDTO<List<ShipperPaymentWayDTO>> query(Long shipperId) {
        List<ShipperPaymentWayDTO> paymentWayDTOList = shipperPaymentWayDao.selectByShipperId(shipperId, false);
        return ResponseDTO.ok(paymentWayDTOList);
    }

    /**
     * 保存
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(@Valid ShipperPaymentWayAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        ShipperPaymentWayEntity paymentWayEntity = SmartBeanUtil.copy(addForm, ShipperPaymentWayEntity.class);
        shipperPaymentWayDao.insert(paymentWayEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(@Valid ShipperPaymentWayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long paymentWayId = updateForm.getPaymentWayId();
        ShipperPaymentWayEntity paymentWayEntity = shipperPaymentWayDao.selectById(paymentWayId);
        if (paymentWayEntity == null || paymentWayEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ShipperPaymentWayEntity updateEntity = SmartBeanUtil.copy(updateForm, ShipperPaymentWayEntity.class);
        shipperPaymentWayDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     * @param paymentWayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> delete(Long paymentWayId, DataTracerRequestForm dataTracerRequestForm) {
        ShipperPaymentWayEntity paymentWayEntity = shipperPaymentWayDao.selectById(paymentWayId);
        if (paymentWayEntity == null || paymentWayEntity.getDeletedFlag()) {
            return ResponseDTO.ok();
        }
        shipperPaymentWayDao.updateDeletedFlagById(paymentWayId, true);
        return ResponseDTO.ok();
    }
}