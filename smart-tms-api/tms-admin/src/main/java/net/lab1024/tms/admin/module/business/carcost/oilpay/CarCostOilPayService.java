package net.lab1024.tms.admin.module.business.carcost.oilpay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilPayUpdateForm;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CarCostOilPayService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private CarCostOilPayDao carCostOilPayDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostOilPayManager carCostOilPayManager;
    @Resource
    private CarCostOilPayDataTracerService carCostOilPayDataTracerService;

    /**
     * 根据运单ID查询油费记录
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostOilPayVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在");
        }
        List<CarCostOilPayVO> oilPayVOList = carCostOilPayDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(oilPayVOList);
    }

    /**
     * 新建油费记录
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostOilPayAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
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

        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.OIL_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        // 根据支付方式填入司机ID或油卡ID
        Long moduleId;
        String oilCardNo = StringConst.EMPTY;
        BigDecimal amount = addForm.getAmount();
        Long oilCardId = addForm.getOilCardId();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(categoryEntity.getPayMode(), CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                if (addForm.getFuelType() == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "现金支付时需要选择燃料类型");
                }
                moduleId = driverId;
                break;
            case OIL_CARD:
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }

                OilCardEntity oilCardEntity = oilCardResp.getData();
                moduleId = oilCardEntity.getOilCardId();
                BigDecimal balance = oilCardEntity.getBalance();
                if (balance.compareTo(amount) < 0) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡余额不足");
                }
                oilCardNo = oilCardEntity.getOilCardNo();
                addForm.setFuelType(oilCardEntity.getFuelType());
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostOilPayEntity oilPayEntity = new CarCostOilPayEntity();
        oilPayEntity.setEnterpriseId(addForm.getEnterpriseId());
        oilPayEntity.setWaybillId(waybillId);
        oilPayEntity.setDriverId(driverId);
        oilPayEntity.setVehicleId(vehicleId);
        oilPayEntity.setOilCardId(oilCardId);
        oilPayEntity.setFuelType(addForm.getFuelType());
        oilPayEntity.setCategoryId(categoryId);
        oilPayEntity.setCostType(categoryEntity.getCostType());
        oilPayEntity.setCategoryName(categoryEntity.getCategoryName());
        oilPayEntity.setPayMode(categoryEntity.getPayMode());
        oilPayEntity.setAmount(amount);
        oilPayEntity.setOilConsumption(addForm.getOilConsumption());
        oilPayEntity.setAttachment(addForm.getAttachment());
        oilPayEntity.setRemark(addForm.getRemark());
        oilPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        oilPayEntity.setAuditRemark("系统自动审核通过");
        oilPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        oilPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        oilPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        oilPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.OIL_PAY.getValue());
        tabulationEntity.setAmount(amount);
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setAuditRemark("系统自动审核通过");
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostOilPayManager.handleAdd(moduleId, payModeEnum.getValue(), oilPayEntity, tabulationEntity, dataTracerRequestForm);

        CarCostOilPayVO oilPayVO = SmartBeanUtil.copy(oilPayEntity, CarCostOilPayVO.class);
        oilPayVO.setOilCardNo(oilCardNo);
        carCostOilPayDataTracerService.saveOilPayLog(oilPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑油费支出
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostOilPayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long oilPayId = updateForm.getOilPayId();
        CarCostOilPayEntity beforeOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        if (beforeOilPayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeOilPayEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }
        Long categoryId = updateForm.getCategoryId();
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.OIL_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        String oilCardNo = StringConst.EMPTY;
        Integer payMode = categoryEntity.getPayMode();
        Long oilCardId = updateForm.getOilCardId();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                if (updateForm.getFuelType() == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "现金支付时需要选择燃料类型");
                }
                break;
            case OIL_CARD:
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }

                OilCardEntity oilCardEntity = oilCardResp.getData();
                oilCardNo = oilCardEntity.getOilCardNo();
                updateForm.setFuelType(oilCardEntity.getFuelType());
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        Integer auditStatus = beforeOilPayEntity.getAuditStatus();
        CarCostOilPayEntity oilPayEntity = new CarCostOilPayEntity();
        oilPayEntity.setOilPayId(oilPayId);
        oilPayEntity.setWaybillId(waybillId);
        oilPayEntity.setDriverId(driverId);
        oilPayEntity.setVehicleId(vehicleId);
        oilPayEntity.setOilCardId(oilCardId);
        oilPayEntity.setFuelType(updateForm.getFuelType());
        oilPayEntity.setCategoryId(categoryId);
        oilPayEntity.setCostType(categoryEntity.getCostType());
        oilPayEntity.setCategoryName(categoryEntity.getCategoryName());
        oilPayEntity.setPayMode(categoryEntity.getPayMode());
        oilPayEntity.setAmount(updateForm.getAmount());
        oilPayEntity.setOilConsumption(updateForm.getOilConsumption());
        oilPayEntity.setAttachment(updateForm.getAttachment());
        oilPayEntity.setRemark(updateForm.getRemark());
        oilPayEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.OIL_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.OIL_PAY.getValue());
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setAuditStatus(auditStatus);

        carCostOilPayManager.handleUpdate(oilPayEntity, beforeOilPayEntity, tabulationEntity, dataTracerRequestForm);

        CarCostOilPayVO beforeOilPayVO = SmartBeanUtil.copy(beforeOilPayEntity, CarCostOilPayVO.class);
        if (CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(beforeOilPayVO.getPayMode())) {
            OilCardEntity beforeOilCardEntity = oilCardDao.selectById(beforeOilPayVO.getOilCardId());
            beforeOilPayVO.setOilCardNo(beforeOilCardEntity == null ? StringConst.EMPTY : beforeOilCardEntity.getOilCardNo());
        } else {
            beforeOilPayVO.setOilCardNo(StringConst.EMPTY);
        }
        CarCostOilPayEntity afterOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        CarCostOilPayVO afterOilPayVO = SmartBeanUtil.copy(afterOilPayEntity, CarCostOilPayVO.class);
        afterOilPayVO.setOilCardNo(oilCardNo);
        carCostOilPayDataTracerService.updateOilPayLog(beforeOilPayVO, afterOilPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 油费支出审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostOilPayAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        if (Objects.equals(auditStatusEnum, AuditStatusEnum.WAIT_AUDIT)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        Long oilPayId = auditForm.getOilPayId();
        CarCostOilPayEntity beforePayEntity = carCostOilPayDao.selectById(oilPayId);
        if (ObjectUtil.isEmpty(beforePayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        if (auditStatus.equals(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用已处于此审核状态");
        }

        Long vehicleId = beforePayEntity.getVehicleId();
        Long driverId = beforePayEntity.getDriverId();
        Long waybillId = beforePayEntity.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        CarCostOilPayEntity oilPayEntity = new CarCostOilPayEntity();
        oilPayEntity.setWaybillId(waybillId);
        oilPayEntity.setOilPayId(oilPayId);
        oilPayEntity.setAuditStatus(auditStatus);
        oilPayEntity.setAuditRemark(auditForm.getAuditRemark());

        Integer moduleType = CarCostModuleTypeEnum.OIL_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        String oilCardNo = StringConst.EMPTY;
        Integer payMode = beforePayEntity.getPayMode();
        BigDecimal amount = beforePayEntity.getAmount();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        boolean passFlag = AuditStatusEnum.AUDIT_PASS.equalsValue(beforePayEntity.getAuditStatus());
        switch (payModeEnum) {
            case CASH:
                carCostOilPayManager.handleAudit(beforePayEntity, oilPayEntity, tabulationEntity, dataTracerRequestForm);
                break;
            case OIL_CARD:
                Long oilCardId = beforePayEntity.getOilCardId();
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }

                OilCardEntity oilCardEntity = oilCardResp.getData();
                BigDecimal oilCardBalance = oilCardEntity.getBalance();
                if (oilCardBalance.compareTo(amount) < 0) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡余额不足");
                }
                oilCardNo = oilCardEntity.getOilCardNo();
                carCostOilPayManager.handleAudit(beforePayEntity, oilPayEntity, tabulationEntity, dataTracerRequestForm);
                break;
            case ETC_CARD:
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostOilPayEntity afterOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        CarCostOilPayVO costOilPayVO = SmartBeanUtil.copy(afterOilPayEntity, CarCostOilPayVO.class);
        costOilPayVO.setOilCardNo(oilCardNo);
        carCostOilPayDataTracerService.auditOilPayLog(costOilPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param oilPayId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long oilPayId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilPayEntity dbOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        if (ObjectUtil.isEmpty(dbOilPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        if (dbOilPayEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }

        Long vehicleId = dbOilPayEntity.getVehicleId();
        Long driverId = dbOilPayEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkWaybill(waybillId, vehicleId, driverId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        CarCostOilPayEntity oilPayEntity = new CarCostOilPayEntity();
        oilPayEntity.setOilPayId(oilPayId);
        oilPayEntity.setWaybillId(waybillId);
        oilPayEntity.setPayMode(dbOilPayEntity.getPayMode());
        oilPayEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.OIL_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostOilPayManager.handleRelateWaybill(oilPayEntity, tabulationEntity, dbOilPayEntity, dataTracerRequestForm);
        carCostOilPayDataTracerService.relateWaybillLog(vehicleId, oilPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param oilPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long oilPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilPayEntity dbOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        if (ObjectUtil.isEmpty(dbOilPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        if (dbOilPayEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.OIL_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilPayId, moduleType);

        carCostOilPayManager.handleCancelRelateWaybill(oilPayId, tabulationId, dbOilPayEntity, dataTracerRequestForm);
        carCostOilPayDataTracerService.cancelRelateWaybillLog(dbOilPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用信息
     *
     * @param oilPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(Long oilPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilPayEntity dbOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        if (ObjectUtil.isEmpty(dbOilPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(oilPayId, CarCostModuleTypeEnum.OIL_PAY.getValue());
        carCostOilPayManager.handleDel(tabulationEntity, dbOilPayEntity, dataTracerRequestForm);
        carCostOilPayDataTracerService.delOilPayLog(dbOilPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用信息
     *
     * @param tabulationEntity
     * @param oilPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long oilPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostOilPayEntity dbOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        if (ObjectUtil.isEmpty(dbOilPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }
        carCostOilPayManager.handleDel(tabulationEntity, dbOilPayEntity, dataTracerRequestForm);
        carCostOilPayDataTracerService.delOilPayLog(dbOilPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
