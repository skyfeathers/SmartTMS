package net.lab1024.tms.driver.module.business.carcost.cashpay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.cashpay.domain.CarCostCashPayVO;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
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
     * 现金支出详情
     *
     * @param cashPayId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long cashPayId) {
        CarCostTabulationDetailVO detailVO = carCostCashPayDao.selectDetail(cashPayId);
        return ResponseDTO.ok(detailVO);
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
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = addForm.getCategoryId();
        Long enterpriseId = addForm.getEnterpriseId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.CASH_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setVehicleId(vehicleId);
        cashPayEntity.setDriverId(driverId);
        cashPayEntity.setCategoryId(categoryId);
        cashPayEntity.setEnterpriseId(enterpriseId);
        cashPayEntity.setCostType(categoryEntity.getCostType());
        cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
        cashPayEntity.setPayMode(categoryEntity.getPayMode());
        cashPayEntity.setAmount(addForm.getAmount());
        cashPayEntity.setAttachment(addForm.getAttachment());
        cashPayEntity.setRemark(addForm.getRemark());
        cashPayEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        cashPayEntity.setCreateUserId(driverId);
        cashPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        cashPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        cashPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostCashPayManager.handleAdd(cashPayEntity, tabulationEntity);
        carCostCashPayDataTracerService.saveCashPayLog(vehicleId, cashPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 修改现金收入
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostCashPayUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        // 校验现金支出记录
        Long cashPayId = updateForm.getCashPayId();
        CarCostCashPayEntity beforePayEntity = carCostCashPayDao.selectById(cashPayId);
        if (beforePayEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "现金支出记录不存在");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "现金支出记录已审核");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = updateForm.getCategoryId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.CASH_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
        cashPayEntity.setWaybillId(waybillId);
        cashPayEntity.setVehicleId(vehicleId);
        cashPayEntity.setDriverId(driverId);
        cashPayEntity.setCategoryId(categoryId);
        cashPayEntity.setCostType(categoryEntity.getCostType());
        cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
        cashPayEntity.setPayMode(categoryEntity.getPayMode());
        cashPayEntity.setAmount(updateForm.getAmount());
        cashPayEntity.setAttachment(updateForm.getAttachment());
        cashPayEntity.setRemark(updateForm.getRemark());

        Integer moduleType = CarCostModuleTypeEnum.CASH_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);

        carCostCashPayManager.handleUpdate(cashPayEntity, tabulationEntity);
        CarCostCashPayEntity afterPayEntity = carCostCashPayDao.selectById(cashPayId);
        carCostCashPayDataTracerService.updateCashPayLog(vehicleId, beforePayEntity, afterPayEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

}
