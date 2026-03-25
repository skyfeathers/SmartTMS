package net.lab1024.tms.admin.module.business.quick;

import cn.hutool.core.util.StrUtil;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.bracket.BracketDataTracerService;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.driver.service.DriverDataTracerService;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.quick.constant.QuickCreateTypeEnum;
import net.lab1024.tms.admin.module.business.quick.domain.bo.QuickSaveBO;
import net.lab1024.tms.admin.module.business.quick.domain.form.QuickSearchForm;
import net.lab1024.tms.admin.module.business.quick.domain.form.VehicleQuickCreateForm;
import net.lab1024.tms.admin.module.business.quick.domain.vo.QuickSearchVO;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.service.VehicleDataTracerService;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 快速创建司机、车辆、挂车业务处理
 *
 * @author lidoudou
 * @date 2022/10/17 下午3:21
 */
@Service
public class QuickService {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private DriverDataTracerService driverDataTracerService;
    @Autowired
    private VehicleDataTracerService vehicleDataTracerService;
    @Autowired
    private BracketDataTracerService bracketDataTracerService;


    public ResponseDTO<String> save(VehicleQuickCreateForm createForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        ResponseDTO<String> validateResult = this.validate(createForm, enterpriseId);
        if (!validateResult.getOk()) {
            return validateResult;
        }
        createForm.setCreateUserId(dataTracerRequestForm.getOperatorId());
        createForm.setCreateUserName(dataTracerRequestForm.getOperatorName());
        createForm.setCreateUserType(dataTracerRequestForm.getOperatorType());
        createForm.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        QuickService quickService = (QuickService) AopContext.currentProxy();
        if (QuickCreateTypeEnum.DRIVER.equalsValue(createForm.getCreateType())) {
            Long driverId = quickService.saveDriver(createForm, enterpriseId);
            driverDataTracerService.saveLog(driverId, dataTracerRequestForm);
        } else if (QuickCreateTypeEnum.VEHICLE.equalsValue(createForm.getCreateType())) {
            Long vehicleId = quickService.saveVehicle(createForm, enterpriseId);
            vehicleDataTracerService.saveLog(vehicleId, dataTracerRequestForm);
        } else if (QuickCreateTypeEnum.BRACKET.equalsValue(createForm.getCreateType())) {
            Long bracketId = quickService.saveBracket(createForm, enterpriseId);
            bracketDataTracerService.saveLog(bracketId, dataTracerRequestForm);
        } else {
            QuickSaveBO quickSaveBO = quickService.saveAll(createForm, enterpriseId);

            driverDataTracerService.saveLog(quickSaveBO.getDriverId(), dataTracerRequestForm);
            bracketDataTracerService.saveLog(quickSaveBO.getBracketId(), dataTracerRequestForm);
            vehicleDataTracerService.saveLog(quickSaveBO.getVehicleId(), dataTracerRequestForm);
        }
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveDriver(VehicleQuickCreateForm createForm, Long enterpriseId){
        DriverEntity driverEntity = SmartBeanUtil.copy(createForm, DriverEntity.class);
        driverEntity.setEnterpriseId(enterpriseId);
        driverDao.insert(driverEntity);
        return driverEntity.getDriverId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveBracket(VehicleQuickCreateForm createForm, Long enterpriseId){
        BracketEntity bracketEntity = SmartBeanUtil.copy(createForm, BracketEntity.class);
        bracketEntity.setEnterpriseId(enterpriseId);
        bracketDao.insert(bracketEntity);
        return bracketEntity.getBracketId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveVehicle(VehicleQuickCreateForm createForm, Long enterpriseId){
        VehicleEntity vehicleEntity = SmartBeanUtil.copy(createForm, VehicleEntity.class);
        vehicleEntity.setEnterpriseId(enterpriseId);
        vehicleDao.insert(vehicleEntity);
        return vehicleEntity.getVehicleId();
    }

    @Transactional(rollbackFor = Exception.class)
    public QuickSaveBO saveAll(VehicleQuickCreateForm createForm, Long enterpriseId){
        Long driverId = this.saveDriver(createForm, enterpriseId);
        Long bracketId = this.saveBracket(createForm, enterpriseId);
        Long vehicleId = this.saveVehicle(createForm, enterpriseId);

        DriverVehicleEntity driverVehicleEntity = new DriverVehicleEntity();
        driverVehicleEntity.setDriverId(driverId);
        driverVehicleEntity.setVehicleId(vehicleId);
        driverVehicleDao.insert(driverVehicleEntity);

        vehicleDao.updateBracketId(vehicleId, bracketId);

        QuickSaveBO quickSaveBO = new QuickSaveBO();
        quickSaveBO.setDriverId(driverId);
        quickSaveBO.setBracketId(bracketId);
        quickSaveBO.setVehicleId(vehicleId);

        return quickSaveBO;
    }


    /**
     * 验证数据有效性
     *
     * @param createForm
     * @return
     */
    private ResponseDTO<String> validate(VehicleQuickCreateForm createForm, Long enterpriseId) {
        if (QuickCreateTypeEnum.DRIVER.equalsValue(createForm.getCreateType())) {
            ResponseDTO<String> resp = this.checkDriver(createForm, enterpriseId);
            if (!resp.getOk()) {
                return ResponseDTO.error(resp);
            }
        }
        if (QuickCreateTypeEnum.VEHICLE.equalsValue(createForm.getCreateType())) {
            ResponseDTO<String> resp = this.checkVehicle(createForm, enterpriseId);
            if (!resp.getOk()) {
                return ResponseDTO.error(resp);
            }
        }
        if (QuickCreateTypeEnum.BRACKET.equalsValue(createForm.getCreateType())) {
            ResponseDTO<String> resp = this.checkBracket(createForm, enterpriseId);
            if (!resp.getOk()) {
                return ResponseDTO.error(resp);
            }
        }
        if (QuickCreateTypeEnum.ALL.equalsValue(createForm.getCreateType())) {
            ResponseDTO<String> resp = this.checkAll(createForm, enterpriseId);
            if (!resp.getOk()) {
                return ResponseDTO.error(resp);
            }
        }

        return ResponseDTO.ok();
    }

    private ResponseDTO<String> checkAll(VehicleQuickCreateForm createForm, Long enterpriseId) {
        if (StrUtil.isBlank(createForm.getDriverName()) || StrUtil.isBlank(createForm.getTelephone())
                || StrUtil.isBlank(createForm.getVehicleNumber()) || StrUtil.isBlank(createForm.getBracketNo())) {
            return ResponseDTO.userErrorParam("信息校验错误，请完善后重试");
        }
        ResponseDTO<String> resp = this.checkDriver(createForm, enterpriseId);
        if (!resp.getOk()) {
            return resp;
        }
        resp = this.checkVehicle(createForm, enterpriseId);
        if (!resp.getOk()) {
            return resp;
        }
        resp = this.checkBracket(createForm, enterpriseId);
        if (!resp.getOk()) {
            return resp;
        }

        return ResponseDTO.ok();
    }

    private ResponseDTO<String> checkBracket(VehicleQuickCreateForm createForm, Long enterpriseId) {
        if (StrUtil.isBlank(createForm.getBracketNo())) {
            return ResponseDTO.userErrorParam("挂车车牌号不能为空");
        }
        List<BracketEntity> shipperBasicList = bracketDao.selectByNoExcludeIds(enterpriseId, createForm.getBracketNo(), null, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(shipperBasicList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "挂车车牌号已存在");
        }

        return ResponseDTO.ok();
    }

    private ResponseDTO<String> checkVehicle(VehicleQuickCreateForm createForm, Long enterpriseId) {
        if (StrUtil.isBlank(createForm.getVehicleNumber())) {
            return ResponseDTO.userErrorParam("车牌号不能为空");
        }
        List<VehicleEntity> vehicleList = vehicleDao.selectByNameExcludeIds(enterpriseId, createForm.getVehicleNumber(), null, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(vehicleList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车牌号已存在");
        }

        return ResponseDTO.ok();
    }

    private ResponseDTO<String> checkDriver(VehicleQuickCreateForm createForm, Long enterpriseId) {
        if (StrUtil.isBlank(createForm.getDriverName()) || StrUtil.isBlank(createForm.getTelephone())) {
            return ResponseDTO.userErrorParam("司机姓名或司机电话不能为空");
        }
        List<DriverEntity> driverList = driverDao.selectByPhoneExcludeIds(enterpriseId, createForm.getTelephone(), null, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(driverList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "司机电话已存在");
        }

        return ResponseDTO.ok();
    }

    public ResponseDTO<QuickSearchVO> quickSearch(QuickSearchForm quickSearchForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        QuickSearchTypeEnum searchTypeEnum = SmartBaseEnumUtil.getEnumByValue(quickSearchForm.getSearchType(), QuickSearchTypeEnum.class);
        Boolean successFlag = false;
        Long id = null;
        if (QuickSearchTypeEnum.ORDER == searchTypeEnum) {
            id = orderDao.selectIdByNumber(quickSearchForm.getKeywords());
            successFlag = id != null;
        }
        if (QuickSearchTypeEnum.WAYBILL == searchTypeEnum) {
            id = waybillDao.selectIdByNumber(quickSearchForm.getKeywords());
            successFlag = id != null;
        }
        if (QuickSearchTypeEnum.PAY_ORDER == searchTypeEnum) {
            id = payOrderDao.selectIdByNumber(quickSearchForm.getKeywords());
            successFlag = id != null;
        }
        if (QuickSearchTypeEnum.RECEIVE_ORDER == searchTypeEnum) {
            id = receiveOrderDao.selectIdByNumber(quickSearchForm.getKeywords());
            successFlag = id != null;
        }
        QuickSearchVO quickSearchVO = new QuickSearchVO();
        quickSearchVO.setSuccessFlag(successFlag);
        quickSearchVO.setSearchType(quickSearchForm.getSearchType());
        quickSearchVO.setId(id);
        return ResponseDTO.ok(quickSearchVO);
    }
}
