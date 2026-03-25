package net.lab1024.tms.admin.module.business.shipper.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperContactDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperContactDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperContactAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperContactUpdateForm;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperContactEntity;
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
public class ShipperContactService {

    @Autowired
    private ShipperContactDao shipperContactDao;

    /**
     * 查询货主下的联系人
     * @param shipperId
     * @return
     */
    public ResponseDTO<List<ShipperContactDTO>> query(Long shipperId) {
        List<ShipperContactEntity> contactEntityList = shipperContactDao.selectByShipperId(shipperId, false);
        if (CollectionUtils.isEmpty(contactEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<ShipperContactDTO> contactList = SmartBeanUtil.copyList(contactEntityList, ShipperContactDTO.class);
        return ResponseDTO.ok(contactList);
    }

    /**
     * 保存
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(@Valid ShipperContactAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        ShipperContactEntity shipperContactEntity = SmartBeanUtil.copy(addForm, ShipperContactEntity.class);
        shipperContactDao.insert(shipperContactEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(@Valid ShipperContactUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long contactId = updateForm.getContactId();
        ShipperContactEntity contactEntity = shipperContactDao.selectById(contactId);
        if (contactEntity == null || contactEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("联系人不存在");
        }
        ShipperContactEntity updateEntity = SmartBeanUtil.copy(updateForm, ShipperContactEntity.class);
        shipperContactDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     * @param contactId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> delete(Long contactId, DataTracerRequestForm dataTracerRequestForm) {
        ShipperContactEntity contactEntity = shipperContactDao.selectById(contactId);
        if (contactEntity == null || contactEntity.getDeletedFlag()) {
            return ResponseDTO.ok();
        }
        shipperContactDao.updateDeletedFlagById(contactId, true);
        return ResponseDTO.ok();
    }
}