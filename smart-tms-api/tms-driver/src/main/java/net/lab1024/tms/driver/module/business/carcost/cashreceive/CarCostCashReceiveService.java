package net.lab1024.tms.driver.module.business.carcost.cashreceive;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.cashreceive.domain.CarCostCashReceiveVO;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostCashReceiveService {

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;
    @Resource
    private CarCostCashReceiveManager carCostCashReceiveManager;
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
     * 现金收入详情
     *
     * @param cashReceiveId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long cashReceiveId) {
        CarCostTabulationDetailVO detailVO = carCostCashReceiveDao.selectDetail(cashReceiveId);
        return ResponseDTO.ok(detailVO);
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
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long enterpriseId = addForm.getEnterpriseId();

        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setEnterpriseId(enterpriseId);
        cashReceiveEntity.setWaybillId(waybillId);
        cashReceiveEntity.setDriverId(driverId);
        cashReceiveEntity.setVehicleId(vehicleId);
        cashReceiveEntity.setAmount(addForm.getAmount());
        cashReceiveEntity.setAttachment(addForm.getAttachment());
        cashReceiveEntity.setRemark(addForm.getRemark());
        cashReceiveEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        cashReceiveEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        cashReceiveEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        cashReceiveEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        cashReceiveEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_RECEIVE.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostCashReceiveManager.handleAdd(cashReceiveEntity, tabulationEntity);
        carCostCashReceiveDataTracerService.saveCashReceiveLog(vehicleId, cashReceiveEntity, dataTracerRequestForm);
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
        // 收入记录检验
        Long cashReceiveId = updateForm.getCashReceiveId();
        CarCostCashReceiveEntity beforeReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.userErrorParam("现金收入记录不存在");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeReceiveEntity.getAuditStatus())) {
            return ResponseDTO.userErrorParam("现金收入记录已审核");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        // 校验参数
        ResponseDTO<String> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId);
        if (!resp.getOk()) {
            return resp;
        }

        CarCostCashReceiveEntity cashReceiveEntity = new CarCostCashReceiveEntity();
        cashReceiveEntity.setCashReceiveId(cashReceiveId);
        cashReceiveEntity.setWaybillId(waybillId);
        cashReceiveEntity.setVehicleId(vehicleId);
        cashReceiveEntity.setAmount(updateForm.getAmount());
        cashReceiveEntity.setAttachment(updateForm.getAttachment());
        cashReceiveEntity.setRemark(updateForm.getRemark());

        Integer moduleType = CarCostModuleTypeEnum.CASH_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(cashReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setVehicleId(vehicleId);

        carCostCashReceiveManager.handleUpdate(cashReceiveEntity, tabulationEntity);
        CarCostCashReceiveEntity afterReceiveEntity = carCostCashReceiveDao.selectById(cashReceiveId);
        carCostCashReceiveDataTracerService.updateCashReceiveLog(vehicleId, beforeReceiveEntity, afterReceiveEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

}
