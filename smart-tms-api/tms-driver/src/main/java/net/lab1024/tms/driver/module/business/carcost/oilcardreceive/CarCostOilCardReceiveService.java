package net.lab1024.tms.driver.module.business.carcost.oilcardreceive;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.carcost.common.CarCostCheckService;
import net.lab1024.tms.driver.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveVO;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.oilcard.OilCardDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostOilCardReceiveService {

    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCheckService carCostCheckService;
    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;
    @Resource
    private CarCostOilCardReceiveManager carCostOilCardReceiveManager;
    @Resource
    private CarCostOilCardReceiveDataTracerService carCostOilCardReceiveDataTracerService;

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
     * 油卡收入详情
     *
     * @param oilCardReceiveId
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long oilCardReceiveId) {
        CarCostTabulationDetailVO detailVO = carCostOilCardReceiveDao.selectDetail(oilCardReceiveId);
        return ResponseDTO.ok(detailVO);
    }

    /**
     * 油卡费用申请
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(CarCostOilCardReceiveAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = addForm.getWaybillId();
        Long vehicleId = addForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long oilCardId = addForm.getOilCardId();
        Long enterpriseId = addForm.getEnterpriseId();
        // 校验参数
        ResponseDTO<OilCardEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, oilCardId);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setEnterpriseId(enterpriseId);
        oilCardReceiveEntity.setWaybillId(waybillId);
        oilCardReceiveEntity.setVehicleId(vehicleId);
        oilCardReceiveEntity.setDriverId(driverId);
        oilCardReceiveEntity.setOilCardId(oilCardId);
        oilCardReceiveEntity.setAmount(addForm.getAmount());
        oilCardReceiveEntity.setAttachment(addForm.getAttachment());
        oilCardReceiveEntity.setRemark(addForm.getRemark());
        oilCardReceiveEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        oilCardReceiveEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        oilCardReceiveEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        oilCardReceiveEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        oilCardReceiveEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setEnterpriseId(enterpriseId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setModuleType(CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue());
        tabulationEntity.setAmount(addForm.getAmount());
        tabulationEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setOilCardId(oilCardId);
        tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        carCostOilCardReceiveManager.handleAdd(oilCardReceiveEntity, tabulationEntity);
        // 保存日志
        CarCostOilCardReceiveVO cardReceiveVO = SmartBeanUtil.copy(oilCardReceiveEntity, CarCostOilCardReceiveVO.class);
        cardReceiveVO.setOilCardNo(resp.getData().getOilCardNo());
        carCostOilCardReceiveDataTracerService.saveOilCardReceiveLog(vehicleId, cardReceiveVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 油卡收入编辑
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(CarCostOilCardReceiveUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        // 校验收入记录
        Long oilCardReceiveId = updateForm.getOilCardReceiveId();
        CarCostOilCardReceiveEntity beforeReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        if (beforeReceiveEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡收入记录不存在");
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeReceiveEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "油卡收入记录已审核");
        }

        Long waybillId = updateForm.getWaybillId();
        Long vehicleId = updateForm.getVehicleId();
        Long driverId = dataTracerRequestForm.getOperatorId();
        Long oilCardId = updateForm.getOilCardId();
        // 校验参数
        ResponseDTO<OilCardEntity> resp = carCostCheckService.checkParam(waybillId, vehicleId, driverId, oilCardId);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }

        CarCostOilCardReceiveEntity oilCardReceiveEntity = new CarCostOilCardReceiveEntity();
        oilCardReceiveEntity.setOilCardReceiveId(oilCardReceiveId);
        oilCardReceiveEntity.setWaybillId(waybillId);
        oilCardReceiveEntity.setVehicleId(vehicleId);
        oilCardReceiveEntity.setDriverId(driverId);
        oilCardReceiveEntity.setOilCardId(oilCardId);
        oilCardReceiveEntity.setAmount(updateForm.getAmount());
        oilCardReceiveEntity.setAttachment(updateForm.getAttachment());
        oilCardReceiveEntity.setRemark(updateForm.getRemark());

        Integer moduleType = CarCostModuleTypeEnum.OIL_CARD_RECEIVE.getValue();
        Long tabulationId = carCostTabulationDao.selectTabulationIdByModule(oilCardReceiveId, moduleType);
        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setAmount(updateForm.getAmount());
        tabulationEntity.setDriverId(driverId);
        tabulationEntity.setVehicleId(vehicleId);
        tabulationEntity.setOilCardId(oilCardId);

        carCostOilCardReceiveManager.handleUpdate(oilCardReceiveEntity, tabulationEntity);
        saveUpdateLog(dataTracerRequestForm, oilCardReceiveId, beforeReceiveEntity, vehicleId, resp.getData());
        return ResponseDTO.ok();
    }

    private void saveUpdateLog(DataTracerRequestForm dataTracerRequestForm,
                               Long oilCardReceiveId,
                               CarCostOilCardReceiveEntity beforeReceiveEntity,
                               Long vehicleId,
                               OilCardEntity afterOilCardEntity) {
        CarCostOilCardReceiveVO beforeReceiveVO = SmartBeanUtil.copy(beforeReceiveEntity, CarCostOilCardReceiveVO.class);
        OilCardEntity oilCardEntity = oilCardDao.selectById(beforeReceiveVO.getOilCardId());
        beforeReceiveVO.setOilCardNo(oilCardEntity == null ? StringConst.EMPTY : oilCardEntity.getOilCardNo());
        CarCostOilCardReceiveEntity afterReceiveEntity = carCostOilCardReceiveDao.selectById(oilCardReceiveId);
        CarCostOilCardReceiveVO afterReceiveVO = SmartBeanUtil.copy(afterReceiveEntity, CarCostOilCardReceiveVO.class);
        afterReceiveVO.setOilCardNo(afterOilCardEntity.getOilCardNo());
        carCostOilCardReceiveDataTracerService.updateOilCardReceiveLog(vehicleId, beforeReceiveVO, afterReceiveVO, dataTracerRequestForm);
    }

}
