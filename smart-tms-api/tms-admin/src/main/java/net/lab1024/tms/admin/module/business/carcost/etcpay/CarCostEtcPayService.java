package net.lab1024.tms.admin.module.business.carcost.etcpay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.etcpay.domain.CarCostEtcPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.etcpay.domain.CarCostEtcPayVO;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayUpdateForm;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostEtcPayService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;
    @Resource
    private CarCostEtcPayManager carCostEtcPayManager;
    @Resource
    private CarCostEtcPayDataTracerService carCostEtcPayDataTracerService;

    /**
     * 根据运单ID查询ETC支出列表
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostEtcPayVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "运单不存在");
        }
        List<CarCostEtcPayVO> etcPayVOList = carCostEtcPayDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(etcPayVOList);
    }

    /**
     * 新建ETC支出
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostEtcPayAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = addForm.getWaybillId();
        ResponseDTO<WaybillEntity> resp = carCostCheckService.checkParam(waybillId);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }

        WaybillEntity waybillEntity = resp.getData();
        if (!addForm.getEnterpriseId().equals(waybillEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("运单所属企业和当前登录企业不一致");
        }

        Long vehicleId = waybillEntity.getVehicleId();
        Long driverId = waybillEntity.getDriverId();
        Long categoryId = addForm.getCategoryId();

        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.ETC_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        if (StringUtils.isBlank(addForm.getAttachment())
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(categoryEntity.getPayMode())) {
            return ResponseDTO.userErrorParam("附件不能为空");
        }
        Integer payMode = categoryEntity.getPayMode();
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(payMode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostEtcPayEntity etcPayEntity = new CarCostEtcPayEntity();
        etcPayEntity.setEnterpriseId(addForm.getEnterpriseId());
        etcPayEntity.setWaybillId(waybillId);
        etcPayEntity.setVehicleId(vehicleId);
        etcPayEntity.setDriverId(driverId);
        etcPayEntity.setCategoryId(categoryId);
        etcPayEntity.setCostType(categoryEntity.getCostType());
        etcPayEntity.setCategoryName(categoryEntity.getCategoryName());
        etcPayEntity.setPayMode(payMode);
        etcPayEntity.setAmount(addForm.getAmount());
        etcPayEntity.setAttachment(addForm.getAttachment());
        etcPayEntity.setRemark(addForm.getRemark());
        etcPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        etcPayEntity.setAuditRemark("系统自动审核通过");
        etcPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        etcPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        etcPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        etcPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.ETC_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setAuditRemark("系统自动审核通过");
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostEtcPayManager.handleAdd(etcPayEntity, tabulationEntity);
        carCostEtcPayDataTracerService.saveEtcPayLog(etcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑ETC支出
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostEtcPayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long etcPayId = updateForm.getEtcPayId();
        CarCostEtcPayEntity beforeEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (ObjectUtil.isEmpty(beforeEtcPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }

        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeEtcPayEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }
        Long categoryId = updateForm.getCategoryId();
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.ETC_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();
        if (StringUtils.isBlank(updateForm.getAttachment())
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(categoryEntity.getPayMode())) {
            return ResponseDTO.userErrorParam("附件不能为空");
        }

        Integer payMode = categoryEntity.getPayMode();
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(payMode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        Integer auditStatus = beforeEtcPayEntity.getAuditStatus();
        CarCostEtcPayEntity etcPayEntity = new CarCostEtcPayEntity();
        etcPayEntity.setEtcPayId(etcPayId);
        etcPayEntity.setWaybillId(waybillId);
        etcPayEntity.setVehicleId(vehicleId);
        etcPayEntity.setDriverId(driverId);
        etcPayEntity.setCategoryId(categoryId);
        etcPayEntity.setCostType(categoryEntity.getCostType());
        etcPayEntity.setCategoryName(categoryEntity.getCategoryName());
        etcPayEntity.setPayMode(categoryEntity.getPayMode());
        etcPayEntity.setAmount(updateForm.getAmount());
        etcPayEntity.setAttachment(updateForm.getAttachment());
        etcPayEntity.setRemark(updateForm.getRemark());
        etcPayEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.ETC_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(etcPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.ETC_PAY.getValue());
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);

        carCostEtcPayManager.handleUpdate(etcPayEntity, beforeEtcPayEntity, tabulationEntity);
        CarCostEtcPayEntity afterEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        carCostEtcPayDataTracerService.updateEtcPayLog(beforeEtcPayEntity, afterEtcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * ETC支出审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostEtcPayAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        Long etcPayId = auditForm.getEtcPayId();
        CarCostEtcPayEntity beforePayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (ObjectUtil.isEmpty(beforePayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        if (auditStatus.equals(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用已处于"+auditStatusEnum.getDesc()+"状态");
        }

        Long vehicleId = beforePayEntity.getVehicleId();
        Long driverId = beforePayEntity.getDriverId();
        Long waybillId = beforePayEntity.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        CarCostEtcPayEntity etcPayEntity = new CarCostEtcPayEntity();
        etcPayEntity.setWaybillId(waybillId);
        etcPayEntity.setEtcPayId(etcPayId);
        etcPayEntity.setAuditStatus(auditStatus);
        etcPayEntity.setAuditRemark(auditForm.getAuditRemark());

        Integer moduleType = CarCostModuleTypeEnum.ETC_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(etcPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        carCostEtcPayManager.handleAudit(beforePayEntity, etcPayEntity, tabulationEntity);
        CarCostEtcPayEntity afterEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        carCostEtcPayDataTracerService.auditEtcPayLog(afterEtcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param etcPayId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long etcPayId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostEtcPayEntity dbEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (ObjectUtil.isEmpty(dbEtcPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        if (dbEtcPayEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }

        Long vehicleId = dbEtcPayEntity.getVehicleId();
        Long driverId = dbEtcPayEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkWaybill(waybillId, vehicleId, driverId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        CarCostEtcPayEntity etcPayEntity = new CarCostEtcPayEntity();
        etcPayEntity.setEtcPayId(etcPayId);
        etcPayEntity.setWaybillId(waybillId);
        etcPayEntity.setPayMode(dbEtcPayEntity.getPayMode());
        etcPayEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.ETC_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(etcPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostEtcPayManager.handleRelateWaybill(etcPayEntity, tabulationEntity, dbEtcPayEntity);
        carCostEtcPayDataTracerService.relateWaybillLog(vehicleId, etcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param etcPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long etcPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostEtcPayEntity dbEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (dbEtcPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        if (dbEtcPayEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.ETC_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(etcPayId, moduleType);

        carCostEtcPayManager.handleCancelRelateWaybill(etcPayId, tabulationId, dbEtcPayEntity);
        carCostEtcPayDataTracerService.cancelRelateWaybillLog(dbEtcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> del(Long etcPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostEtcPayEntity dbEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (dbEtcPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(etcPayId, CarCostModuleTypeEnum.ETC_PAY.getValue());
        carCostEtcPayManager.del(tabulationEntity, dbEtcPayEntity);
        carCostEtcPayDataTracerService.delEtcPayLog(dbEtcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param etcPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long etcPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostEtcPayEntity dbEtcPayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (dbEtcPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        carCostEtcPayManager.del(tabulationEntity, dbEtcPayEntity);
        carCostEtcPayDataTracerService.delEtcPayLog(dbEtcPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
