package net.lab1024.tms.driver.module.business.carcost.common;

import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.CarCostOilCardValidateService;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.driver.module.business.carcost.basicinfo.CarCostBasicInfoService;
import net.lab1024.tms.driver.module.business.carcost.category.CarCostCategoryDao;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.oilcard.OilCardDao;
import net.lab1024.tms.driver.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarCostCheckService {

    @Resource
    private VehicleDao vehicleDao;
    @Resource
    private DriverDao driverDao;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private CarCostCategoryDao carCostCategoryDao;
    @Resource
    private CarCostBasicInfoService carCostBasicInfoService;
    @Resource
    private CarCostOilCardValidateService carCostOilCardValidateService;

    /**
     * 校验车辆
     *
     * @param vehicleId
     * @return
     */
    public ResponseDTO<String> checkVehicle(Long vehicleId) {
        if (vehicleId == null) {
            return ResponseDTO.userErrorParam("车辆ID不能为空");
        }
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        if (vehicleEntity == null || vehicleEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("您选择的车辆不存在");
        }
        return ResponseDTO.ok();
    }

    /**
     * 校验运单
     *
     * @param waybillId
     * @param vehicleId
     * @param driverId
     * @return
     */
    public ResponseDTO<String> checkWaybill(Long waybillId, Long vehicleId, Long driverId) {
        if (waybillId == null) {
            return ResponseDTO.ok();
        }
        Boolean confirmFlag = carCostBasicInfoService.selectConfirmFlagByWaybillId(waybillId);
        if (confirmFlag) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的运单信息已确认");
        }
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的运单不存在");
        }
        if (WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的运单已作废");
        }
        if (!driverId.equals(waybillEntity.getDriverId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机信息与运单信息不匹配");
        }
        if (!vehicleId.equals(waybillEntity.getVehicleId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆信息与运单信息不匹配");
        }

        return ResponseDTO.ok();
    }

    /**
     * 油卡校验
     *
     * @param waybillId
     * @param vehicleId
     * @param slaveOilCardId
     * @return
     */
    public ResponseDTO<OilCardEntity> checkOilCard(Long waybillId, Long vehicleId, Long slaveOilCardId) {
        if (slaveOilCardId == null) {
            return ResponseDTO.userErrorParam("油卡校验信息缺失");
        }

        // 校验副卡
        OilCardEntity slaveOilCardEntity = oilCardDao.selectById(slaveOilCardId);
        if (slaveOilCardEntity == null || slaveOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的油卡不存在");
        }
        if (slaveOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的油卡已禁用");
        }
        if (!slaveOilCardEntity.getUseVehicleId().equals(vehicleId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的油卡与车辆信息不匹配");
        }

        // 校验主卡
        Long masterOilCardId = slaveOilCardEntity.getMasterOilCardId();
        OilCardEntity masterOilCardEntity = oilCardDao.selectById(masterOilCardId);
        if (masterOilCardEntity == null || masterOilCardEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的油卡所属主卡不存在");
        }
        if (masterOilCardEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您选择的油卡所属主卡已禁用");
        }

        // 校验是否使用多张同类型油卡
        if (waybillId != null) {
            Integer fuelType = slaveOilCardEntity.getFuelType();
            ResponseDTO<String> validateResp = carCostOilCardValidateService.validateOilCardId(waybillId, fuelType, slaveOilCardId);
            if (!validateResp.getOk()) {
                return ResponseDTO.userErrorParam(validateResp.getMsg());
            }
        }

        return ResponseDTO.ok(slaveOilCardEntity);
    }

    /**
     * 校验费用分类
     *
     * @param categoryId
     * @return
     */
    public ResponseDTO<CarCostCategoryEntity> checkCategory(Long categoryId, CarCostCategoryCostTypeEnum costTypeEnum) {
        CarCostCategoryEntity categoryEntity = carCostCategoryDao.selectById(categoryId);
        if (categoryEntity == null || categoryEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "费用分类不存在");
        }
        Integer costType = categoryEntity.getCostType();
        if (!costTypeEnum.equalsValue(costType)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "费用分类类型错误");
        }

        return ResponseDTO.ok(categoryEntity);
    }

    /**
     * 校验司机，车辆，运单
     *
     * @param waybillId
     * @param vehicleId
     * @param driverId
     * @return
     */
    public ResponseDTO<String> checkParam(Long waybillId, Long vehicleId, Long driverId) {
        ResponseDTO<String> vehicleResp = this.checkVehicle(vehicleId);
        if (!vehicleResp.getOk()) {
            return vehicleResp;
        }
        ResponseDTO<String> driverResp = this.checkDriver(driverId, vehicleId);
        if (!driverResp.getOk()) {
            return driverResp;
        }
        ResponseDTO<String> waybillResp = this.checkWaybill(waybillId, vehicleId, driverId);
        if (!waybillResp.getOk()) {
            return waybillResp;
        }

        return ResponseDTO.ok();
    }

    /**
     * 校验司机
     *
     * @param driverId
     * @param vehicleId
     * @return
     */
    private ResponseDTO<String> checkDriver(Long driverId, Long vehicleId) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (driverEntity == null
                || driverEntity.getDeletedFlag()
                || DriverStatusEnum.DISABLED.equalsValue(driverEntity.getStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机不存在或司机状态错误");
        }

        return ResponseDTO.ok();
    }

    /**
     * 校验司机，车辆，运单，油卡
     *
     * @param waybillId
     * @param vehicleId
     * @param driverId
     * @param oilCardId
     * @return
     */
    public ResponseDTO<OilCardEntity> checkParam(Long waybillId, Long vehicleId, Long driverId, Long oilCardId) {
        ResponseDTO<String> paramResp = this.checkParam(waybillId, vehicleId, driverId);
        if (!paramResp.getOk()) {
            return ResponseDTO.userErrorParam(paramResp.getMsg());
        }
        ResponseDTO<OilCardEntity> oilCardResp = this.checkOilCard(waybillId, vehicleId, oilCardId);
        if (!oilCardResp.getOk()) {
            return ResponseDTO.userErrorParam(oilCardResp.getMsg());
        }

        return oilCardResp;
    }

    /**
     * 校验司机，车辆，运单，分类
     *
     * @param waybillId
     * @param vehicleId
     * @param driverId
     * @param categoryId
     * @param costTypeEnum
     * @return
     */
    public ResponseDTO<CarCostCategoryEntity> checkParam(Long waybillId,
                                                         Long vehicleId,
                                                         Long driverId,
                                                         Long categoryId,
                                                         CarCostCategoryCostTypeEnum costTypeEnum) {
        ResponseDTO<String> paramResp = this.checkParam(waybillId, vehicleId, driverId);
        if (!paramResp.getOk()) {
            return ResponseDTO.userErrorParam(paramResp.getMsg());
        }
        ResponseDTO<CarCostCategoryEntity> categoryResp = this.checkCategory(categoryId, costTypeEnum);
        if (!categoryResp.getOk()) {
            return ResponseDTO.userErrorParam(categoryResp.getMsg());
        }

        return categoryResp;
    }
}
