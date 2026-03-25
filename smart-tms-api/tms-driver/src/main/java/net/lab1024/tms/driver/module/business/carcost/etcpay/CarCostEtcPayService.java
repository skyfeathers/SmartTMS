package net.lab1024.tms.driver.module.business.carcost.etcpay;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.etcpay.domain.CarCostEtcPayVO;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostEtcPayService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCheckService carCostCheckService;
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
     * ETC 支出详情
     *
     * @param etcPayId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long etcPayId) {
        CarCostTabulationDetailVO detailVO = carCostEtcPayDao.selectDetail(etcPayId);
        return ResponseDTO.ok(detailVO);
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
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = addForm.getCategoryId();
        Long enterpriseId = addForm.getEnterpriseId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.ETC_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        if (StrUtil.isBlank(addForm.getAttachment())
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(categoryEntity.getPayMode())) {
            return ResponseDTO.userErrorParam("费用附件不能为空");
        }
        Integer payMode = categoryEntity.getPayMode();
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(payMode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostEtcPayEntity etcPayEntity = new CarCostEtcPayEntity();
        etcPayEntity.setEnterpriseId(enterpriseId);
        etcPayEntity.setWaybillId(waybillId);
        etcPayEntity.setVehicleId(vehicleId);
        etcPayEntity.setDriverId(driverId);
        etcPayEntity.setCategoryId(categoryId);
        etcPayEntity.setCostType(categoryEntity.getCostType());
        etcPayEntity.setCategoryName(categoryEntity.getCategoryName());
        etcPayEntity.setPayMode(categoryEntity.getPayMode());
        etcPayEntity.setAmount(addForm.getAmount());
        etcPayEntity.setAttachment(addForm.getAttachment());
        etcPayEntity.setRemark(addForm.getRemark());
        etcPayEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        etcPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        etcPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        etcPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        etcPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.ETC_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostEtcPayManager.handleAdd(etcPayEntity, tabulationEntity);
        carCostEtcPayDataTracerService.saveEtcPayLog(vehicleId, etcPayEntity, dataTracerRequestForm);
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
        // 校验ETC支出记录
        Long etcPayId = updateForm.getEtcPayId();
        CarCostEtcPayEntity beforePayEntity = carCostEtcPayDao.selectById(etcPayId);
        if (beforePayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录不存在");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "ETC支出记录已审核");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = updateForm.getCategoryId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.ETC_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        if (StringUtils.isBlank(updateForm.getAttachment())
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(categoryEntity.getPayMode())) {
            return ResponseDTO.userErrorParam("费用附件不能为空");
        }
        Integer payMode = categoryEntity.getPayMode();
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)
                && !CarCostCategoryPayModeEnum.ETC_CARD.equalsValue(payMode)) {
            return ResponseDTO.userErrorParam("支付方式暂不支持");
        }

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

        Integer moduleType = CarCostModuleTypeEnum.ETC_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(etcPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setTabulationId(etcPayId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);

        carCostEtcPayManager.handleUpdate(etcPayEntity, tabulationEntity);
        CarCostEtcPayEntity afterPayEntity = carCostEtcPayDao.selectById(etcPayId);
        carCostEtcPayDataTracerService.updateEtcPayLog(vehicleId, beforePayEntity, afterPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

}
