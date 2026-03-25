package net.lab1024.tms.admin.module.business.shipper.service;

import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperInvoiceDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperInvoiceDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperInvoiceAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperInvoiceUpdateForm;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperInvoiceEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/1/22 上午10:48
 */
@Service
public class ShipperInvoiceService {

    @Autowired
    private ShipperInvoiceDao shipperInvoiceDao;

    @Autowired
    private ShipperDao shipperDao;

    @Autowired
    private ShipperDataTracerService shipperDataTracerService;
    /**
     * 查询货主的发票信息
     *
     * @param shipperId
     * @return
     */
    public ResponseDTO<List<ShipperInvoiceDTO>> selectInvoiceByShipperId(Long shipperId) {
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        if (shipperEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<ShipperInvoiceDTO> invoiceList = shipperInvoiceDao.selectByShipperId(shipperId, Boolean.FALSE);
        return ResponseDTO.ok(invoiceList);
    }


    /**
     * 保存发票信息
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> saveInvoice(ShipperInvoiceAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long shipperId = addForm.getShipperId();
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        if (null == shipperEntity || shipperEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "货主不存在");
        }
        ShipperInvoiceEntity shipperInvoiceEntity = SmartBeanUtil.copy(addForm, ShipperInvoiceEntity.class);
        shipperInvoiceDao.insert(shipperInvoiceEntity);
        shipperDataTracerService.saveInvoiceLog(SmartBeanUtil.copy(shipperInvoiceEntity, ShipperInvoiceDTO.class), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新发票信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateInvoice(ShipperInvoiceUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long invoiceId = updateForm.getInvoiceId();
        ShipperInvoiceEntity invoiceEntity = shipperInvoiceDao.selectById(invoiceId);
        if (null == invoiceEntity || invoiceEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ShipperInvoiceEntity shipperInvoiceEntity = SmartBeanUtil.copy(updateForm, ShipperInvoiceEntity.class);
        shipperInvoiceDao.updateById(shipperInvoiceEntity);
        shipperDataTracerService.updateInvoiceLog(SmartBeanUtil.copy(shipperInvoiceEntity, ShipperInvoiceDTO.class), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     * @param invoiceId
     * @return
     */
    public ResponseDTO<String> deleteInvoice(Long invoiceId) {
        ShipperInvoiceEntity invoiceEntity = shipperInvoiceDao.selectById(invoiceId);
        if (null == invoiceEntity || invoiceEntity.getDeletedFlag()) {
            return ResponseDTO.ok();
        }
        shipperInvoiceDao.updateDeletedFlagById(invoiceId, true);
        return ResponseDTO.ok();
    }
}