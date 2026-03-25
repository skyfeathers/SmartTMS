package net.lab1024.tms.admin.module.business.carcost.cashreceive;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveAuditForm;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveVO;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveUpdateForm;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostCashReceiveService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostCashReceiveManager carCostCashReceiveManager;
    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;
    @Resource
    private CarCostCashReceiveDataTracerService carCostCashReceiveDataTracerService;

    /**
     * 根据运单ID获取运单下的现金收入
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostCashReceiveVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在");
        }
        List<CarCostCashReceiveVO> cashReceiveVOList = carCostCashReceiveDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(cashReceiveVOList);
    }

    /**
     * 新建自有车现金收入
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostCashReceiveAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
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

        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setEnterpriseId(addForm.getEnterpriseId());
        cashReceiveEntity.setWaybillId(waybillId);
        cashReceiveEntity.setDriverId(driverId);
        cashReceiveEntity.setVehicleId(vehicleId);
        cashReceiveEntity.setAmount(addForm.getAmount());
        cashReceiveEntity.setAttachment(addForm.getAttachment());
        cashReceiveEntity.setRemark(addForm.getRemark());
        cashReceiveEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        cashReceiveEntity.setAuditRemark("系统自动审核通过");
        cashReceiveEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        cashReceiveEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        cashReceiveEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        cashReceiveEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_RECEIVE.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setAuditRemark("系统自动审核通过");
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostCashReceiveManager.handleAdd(cashReceiveEntity, tabulationEntity);
        carCostCashReceiveDataTracerService.saveCashReceiveLog(cashReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑自有车现金收入
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostCashReceiveUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long cashReceiveId = updateForm.getCashReceiveId();
        CarCostCashReceiveEntity beforeReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金收入记录不存在");
        }

        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeReceiveEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        Integer auditStatus = beforeReceiveEntity.getAuditStatus();
        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setCashReceiveId(cashReceiveId);
        cashReceiveEntity.setWaybillId(waybillId);
        cashReceiveEntity.setDriverId(driverId);
        cashReceiveEntity.setVehicleId(vehicleId);
        cashReceiveEntity.setAmount(updateForm.getAmount());
        cashReceiveEntity.setAttachment(updateForm.getAttachment());
        cashReceiveEntity.setRemark(updateForm.getRemark());
        cashReceiveEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.CASH_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(moduleType);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setAuditStatus(auditStatus);

        carCostCashReceiveManager.handleUpdate(beforeReceiveEntity, cashReceiveEntity, tabulationEntity);
        CarCostCashReceiveEntity afterReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        carCostCashReceiveDataTracerService.updateCashReceiveLog(beforeReceiveEntity, afterReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 司机现金收入审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostCashReceiveAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        if (AuditStatusEnum.WAIT_AUDIT.equalsValue(auditStatus)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        Long cashReceiveId = auditForm.getCashReceiveId();
        CarCostCashReceiveEntity beforeReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金收入记录不存在");
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        if (auditStatus.equals(beforeReceiveEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用已处于"+auditStatusEnum.getDesc()+"状态");
        }


        Long vehicleId = beforeReceiveEntity.getVehicleId();
        Long driverId = beforeReceiveEntity.getDriverId();
        Long waybillId = beforeReceiveEntity.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setCashReceiveId(cashReceiveId);
        cashReceiveEntity.setAuditStatus(auditStatus);
        cashReceiveEntity.setAuditRemark(auditForm.getAuditRemark());
        // 设置运单ID用于更新运单自有车费用审核状态
        cashReceiveEntity.setWaybillId(waybillId);

        Integer moduleType = CarCostModuleTypeEnum.CASH_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        carCostCashReceiveManager.handleAudit(beforeReceiveEntity, cashReceiveEntity, tabulationEntity);
        CarCostCashReceiveEntity afterReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        carCostCashReceiveDataTracerService.auditCashReceiveLog(afterReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param cashReceiveId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long cashReceiveId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashReceiveEntity dbCashReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (dbCashReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "现金收入记录不存在");
        }
        if (dbCashReceiveEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }
        // 参数校验
        Long vehicleId = dbCashReceiveEntity.getVehicleId();
        Long driverId = dbCashReceiveEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }


        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setCashReceiveId(cashReceiveId);
        cashReceiveEntity.setWaybillId(waybillId);
        cashReceiveEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.CASH_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostCashReceiveManager.handleRelateWaybill(cashReceiveEntity, tabulationEntity, dbCashReceiveEntity);
        carCostCashReceiveDataTracerService.relateWaybillLog(vehicleId, cashReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param cashReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long cashReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashReceiveEntity dbCashReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (ObjectUtil.isEmpty(dbCashReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金收入记录不存在");
        }
        if (dbCashReceiveEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.CASH_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashReceiveId, moduleType);

        carCostCashReceiveManager.handleCancelRelateWaybill(cashReceiveId, tabulationId, dbCashReceiveEntity);
        carCostCashReceiveDataTracerService.cancelRelateWaybillLog(dbCashReceiveEntity.getVehicleId(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param cashReceiveId
     * @param cashReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(Long cashReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashReceiveEntity dbCashReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (ObjectUtil.isEmpty(dbCashReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金收入记录不存在");
        }
        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(cashReceiveId, CarCostModuleTypeEnum.CASH_RECEIVE.getValue());
        carCostCashReceiveManager.handleDel(tabulationEntity, dbCashReceiveEntity);
        carCostCashReceiveDataTracerService.delCashReceiveLog(dbCashReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param cashReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long cashReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostCashReceiveEntity dbCashReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (ObjectUtil.isEmpty(dbCashReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "现金收入记录不存在");
        }

        carCostCashReceiveManager.handleDel(tabulationEntity, dbCashReceiveEntity);
        carCostCashReceiveDataTracerService.delCashReceiveLog(dbCashReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

}
