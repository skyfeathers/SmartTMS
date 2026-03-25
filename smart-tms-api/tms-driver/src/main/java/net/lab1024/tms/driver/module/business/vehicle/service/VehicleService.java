package net.lab1024.tms.driver.module.business.vehicle.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.driver.module.business.bracket.dao.BracketDao;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverVehicleQueryForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVehicleQueryVO;
import net.lab1024.tms.driver.module.business.vehicle.dao.DriverVehicleDao;
import net.lab1024.tms.driver.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleAddForm;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleUpdateForm;
import net.lab1024.tms.driver.module.business.vehicle.domain.vo.VehicleDetailVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Resource
    private VehicleDao vehicleDao;
    @Resource
    private BracketDao bracketDao;
    @Resource
    private VehicleManager vehicleManager;
    @Resource
    private DictCacheService dictCacheService;
    @Resource
    private DriverVehicleDao driverVehicleDao;
    @Resource
    private VehicleDataTracerService vehicleDataTracerService;

    /**
     * 根据司机ID查询车辆列表
     *
     * @param queryForm
     * @param driver
     * @return
     */
    public PageResult<DriverVehicleQueryVO> getVehicleListByDriverId(DriverVehicleQueryForm queryForm, RequestUser driver) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setDriverId(driver.getUserId());
        queryForm.setUserType(driver.getUserType().getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DriverVehicleQueryVO> vehicleQueryVOList = vehicleDao.selectByList(page, queryForm);
        PageResult<DriverVehicleQueryVO> pageResult = SmartPageUtil.convert2PageResult(page, vehicleQueryVOList);
        return pageResult;
    }

    /**
     * 获取司机关联车辆
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<List<DriverVehicleQueryVO>> relateVehicleList(Long driverId) {
        List<VehicleEntity> vehicleList = vehicleDao.selectByDriverRelate(driverId, false);

        List<DriverVehicleQueryVO> vehicleVOList = vehicleList.stream().map(e -> SmartBeanUtil.copy(e, DriverVehicleQueryVO.class))
                .collect(Collectors.toList());

        return ResponseDTO.ok(vehicleVOList);
    }

    /**
     * 获取车辆详情
     *
     * @param vehicleId
     * @return
     */
    public ResponseDTO<VehicleDetailVO> getDetail(Long vehicleId) {
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        if (ObjectUtil.isEmpty(vehicleEntity) || vehicleEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        VehicleDetailVO detailVO = SmartBeanUtil.copy(vehicleEntity, VehicleDetailVO.class);
        detailVO.setVehicleEnergyTypeName(dictCacheService.selectValueNameByValueCode(detailVO.getVehicleEnergyType()));
        detailVO.setBracketNo(this.getBracketNo(vehicleEntity));
        detailVO.setVehiclePlateColor(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getVehiclePlateColorCode(), VehiclePlateColorEnum.class));
        detailVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getAuditStatus(), AuditStatusEnum.class));
        detailVO.setBusinessModeDesc(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getBusinessMode(), BusinessModeEnum.class));
        return ResponseDTO.ok(detailVO);
    }

    /**
     * 新增车辆信息
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(VehicleAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        VehicleEntity vehicleEntity = vehicleDao.selectByNumberExcludeIds(addForm.getVehicleNumber(), null, Boolean.FALSE);
        if (ObjectUtil.isNotEmpty(vehicleEntity)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车牌号已存在");
        }
        addForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        addForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        addForm.setOperatorType(dataTracerRequestForm.getOperatorType());
        addForm.setOperatorTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        addForm.setDriverId(dataTracerRequestForm.getOperatorId());
        Long vehicleId = vehicleManager.saveVehicle(addForm);
        vehicleDataTracerService.saveLog(vehicleId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新车辆信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(VehicleUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long vehicleId = updateForm.getVehicleId();
        VehicleEntity dbVehicleEntity = vehicleDao.selectById(vehicleId);
        if (ObjectUtil.isEmpty(dbVehicleEntity) || dbVehicleEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "车辆不存在");
        }
        if (ObjectUtil.isNotEmpty(updateForm.getVehicleNumber())) {
            VehicleEntity vehicleEntity = vehicleDao.selectByNumberExcludeIds(updateForm.getVehicleNumber(), Lists.newArrayList(vehicleId), Boolean.FALSE);
            if (ObjectUtil.isNotEmpty(vehicleEntity)) {
                return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车牌号已存在");
            }
        }
        VehicleDetailVO beforeDate = getDetail(vehicleId).getData();
        updateForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        updateForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        vehicleManager.updateVehicle(updateForm);
        VehicleDetailVO afterDate = getDetail(vehicleId).getData();
        vehicleDataTracerService.updateLog(beforeDate, afterDate, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取挂车车牌号
     *
     * @param vehicleEntity
     * @return
     */
    private String getBracketNo(VehicleEntity vehicleEntity) {
        if (ObjectUtil.isNotEmpty(vehicleEntity.getBracketId())) {
            BracketEntity bracketEntity = bracketDao.selectById(vehicleEntity.getBracketId());
            if (ObjectUtil.isNotEmpty(bracketEntity) && !bracketEntity.getDeletedFlag()) {
                return bracketEntity.getBracketNo();
            }
        }
        return null;
    }

}
