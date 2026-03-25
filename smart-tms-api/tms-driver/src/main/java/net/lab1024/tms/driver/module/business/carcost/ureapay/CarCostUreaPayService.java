package net.lab1024.tms.driver.module.business.carcost.ureapay;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.CarCostOilCardValidateService;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCategoryEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.basicinfo.CarCostBasicInfoService;
import net.lab1024.tms.driver.module.business.carcost.category.CarCostCategoryDao;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.oilcard.OilCardDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CarCostUreaPayService {

    @Resource
    private DriverDao driverDao;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostCategoryDao carCostCategoryDao;
    @Resource
    private CarCostBasicInfoService carCostBasicInfoService;
    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;
    @Resource
    private CarCostUreaPayManager carCostUreaPayManager;
    @Resource
    private CarCostOilCardValidateService carCostOilCardValidateService;
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
     * 尿素支出详情
     *
     * @param ureaPayId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long ureaPayId) {
        CarCostTabulationDetailVO detailVO = carCostUreaPayDao.selectDetail(ureaPayId);
        return ResponseDTO.ok(detailVO);
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
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = addForm.getCategoryId();
        Long enterpriseId = addForm.getEnterpriseId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.UREA_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        String oilCardNo = StringConst.EMPTY;
        Long oilCardId = addForm.getOilCardId();
        if (CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(categoryEntity.getPayMode())) {
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
        }

        CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
        ureaPayEntity.setEnterpriseId(enterpriseId);
        ureaPayEntity.setWaybillId(waybillId);
        ureaPayEntity.setVehicleId(vehicleId);
        ureaPayEntity.setDriverId(driverId);
        ureaPayEntity.setOilCardId(oilCardId);
        ureaPayEntity.setCategoryId(categoryId);
        ureaPayEntity.setCostType(categoryEntity.getCostType());
        ureaPayEntity.setCategoryName(categoryEntity.getCategoryName());
        ureaPayEntity.setPayMode(categoryEntity.getPayMode());
        ureaPayEntity.setAmount(addForm.getAmount());
        ureaPayEntity.setAttachment(addForm.getAttachment());
        ureaPayEntity.setRemark(addForm.getRemark());
        ureaPayEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        ureaPayEntity.setCreateUserId(driverId);
        ureaPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        ureaPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        ureaPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.UREA_PAY.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostUreaPayManager.handleAdd(ureaPayEntity, tabulationEntity);
        CarCostUreaPayVO ureaPayVO = SmartBeanUtil.copy(ureaPayEntity, CarCostUreaPayVO.class);
        ureaPayVO.setOilCardNo(oilCardNo);
        carCostUreaPayDataTracerService.saveUreaPayLog(vehicleId, ureaPayVO, dataTracerRequestForm);
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
        // 校验支出记录
        Long ureaPayId = updateForm.getUreaPayId();
        CarCostUreaPayEntity beforePayEntity = carCostUreaPayDao.selectById(ureaPayId);
        if (beforePayEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录不存在");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "尿素支出记录已审核");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long categoryId = updateForm.getCategoryId();
        // 校验参数
        CarCostCategoryCostTypeEnum costTypeEnum = CarCostCategoryCostTypeEnum.UREA_COST;
        ResponseDTO<CarCostCategoryEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, categoryId, costTypeEnum);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        CarCostCategoryEntity categoryEntity = resp.getData();

        String oilCardNo = StringConst.EMPTY;
        Long oilCardId = updateForm.getOilCardId();
        if (CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(categoryEntity.getPayMode())) {
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
        }

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
        ureaPayEntity.setAmount(updateForm.getAmount());
        ureaPayEntity.setAttachment(updateForm.getAttachment());
        ureaPayEntity.setRemark(updateForm.getRemark());

        Integer moduleType = CarCostModuleTypeEnum.UREA_PAY.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(ureaPayId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.UREA_PAY.getValue());
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setCategoryId(categoryId);
        tabulationEntity.setOilCardId(oilCardId);

        carCostUreaPayManager.handleUpdate(ureaPayEntity, tabulationEntity);
        saveUpdateLog(dataTracerRequestForm, ureaPayId, beforePayEntity, vehicleId, oilCardNo);
        return ResponseDTO.ok();
    }

    private void saveUpdateLog(DataTracerRequestForm dataTracerRequestForm,
                               Long ureaPayId,
                               CarCostUreaPayEntity beforePayEntity,
                               Long vehicleId,
                               String afterOilCardNo) {
        CarCostUreaPayVO beforeUreaPayVO = SmartBeanUtil.copy(beforePayEntity, CarCostUreaPayVO.class);
        OilCardEntity beforeOilCardEntity = oilCardDao.selectById(beforeUreaPayVO.getOilCardId());
        beforeUreaPayVO.setOilCardNo(beforeOilCardEntity == null ? StringConst.EMPTY : beforeOilCardEntity.getOilCardNo());
        CarCostUreaPayEntity afterPayEntity = carCostUreaPayDao.selectById(ureaPayId);
        CarCostUreaPayVO afterUreaPayVO = SmartBeanUtil.copy(afterPayEntity, CarCostUreaPayVO.class);
        afterUreaPayVO.setOilCardNo(afterOilCardNo);
        carCostUreaPayDataTracerService.updateUreaPayLog(vehicleId, beforeUreaPayVO, afterUreaPayVO, dataTracerRequestForm);
    }

}
