package net.lab1024.tms.admin.module.business.shipper.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperMailAddressDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperMailAddressDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperMailAddressAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperMailAddressUpdateForm;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperMailAddressEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
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
public class ShipperMailAddressService {

    @Autowired
    private ShipperMailAddressDao shipperMailAddressDao;

    /**
     * 查询
     * @param shipperId
     * @return
     */
    public ResponseDTO<List<ShipperMailAddressDTO>> query(Long shipperId) {
        List<ShipperMailAddressEntity> mailAddressEntityList = shipperMailAddressDao.selectByShipperId(shipperId, false);
        if (CollectionUtils.isEmpty(mailAddressEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<ShipperMailAddressDTO> contactList = SmartBeanUtil.copyList(mailAddressEntityList, ShipperMailAddressDTO.class);
        return ResponseDTO.ok(contactList);
    }

    /**
     * 保存
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(@Valid ShipperMailAddressAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        ShipperMailAddressEntity mailAddressEntity = SmartBeanUtil.copy(addForm, ShipperMailAddressEntity.class);
        shipperMailAddressDao.insert(mailAddressEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(@Valid ShipperMailAddressUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long mailAddressId = updateForm.getMailAddressId();
        ShipperMailAddressEntity mailAddressEntity = shipperMailAddressDao.selectById(mailAddressId);
        if (mailAddressEntity == null || mailAddressEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("邮寄信息不存在");
        }
        ShipperMailAddressEntity updateEntity = SmartBeanUtil.copy(updateForm, ShipperMailAddressEntity.class);
        shipperMailAddressDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     * @param mailAddressId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> delete(Long mailAddressId, DataTracerRequestForm dataTracerRequestForm) {
        ShipperMailAddressEntity mailAddressEntity = shipperMailAddressDao.selectById(mailAddressId);
        if (mailAddressEntity == null || mailAddressEntity.getDeletedFlag()) {
            return ResponseDTO.ok();
        }
        shipperMailAddressDao.updateDeletedFlagById(mailAddressId, true);
        return ResponseDTO.ok();
    }
}