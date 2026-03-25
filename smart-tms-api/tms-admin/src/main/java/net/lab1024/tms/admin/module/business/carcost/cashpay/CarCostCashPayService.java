package net.lab1024.tms.admin.module.business.carcost.cashpay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.cashpay.domain.CarCostCashPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.cashpay.domain.CarCostCashPayVO;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayUpdateForm;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostCashPayService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashPayDao carCostCashPayDao;
    @Resource
    private CarCostCashPayManager carCostCashPayManager;
    @Resource
    private CarCostCashPayDataTracerService carCostCashPayDataTracerService;

    /**
     * 根据运单ID获取现金支出列表
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostCashPayVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在");
        }

        List<CarCostCashPayVO> cashPayVOList = carCostCashPayDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(cashPayVOList);
    }

    /**
     * 新建现金支出记录
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostCashPayAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
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

        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.CASH_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setEnterpriseId(addForm.getEnterpriseId());
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setDriverId(driverId);
        cashPayEntity.setVehicleId(vehicleId);
        cashPayEntity.setCategoryId(categoryId);
        cashPayEntity.setCostType(categoryEntity.getCostType());
        cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
        cashPayEntity.setPayMode(categoryEntity.getPayMode());
        cashPayEntity.setAmount(addForm.getAmount());
        cashPayEntity.setAttachment(addForm.getAttachment());
        cashPayEntity.setRemark(addForm.getRemark());
        cashPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        cashPayEntity.setAuditRemark("系统自动审核通过");
        cashPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        cashPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        cashPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        cashPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setAuditRemark("系统自动审核通过");
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostCashPayManager.handleAdd(cashPayEntity, tabulationEntity);
        carCostCashPayDataTracerService.saveCashPayLog(cashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑现金支出记录
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostCashPayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long cashPayId = updateForm.getCashPayId();
        CarCostCashPayEntity beforeCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        if (beforeCashPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
        }

        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeCashPayEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }
        Long categoryId = updateForm.getCategoryId();
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.CASH_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        Integer auditStatus = beforeCashPayEntity.getAuditStatus();
        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setCashPayId(cashPayId);
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setDriverId(driverId);
        cashPayEntity.setVehicleId(vehicleId);
        cashPayEntity.setCategoryId(categoryId);
        cashPayEntity.setCostType(categoryEntity.getCostType());
        cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
        cashPayEntity.setPayMode(categoryEntity.getPayMode());
        cashPayEntity.setAmount(updateForm.getAmount());
        cashPayEntity.setAttachment(updateForm.getAttachment());
        cashPayEntity.setRemark(updateForm.getRemark());
        cashPayEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.CASH_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setAuditStatus(auditStatus);

        carCostCashPayManager.handleUpdate(beforeCashPayEntity, cashPayEntity, tabulationEntity);
        CarCostCashPayEntity afterCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        carCostCashPayDataTracerService.updateCashPayLog(beforeCashPayEntity, afterCashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 现金支出审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostCashPayAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        if (AuditStatusEnum.WAIT_AUDIT.equalsValue(auditStatus)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        Long cashPayId = auditForm.getCashPayId();
        CarCostCashPayEntity beforePayEntity = carCostCashPayDao.selectById(cashPayId);
        if (beforePayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
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

        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setCashPayId(cashPayId);
        cashPayEntity.setAuditStatus(auditStatus);
        cashPayEntity.setAuditRemark(auditForm.getAuditRemark());

        Integer moduleType = CarCostModuleTypeEnum.CASH_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        carCostCashPayManager.handleAudit(beforePayEntity, cashPayEntity, tabulationEntity);
        CarCostCashPayEntity afterCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        carCostCashPayDataTracerService.auditCashPayLog(afterCashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param cashPayId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long cashPayId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashPayEntity dbCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        if (ObjectUtil.isEmpty(dbCashPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
        }
        if (dbCashPayEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }

        // 参数校验
        Long vehicleId = dbCashPayEntity.getVehicleId();
        Long driverId = dbCashPayEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setCashPayId(cashPayId);
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.CASH_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostCashPayManager.handleRelateWaybill(cashPayEntity, tabulationEntity, dbCashPayEntity);
        carCostCashPayDataTracerService.relateWaybillLog(dbCashPayEntity.getVehicleId(), cashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param cashPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long cashPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashPayEntity dbCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        if (dbCashPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
        }
        if (dbCashPayEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.CASH_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashPayId, moduleType);

        carCostCashPayManager.handleCancelRelateWaybill(cashPayId, tabulationId, dbCashPayEntity);
        carCostCashPayDataTracerService.cancelRelateWaybillLog(dbCashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> del(Long cashPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashPayEntity dbCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        if (dbCashPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
        }
        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(cashPayId, CarCostModuleTypeEnum.CASH_PAY.getValue());
        carCostCashPayManager.handleDel(tabulationEntity, dbCashPayEntity);
        carCostCashPayDataTracerService.delCashPayLog(dbCashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param cashPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long cashPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashPayEntity dbCashPayEntity = carCostCashPayDao.selectById(cashPayId);
        if (dbCashPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金支出记录不存在");
        }
        carCostCashPayManager.handleDel(tabulationEntity, dbCashPayEntity);
        carCostCashPayDataTracerService.delCashPayLog(dbCashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}