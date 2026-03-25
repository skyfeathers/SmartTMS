package net.lab1024.tms.admin.module.business.carcost.oilcardreceive;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.CarCostBasicInfoService;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveAuditForm;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveVO;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.CarCostOilCardValidateService;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveUpdateForm;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostOilCardReceiveService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;
    @Resource
    private CarCostOilCardReceiveManager carCostOilCardReceiveManager;
    @Resource
    private CarCostBasicInfoService carCostBasicInfoService;
    @Resource
    private CarCostOilCardReceiveDataTracerService carCostOilCardReceiveDataTracerService;
    @Resource
    private CarCostOilCardValidateService carCostOilCardValidateService;

    /**
     * 根据运单ID获取油卡收入列表
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostOilCardReceiveVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在");
        }
        List<CarCostOilCardReceiveVO> oilCardReceiveVOList = carCostOilCardReceiveDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(oilCardReceiveVOList);
    }

    /**
     * 新建油卡收入记录
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostOilCardReceiveAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
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
        Long oilCardId = addForm.getOilCardId();

        ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
        if (!oilCardResp.getOk()) {
            return ResponseDTO.userErrorParam(oilCardResp.getMsg());
        }
        OilCardEntity oilCardEntity = oilCardResp.getData();

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setEnterpriseId(addForm.getEnterpriseId());
        oilCardReceiveEntity.setWaybillId(waybillId);
        oilCardReceiveEntity.setVehicleId(vehicleId);
        oilCardReceiveEntity.setDriverId(driverId);
        oilCardReceiveEntity.setOilCardId(oilCardId);
        oilCardReceiveEntity.setAmount(addForm.getAmount());
        oilCardReceiveEntity.setAttachment(addForm.getAttachment());
        oilCardReceiveEntity.setRemark(addForm.getRemark());
        oilCardReceiveEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        oilCardReceiveEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        oilCardReceiveEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        oilCardReceiveEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        oilCardReceiveEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setDriverId(waybillEntity.getDriverId());
        tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostOilCardReceiveManager.handleAdd(oilCardReceiveEntity, tabulationEntity, dataTracerRequestForm);
        CarCostOilCardReceiveVO oilCardReceiveVO = SmartBeanUtil.copy(oilCardReceiveEntity, CarCostOilCardReceiveVO.class);
        oilCardReceiveVO.setOilCardNo(oilCardEntity.getOilCardNo());
        carCostOilCardReceiveDataTracerService.saveOilCardReceiveLog(oilCardReceiveVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑油卡收入记录
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostOilCardReceiveUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardReceiveId = updateForm.getOilCardReceiveId();
        CarCostOilCardReceiveEntity beforeReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
        }

        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeReceiveEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        // 校验油卡
        Long oilCardId = updateForm.getOilCardId();
        ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
        if (!oilCardResp.getOk()) {
            return ResponseDTO.userErrorParam(oilCardResp.getMsg());
        }
        Integer auditStatus = beforeReceiveEntity.getAuditStatus();
        OilCardEntity oilCardEntity = oilCardResp.getData();

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setOilCardReceiveId(oilCardReceiveId);
        oilCardReceiveEntity.setWaybillId(waybillId);
        oilCardReceiveEntity.setVehicleId(vehicleId);
        oilCardReceiveEntity.setDriverId(driverId);
        oilCardReceiveEntity.setOilCardId(oilCardId);
        oilCardReceiveEntity.setAmount(updateForm.getAmount());
        oilCardReceiveEntity.setAttachment(updateForm.getAttachment());
        oilCardReceiveEntity.setRemark(updateForm.getRemark());
        oilCardReceiveEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilCardReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setOilCardId(oilCardId);
        carCostOilCardReceiveManager.handleUpdate(oilCardReceiveEntity, beforeReceiveEntity, tabulationEntity, dataTracerRequestForm);
        saveUpdateLog(dataTracerRequestForm, oilCardReceiveId, beforeReceiveEntity, oilCardEntity);
        return ResponseDTO.ok();
    }

    private void saveUpdateLog(DataTracerRequestForm dataTracerRequestForm,
                               Long oilCardReceiveId,
                               CarCostOilCardReceiveEntity beforeReceiveEntity,
                               OilCardEntity oilCardEntity) {
        CarCostOilCardReceiveVO beforeOilCardReceiveVO = SmartBeanUtil.copy(beforeReceiveEntity, CarCostOilCardReceiveVO.class);
        OilCardEntity beforeOilCardEntity = oilCardDao.selectById(beforeReceiveEntity.getOilCardId());
        beforeOilCardReceiveVO.setOilCardNo(ObjectUtil.isNotEmpty(beforeOilCardEntity) ? beforeOilCardEntity.getOilCardNo() : StringConst.EMPTY);
        CarCostOilCardReceiveEntity afterOilCardReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        CarCostOilCardReceiveVO afterOilCardReceiveVO = SmartBeanUtil.copy(afterOilCardReceiveEntity, CarCostOilCardReceiveVO.class);
        afterOilCardReceiveVO.setOilCardNo(oilCardEntity.getOilCardNo());
        carCostOilCardReceiveDataTracerService.updateOilCardReceiveLog(beforeOilCardReceiveVO, afterOilCardReceiveVO, dataTracerRequestForm);
    }

    /**
     * 油卡收入审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostOilCardReceiveAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        Long oilCardReceiveId = auditForm.getOilCardReceiveId();
        CarCostOilCardReceiveEntity beforeReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
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
        // 校验油卡
        Long oilCardId = beforeReceiveEntity.getOilCardId();
        ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
        if (!oilCardResp.getOk()) {
            return ResponseDTO.userErrorParam(oilCardResp.getMsg());
        }
        OilCardEntity oilCardEntity = oilCardResp.getData();

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setOilCardReceiveId(oilCardReceiveId);
        oilCardReceiveEntity.setAuditStatus(auditStatus);
        oilCardReceiveEntity.setAuditRemark(auditForm.getAuditRemark());
        oilCardReceiveEntity.setAmount(beforeReceiveEntity.getAmount());
        oilCardReceiveEntity.setWaybillId(waybillId);

        Integer moduleType = CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilCardReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        carCostOilCardReceiveManager.handleAudit(beforeReceiveEntity, oilCardReceiveEntity, tabulationEntity, dataTracerRequestForm);
        CarCostOilCardReceiveEntity afterReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        CarCostOilCardReceiveVO afterReceiveVO = SmartBeanUtil.copy(afterReceiveEntity, CarCostOilCardReceiveVO.class);
        afterReceiveVO.setOilCardNo(oilCardEntity.getOilCardNo());
        carCostOilCardReceiveDataTracerService.auditOilCardReceiveLog(afterReceiveVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param oilCardReceiveId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long oilCardReceiveId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilCardReceiveEntity dbOilCardReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (dbOilCardReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
        }
        if (dbOilCardReceiveEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }

        // 参数校验
        Long vehicleId = dbOilCardReceiveEntity.getVehicleId();
        Long driverId = dbOilCardReceiveEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setOilCardReceiveId(oilCardReceiveId);
        oilCardReceiveEntity.setWaybillId(waybillId);
        oilCardReceiveEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilCardReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostOilCardReceiveManager.handleRelateWaybill(oilCardReceiveEntity, tabulationEntity, dbOilCardReceiveEntity, dataTracerRequestForm);
        carCostOilCardReceiveDataTracerService.relateWaybillLog(vehicleId, oilCardReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param oilCardReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long oilCardReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilCardReceiveEntity dbOilCardReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (ObjectUtil.isEmpty(dbOilCardReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
        }
        if (dbOilCardReceiveEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilCardReceiveId, moduleType);

        carCostOilCardReceiveManager.handleCancelRelateWaybill(oilCardReceiveId, tabulationId, dbOilCardReceiveEntity, dataTracerRequestForm);
        carCostOilCardReceiveDataTracerService.cancelRelateWaybillLog(dbOilCardReceiveEntity.getVehicleId(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param oilCardReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(Long oilCardReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilCardReceiveEntity dbOilCardReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (ObjectUtil.isEmpty(dbOilCardReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
        }

        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(oilCardReceiveId, CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue());
        carCostOilCardReceiveManager.handleDel(tabulationEntity, dbOilCardReceiveEntity, dataTracerRequestForm);
        carCostOilCardReceiveDataTracerService.delOilCardReceiveLog(dbOilCardReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param oilCardReceiveId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long oilCardReceiveId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilCardReceiveEntity dbOilCardReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (ObjectUtil.isEmpty(dbOilCardReceiveEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油卡收入记录不存在");
        }
        carCostOilCardReceiveManager.handleDel(tabulationEntity, dbOilCardReceiveEntity, dataTracerRequestForm);
        carCostOilCardReceiveDataTracerService.delOilCardReceiveLog(dbOilCardReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
