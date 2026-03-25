package net.lab1024.tms.admin.module.business.carcost.ureapay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
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
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayUpdateForm;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CarCostUreaPayService {

    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;
    @Resource
    private CarCostUreaPayManager carCostUreaPayManager;
    @Resource
    private CarCostUreaPayDataTracerService carCostUreaPayDataTracerService;

    /**
     * 根据运单ID获取尿素支出列表
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostUreaPayVO>> list(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (ObjectUtil.isEmpty(waybillEntity)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在");
        }
        List<CarCostUreaPayVO> ureaPayVOList = carCostUreaPayDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(ureaPayVOList);
    }

    /**
     * 新建尿素支出
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostUreaPayAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
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

        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.UREA_COST;
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
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
        ureaPayEntity.setEnterpriseId(addForm.getEnterpriseId());
        ureaPayEntity.setWaybillId(waybillId);
        ureaPayEntity.setVehicleId(vehicleId);
        ureaPayEntity.setDriverId(driverId);
        ureaPayEntity.setOilCardId(oilCardId);
        ureaPayEntity.setCategoryId(categoryId);
        ureaPayEntity.setCostType(categoryEntity.getCostType());
        ureaPayEntity.setCategoryName(categoryEntity.getCategoryName());
        ureaPayEntity.setPayMode(categoryEntity.getPayMode());
        ureaPayEntity.setAmount(amount);
        ureaPayEntity.setAttachment(addForm.getAttachment());
        ureaPayEntity.setRemark(addForm.getRemark());
        ureaPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        ureaPayEntity.setAuditRemark("系统自动审核通过");
        ureaPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        ureaPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        ureaPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        ureaPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(addForm.getEnterpriseId());
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.UREA_PAY.getValue());
        tabulationEntity.setAmount(amount);
        tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
        tabulationEntity.setAuditRemark("系统自动审核通过");
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostUreaPayManager.handleAdd(moduleId, ureaPayEntity, tabulationEntity, dataTracerRequestForm);

        CarCostUreaPayVO ureaPayVO = SmartBeanUtil.copy(ureaPayEntity, CarCostUreaPayVO.class);
        ureaPayVO.setOilCardNo(oilCardNo);
        carCostUreaPayDataTracerService.saveUreaPayLog(ureaPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 编辑尿素支出
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostUreaPayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long ureaPayId = updateForm.getUreaPayId();
        CarCostUreaPayEntity beforeUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(beforeUreaPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = beforeUreaPayEntity.getDriverId();
        Long waybillId = updateForm.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        Long categoryId = updateForm.getCategoryId();
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.UREA_COST;
        ResponseDTO<CarCostCategoryEntity> categoryResp = carCostCheckService.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = categoryResp.getData();

        String oilCardNo = StringConst.EMPTY;
        Integer payMode = categoryEntity.getPayMode();
        BigDecimal amount = updateForm.getAmount();
        Long oilCardId = updateForm.getOilCardId();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                break;
            case OIL_CARD:
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }

                OilCardEntity oilCardEntity = oilCardResp.getData();
                oilCardNo = oilCardEntity.getOilCardNo();
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        Integer auditStatus = beforeUreaPayEntity.getAuditStatus();
        CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
        ureaPayEntity.setUreaPayId(ureaPayId);
        ureaPayEntity.setWaybillId(waybillId);
        ureaPayEntity.setVehicleId(vehicleId);
        ureaPayEntity.setDriverId(driverId);
        ureaPayEntity.setOilCardId(oilCardId);
        ureaPayEntity.setCategoryId(categoryId);
        ureaPayEntity.setCostType(categoryEntity.getCostType());
        ureaPayEntity.setCategoryName(categoryEntity.getCategoryName());
        ureaPayEntity.setPayMode(categoryEntity.getPayMode());
        ureaPayEntity.setAmount(amount);
        ureaPayEntity.setAttachment(updateForm.getAttachment());
        ureaPayEntity.setRemark(updateForm.getRemark());
        ureaPayEntity.setAuditStatus(auditStatus);

        Integer moduleType = CarCostModuleTypeEnum.UREA_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(ureaPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.UREA_PAY.getValue());
        tabulationEntity.setAmount(amount);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);

        carCostUreaPayManager.handleUpdate(ureaPayEntity, beforeUreaPayEntity, tabulationEntity, dataTracerRequestForm);

        CarCostUreaPayVO beforeUreaPayVO = SmartBeanUtil.copy(beforeUreaPayEntity, CarCostUreaPayVO.class);
        if (CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(beforeUreaPayVO.getPayMode())) {
            OilCardEntity beforeOilCardEntity = oilCardDao.selectById(beforeUreaPayVO.getOilCardId());
            beforeUreaPayVO.setOilCardNo(ObjectUtil.isNotEmpty(beforeOilCardEntity) ? beforeOilCardEntity.getOilCardNo() : StringConst.EMPTY);
        } else {
            beforeUreaPayVO.setOilCardNo(StringConst.EMPTY);
        }
        CarCostUreaPayEntity afterUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        CarCostUreaPayVO afterUreaPayVO = SmartBeanUtil.copy(afterUreaPayEntity, CarCostUreaPayVO.class);
        afterUreaPayVO.setOilCardNo(oilCardNo);
        carCostUreaPayDataTracerService.updateUreaPayLog(beforeUreaPayVO, afterUreaPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 审核尿素支出
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(CarCostUreaPayAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Integer auditStatus = auditForm.getAuditStatus();
        Long ureaPayId = auditForm.getUreaPayId();
        CarCostUreaPayEntity beforePayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(beforePayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        if (auditStatusEnum.equalsValue(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用已处于" +auditStatusEnum.getDesc()+ "审核状态,请勿重复审核");
        }

        Long vehicleId = beforePayEntity.getVehicleId();
        Long driverId = beforePayEntity.getDriverId();
        Long waybillId = beforePayEntity.getWaybillId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, driverId, vehicleId);
        if (!resp.getOk()) {
            return resp;
        }

        CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
        ureaPayEntity.setWaybillId(waybillId);
        ureaPayEntity.setUreaPayId(ureaPayId);
        ureaPayEntity.setAuditStatus(auditStatus);
        ureaPayEntity.setAuditRemark(auditForm.getAuditRemark());

        Integer moduleType = CarCostModuleTypeEnum.UREA_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(ureaPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setAuditStatus(auditStatus);
        tabulationEntity.setAuditRemark(auditForm.getAuditRemark());

        String oilCardNo = StringConst.EMPTY;
        Integer payMode = beforePayEntity.getPayMode();
        BigDecimal amount = beforePayEntity.getAmount();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                carCostUreaPayManager.handleAudit(beforePayEntity, ureaPayEntity, tabulationEntity, dataTracerRequestForm);
                break;
            case OIL_CARD:
                Long oilCardId = beforePayEntity.getOilCardId();
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }

                OilCardEntity oilCardEntity = oilCardResp.getData();
                if (oilCardEntity.getBalance().compareTo(amount) < 0) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡余额不足");
                }
                oilCardNo = oilCardEntity.getOilCardNo();
                carCostUreaPayManager.handleAudit(beforePayEntity, ureaPayEntity, tabulationEntity, dataTracerRequestForm);
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }
        CarCostUreaPayEntity afterUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        CarCostUreaPayVO ureaPayVO = SmartBeanUtil.copy(afterUreaPayEntity, CarCostUreaPayVO.class);
        ureaPayVO.setOilCardNo(oilCardNo);
        carCostUreaPayDataTracerService.auditUreaPayLog(ureaPayVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 绑定运单
     *
     * @param ureaPayId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> relateWaybill(Long ureaPayId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostUreaPayEntity dbUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(dbUreaPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        if (dbUreaPayEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }

        Long vehicleId = dbUreaPayEntity.getVehicleId();
        Long driverId = dbUreaPayEntity.getDriverId();
        ResponseDTO<String> waybillResp = carCostCheckService.checkWaybill(waybillId, vehicleId, driverId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
        ureaPayEntity.setUreaPayId(ureaPayId);
        ureaPayEntity.setWaybillId(waybillId);
        ureaPayEntity.setPayMode(dbUreaPayEntity.getPayMode());
        ureaPayEntity.setDriverId(driverId);

        Integer moduleType = CarCostModuleTypeEnum.UREA_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(ureaPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);

        carCostUreaPayManager.handleRelateWaybill(ureaPayEntity, tabulationEntity, dbUreaPayEntity, dataTracerRequestForm);
        carCostUreaPayDataTracerService.relateWaybillLog(vehicleId, ureaPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 取消绑定运单
     *
     * @param ureaPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancelRelateWaybill(Long ureaPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostUreaPayEntity beforeUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(beforeUreaPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        Long originWaybillId = beforeUreaPayEntity.getWaybillId();
        if (originWaybillId == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Integer moduleType = CarCostModuleTypeEnum.UREA_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(ureaPayId, moduleType);

        carCostUreaPayManager.handleCancelRelateWaybill(ureaPayId, tabulationId, beforeUreaPayEntity, dataTracerRequestForm);
        carCostUreaPayDataTracerService.cancelRelateWaybillLog(beforeUreaPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param ureaPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(Long ureaPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostUreaPayEntity dbUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(dbUreaPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }

        CarCostTabulationEntity tabulationEntity = carCostTabulationDao.selectByModule(ureaPayId, CarCostModuleTypeEnum.UREA_PAY.getValue());
        carCostUreaPayManager.handleDel(tabulationEntity, dbUreaPayEntity, dataTracerRequestForm);
        carCostUreaPayDataTracerService.delUreaPayLog(dbUreaPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param ureaPayId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> del(CarCostTabulationEntity tabulationEntity, Long ureaPayId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostUreaPayEntity dbUreaPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (ObjectUtil.isEmpty(dbUreaPayEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        carCostUreaPayManager.handleDel(tabulationEntity, dbUreaPayEntity, dataTracerRequestForm);
        carCostUreaPayDataTracerService.delUreaPayLog(dbUreaPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
