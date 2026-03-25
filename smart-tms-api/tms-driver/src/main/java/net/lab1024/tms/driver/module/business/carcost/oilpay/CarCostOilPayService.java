package net.lab1024.tms.driver.module.business.carcost.oilpay;

import cn.hutool.core.util.ObjectUtil;
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
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.oilcard.OilCardDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
     * 有卡支付详情
     *
     * @param oilPayId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long oilPayId) {
        CarCostTabulationDetailVO detailVO = carCostOilPayDao.selectDetail(oilPayId);
        return ResponseDTO.ok(detailVO);
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
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = addForm.getCategoryId();
        Long enterpriseId = addForm.getEnterpriseId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.OIL_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        Long oilCardId = addForm.getOilCardId();
        String oilCardNo = StringConst.EMPTY;
        Integer payMode = categoryEntity.getPayMode();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                if (addForm.getFuelType() == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "选择现金支付时需要选择燃料类型");
                }
                break;
            case OIL_CARD:
                // 校验油卡
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }
                OilCardEntity oilCardEntity = oilCardResp.getData();

                BigDecimal balance = oilCardEntity.getBalance();
                if (balance.compareTo(addForm.getAmount()) < 0) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡余额不足");
                }

                oilCardNo = oilCardEntity.getOilCardNo();
                addForm.setFuelType(oilCardEntity.getFuelType());
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

        CarCostOilPayEntity oilPayEntity = new CarCostOilPayEntity();
        oilPayEntity.setEnterpriseId(enterpriseId);
        oilPayEntity.setWaybillId(waybillId);
        oilPayEntity.setDriverId(driverId);
        oilPayEntity.setVehicleId(vehicleId);
        oilPayEntity.setOilCardId(oilCardId);
        oilPayEntity.setFuelType(addForm.getFuelType());
        oilPayEntity.setCategoryId(categoryId);
        oilPayEntity.setCostType(categoryEntity.getCostType());
        oilPayEntity.setCategoryName(categoryEntity.getCategoryName());
        oilPayEntity.setPayMode(payMode);
        oilPayEntity.setAmount(addForm.getAmount());
        oilPayEntity.setOilConsumption(addForm.getOilConsumption());
        oilPayEntity.setAttachment(addForm.getAttachment());
        oilPayEntity.setRemark(addForm.getRemark());
        oilPayEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        oilPayEntity.setCreateUserId(driverId);
        oilPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        oilPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        oilPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.OIL_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostOilPayManager.handleAdd(oilPayEntity, tabulationEntity);
        // 保存日志
        CarCostOilPayVO oilPayVO = SmartBeanUtil.copy(oilPayEntity, CarCostOilPayVO.class);
        oilPayVO.setOilCardNo(oilCardNo);
        carCostOilPayDataTracerService.saveOilPayLog(vehicleId, oilPayVO, dataTracerRequestForm);
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
        // 校验支出记录
        Long oilPayId = updateForm.getOilPayId();
        CarCostOilPayEntity beforePayEntity = carCostOilPayDao.selectById(oilPayId);
        if (beforePayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录已审核");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "油费支出记录不存在");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = updateForm.getCategoryId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.OIL_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        Long oilCardId = updateForm.getOilCardId();
        String oilCardNo = StringConst.EMPTY;
        Integer payMode = categoryEntity.getPayMode();
        CarCostCategoryPayModeEnum payModeEnum = SmartBaseEnumUtil.getEnumByValue(payMode, CarCostCategoryPayModeEnum.class);
        switch (payModeEnum) {
            case CASH:
                if (updateForm.getFuelType() == null) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "选择现金支付时需要选择燃料类型");
                }
                break;
            case OIL_CARD:
                // 校验油卡
                ResponseDTO<OilCardEntity> oilCardResp = carCostCheckService.checkOilCard(waybillId, vehicleId, oilCardId);
                if (!oilCardResp.getOk()) {
                    return ResponseDTO.userErrorParam(oilCardResp.getMsg());
                }
                OilCardEntity oilCardEntity = oilCardResp.getData();
                BigDecimal balance = oilCardEntity.getBalance();
                if (balance.compareTo(updateForm.getAmount()) < 0) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡余额不足");
                }

                oilCardNo = oilCardEntity.getOilCardNo();
                updateForm.setFuelType(oilCardEntity.getFuelType());
                break;
            default:
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "支付方式暂不支持");
        }

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
        oilPayEntity.setPayMode(payMode);
        oilPayEntity.setAmount(updateForm.getAmount());
        oilPayEntity.setOilConsumption(updateForm.getOilConsumption());
        oilPayEntity.setAttachment(updateForm.getAttachment());
        oilPayEntity.setRemark(updateForm.getRemark());

        Integer moduleType = CarCostModuleTypeEnum.OIL_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);

        carCostOilPayManager.handleUpdate(oilPayEntity, tabulationEntity);
        saveUpdateLog(dataTracerRequestForm, oilPayId, beforePayEntity, vehicleId, oilCardNo);
        return ResponseDTO.ok();
    }

    private void saveUpdateLog(DataTracerRequestForm dataTracerRequestForm,
                               Long oilPayId,
                               CarCostOilPayEntity beforePayEntity,
                               Long vehicleId,
                               String afterOilCardNo) {
        CarCostOilPayVO beforeOilPayVO = SmartBeanUtil.copy(beforePayEntity, CarCostOilPayVO.class);
        OilCardEntity beforeOilCardEntity = oilCardDao.selectById(beforeOilPayVO.getOilCardId());
        beforeOilPayVO.setOilCardNo(beforeOilCardEntity != null ? beforeOilCardEntity.getOilCardNo() : StringConst.EMPTY);
        CarCostOilPayEntity afterCarCostOilPayEntity = carCostOilPayDao.selectById(oilPayId);
        CarCostOilPayVO afterOilPayVO = SmartBeanUtil.copy(afterCarCostOilPayEntity, CarCostOilPayVO.class);
        afterOilPayVO.setOilCardNo(afterOilCardNo);
        carCostOilPayDataTracerService.updateOilPayLog(vehicleId, beforeOilPayVO, afterOilPayVO, dataTracerRequestForm);
    }

}
