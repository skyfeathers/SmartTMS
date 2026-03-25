package net.lab1024.tms.admin.module.business.insurance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.expiredcertificate.ExpiredCertificateService;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateDelDTO;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceAddForm;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceQueryForm;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceUpdateForm;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceExcelVO;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.business.insurance.domain.InsuranceEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncryptUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * 保险管理 Service
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:09
 */
@Service
public class InsuranceService {

    @Autowired
    private InsuranceDao insuranceDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private ExpiredCertificateService expiredCertificateService;
    @Autowired
    private InsuranceManager insuranceManager;
    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private InsuranceDataTracerService insuranceDataTracerService;

    /**
     * 分页查询保险信息
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<InsuranceVO>> queryByPage(InsuranceQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<InsuranceVO> insuranceList = insuranceDao.queryByPage(page, queryDTO);
        if (CollectionUtils.isEmpty(insuranceList)) {
            PageResult<InsuranceVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, Lists.newArrayList());
            return ResponseDTO.ok(pageResultDTO);
        }
        for (InsuranceVO insuranceVO : insuranceList) {
            // 保险公司名称
            insuranceVO.setInsuranceCompanyName(dictCacheService.selectValueNameByValueCode(insuranceVO.getInsuranceCompanyCode()));
        }
        // 对应模块名称
        this.buildModuleName(insuranceList);
        FieldEncryptUtil.injectList(insuranceList);
        PageResult<InsuranceVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, insuranceList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 构建每条数据的模块名称
     *
     * @param insuranceList
     */
    private void buildModuleName(List<InsuranceVO> insuranceList) {
        Map<Integer, List<InsuranceVO>> insuranceListMap = insuranceList.stream().collect(Collectors.groupingBy(InsuranceVO::getModuleType));
        for (Map.Entry<Integer, List<InsuranceVO>> entry : insuranceListMap.entrySet()) {
            Integer moduleType = entry.getKey();
            InsuranceModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceModuleTypeEnum.class);
            List<InsuranceVO> moduleInsuranceVOList = entry.getValue();

            if (InsuranceModuleTypeEnum.VEHICLE == moduleTypeEnum) {
                this.buildVehicleModuleName(moduleInsuranceVOList);
            }

            if (InsuranceModuleTypeEnum.BRACKET == moduleTypeEnum) {
                this.buildBracketModuleName(moduleInsuranceVOList);
            }
        }

    }

    /**
     * 车辆的模块名称
     *
     * @param moduleInsuranceList
     */
    private void buildVehicleModuleName(List<InsuranceVO> moduleInsuranceList) {
        Set<Long> vehicleIdList = moduleInsuranceList.stream().map(InsuranceVO::getModuleId).collect(Collectors.toSet());
        Map<Long, VehicleEntity> vehicleEntityMap = Maps.newHashMap();
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (CollectionUtils.isNotEmpty(vehicleEntityList)) {
            vehicleEntityMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, Function.identity()));

        }
        for (InsuranceVO insuranceVO : moduleInsuranceList) {
            VehicleEntity vehicleEntity = vehicleEntityMap.get(insuranceVO.getModuleId());
            if (vehicleEntity != null) {
                insuranceVO.setModuleName(vehicleEntity.getVehicleNumber());
            }
        }
    }

    /**
     * 挂车的模块名称
     *
     * @param moduleInsuranceList
     */
    private void buildBracketModuleName(List<InsuranceVO> moduleInsuranceList) {
        Set<Long> bracketIdList = moduleInsuranceList.stream().map(InsuranceVO::getModuleId).collect(Collectors.toSet());
        Map<Long, BracketEntity> bracketEntityMap = Maps.newHashMap();
        List<BracketEntity> bracketEntityList = bracketDao.selectBatchIds(bracketIdList);
        if (CollectionUtils.isNotEmpty(bracketEntityList)) {
            bracketEntityMap = bracketEntityList.stream().collect(Collectors.toMap(BracketEntity::getBracketId, Function.identity()));
        }
        for (InsuranceVO insuranceVO : moduleInsuranceList) {
            BracketEntity bracketEntity = bracketEntityMap.get(insuranceVO.getModuleId());
            if (bracketEntity != null) {
                insuranceVO.setModuleName(bracketEntity.getBracketNo());
            }
        }
    }


    /**
     * 新建保险信息
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */

    public ResponseDTO<String> save(InsuranceAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        InsuranceService insuranceService = (InsuranceService) AopContext.currentProxy();
        InsuranceEntity insuranceEntity = insuranceService.addHandle(addForm, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        //操作日志
        insuranceDataTracerService.saveLog(insuranceEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Throwable.class)
    public InsuranceEntity addHandle(InsuranceAddForm addForm, Long operatorId, String operatorName) {
        InsuranceEntity insuranceEntity = SmartBeanUtil.copy(addForm, InsuranceEntity.class);
        insuranceEntity.setCreateUserId(operatorId);
        insuranceEntity.setCreateUserName(operatorName);
        insuranceDao.insert(insuranceEntity);
        this.updateExpiredCertificate(addForm);
        return insuranceEntity;
    }

    /**
     * 更新保险
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(InsuranceUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        InsuranceEntity beforeEntity = insuranceDao.selectById(updateForm.getInsuranceId());
        if (null == beforeEntity) {
            return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR);
        }
        InsuranceService insuranceService = (InsuranceService) AopContext.currentProxy();
        insuranceService.updateHandle(updateForm, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        // 变动后数据
        InsuranceEntity afterEntity = insuranceDao.selectById(updateForm.getInsuranceId());
        //操作日志
        insuranceDataTracerService.updateLog(beforeEntity, afterEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    @Transactional(rollbackFor = Throwable.class)
    public void updateHandle(InsuranceUpdateForm updateForm, Long operatorId, String operatorName) {
        InsuranceEntity updateEntity = SmartBeanUtil.copy(updateForm, InsuranceEntity.class);
        updateEntity.setUpdateUserId(operatorId);
        updateEntity.setUpdateUserName(operatorName);
        insuranceDao.updateById(updateEntity);
        this.updateExpiredCertificate(updateForm);
    }

    /**
     * 更新到期证件
     *
     * @param addForm
     */
    private void updateExpiredCertificate(InsuranceAddForm addForm) {
        ExpiredCertificateModuleTypeEnum certificateModuleTypeEnum = this.getExpiredCertificateModuleType(addForm.getModuleType());
        if (certificateModuleTypeEnum == null) {
            return;
        }
        ExpiredCertificateTypeEnum certificateTypeEnum = this.getExpiredCertificateType(addForm.getModuleType());
        if (certificateTypeEnum == null) {
            return;
        }
        String moduleName = "";
        if (ExpiredCertificateModuleTypeEnum.BRACKET == certificateModuleTypeEnum) {
            BracketEntity bracketEntity = bracketDao.selectById(addForm.getModuleId());
            moduleName = bracketEntity == null ? "" : bracketEntity.getBracketNo();
        }
        if (ExpiredCertificateModuleTypeEnum.VEHICLE == certificateModuleTypeEnum) {
            VehicleEntity vehicleEntity = vehicleDao.selectById(addForm.getModuleId());
            moduleName = vehicleEntity == null ? "" : vehicleEntity.getVehicleNumber();
        }
        ExpiredCertificateAddDTO baseDTO = new ExpiredCertificateAddDTO();
        baseDTO.setEnterpriseId(addForm.getEnterpriseId());
        baseDTO.setModuleType(certificateModuleTypeEnum);
        baseDTO.setModuleId(addForm.getModuleId());
        baseDTO.setModuleName(moduleName);
        if (null != addForm.getExpireDate()) {
            ExpiredCertificateAddDTO insuranceDTO = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            insuranceDTO.setEnterpriseId(addForm.getEnterpriseId());
            insuranceDTO.setType(certificateTypeEnum);
            insuranceDTO.setExpiredTime(addForm.getExpireDate());
            expiredCertificateService.save(insuranceDTO);
        }
    }

    /**
     * 获取到期证件的模块类型
     *
     * @param moduleType
     * @return
     */
    private ExpiredCertificateModuleTypeEnum getExpiredCertificateModuleType(Integer moduleType) {
        InsuranceModuleTypeEnum type = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceModuleTypeEnum.class);
        switch (type) {
            case BRACKET:
                return ExpiredCertificateModuleTypeEnum.BRACKET;
            case VEHICLE:
                return ExpiredCertificateModuleTypeEnum.VEHICLE;
            default:
                return null;
        }
    }

    /**
     * 获取到期证件的类型
     *
     * @param moduleType
     * @return
     */
    private ExpiredCertificateTypeEnum getExpiredCertificateType(Integer moduleType) {
        InsuranceTypeEnum type = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceTypeEnum.class);
        switch (type) {
            case COMMERCIAL_INSURANCE:
                return ExpiredCertificateTypeEnum.SHANG_YE_XIAN;
            case COMPULSORY_TRAFFIC_INSURANCE:
                return ExpiredCertificateTypeEnum.JIAO_QIANG_XIAN;
            default:
                return null;
        }
    }

    /**
     * 保险删除
     *
     * @param insuranceIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> insuranceIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<InsuranceEntity> insuranceEntityList = insuranceManager.getBaseMapper().selectBatchIds(insuranceIdList);
        if (CollectionUtils.isEmpty(insuranceEntityList)) {
            return ResponseDTO.ok();
        }
        InsuranceService insuranceService = (InsuranceService) AopContext.currentProxy();
        insuranceService.deleteHandle(insuranceEntityList, dataTracerRequestForm.getOperatorId());
        //操作日志
        insuranceDataTracerService.batchDeleteLog(insuranceEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHandle(List<InsuranceEntity> insuranceEntityList, Long requestUserId) {
        List<InsuranceEntity> deleteEntityList = Lists.newLinkedList();
        // 删除过期证件
        for (InsuranceEntity insuranceEntity : insuranceEntityList) {
            Integer moduleType = insuranceEntity.getModuleType();
            ExpiredCertificateModuleTypeEnum expiredCertificateModuleTypeEnum = this.getExpiredCertificateModuleType(moduleType);
            if (expiredCertificateModuleTypeEnum == null) {
                continue;
            }
            ExpiredCertificateTypeEnum expiredCertificateTypeEnum = this.getExpiredCertificateType(moduleType);
            if (expiredCertificateTypeEnum == null) {
                continue;
            }
            ExpiredCertificateDelDTO delDTO = new ExpiredCertificateDelDTO();
            delDTO.setModuleType(expiredCertificateModuleTypeEnum);
            delDTO.setModuleId(insuranceEntity.getModuleId());
            delDTO.setType(expiredCertificateTypeEnum);
            expiredCertificateService.del(delDTO);

            // 待删除数据
            InsuranceEntity deleteEntity = new InsuranceEntity();
            deleteEntity.setInsuranceId(insuranceEntity.getInsuranceId());
            deleteEntity.setDeletedFlag(Boolean.TRUE);
            deleteEntity.setUpdateUserId(requestUserId);
            deleteEntityList.add(deleteEntity);
        }
        // 更改删除状态
        insuranceManager.updateBatchById(deleteEntityList);
    }


    /**
     * 根据模块ID以及类型查询列表
     *
     * @param moduleId
     * @param moduleType
     * @return
     */
    public List<InsuranceVO> selectByModuleIdAndType(Long moduleId, Integer moduleType) {
        List<InsuranceEntity> insuranceList = insuranceDao.selectByModuleIdAndType(moduleId, moduleType);
        List<InsuranceVO> voList = SmartBeanUtil.copyList(insuranceList, InsuranceVO.class);
        // 设置保险公司名称
        if (CollectionUtils.isNotEmpty(voList)) {
            voList.forEach(e -> {
                e.setInsuranceCompanyName(dictCacheService.selectValueNameByValueCode(e.getInsuranceCompanyCode()));
            });
        }
        return voList;
    }

    /**
     * 根据车辆ID以及创建时间筛选
     *
     * @param vehicleId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<InsuranceVO> selectListByVehicleId(Long vehicleId, LocalDate startTime, LocalDate endTime) {
        InsuranceQueryForm insuranceQueryForm = new InsuranceQueryForm();
        insuranceQueryForm.setModuleId(vehicleId);
        insuranceQueryForm.setModuleType(RepairModuleTypeEnum.VEHICLE.getValue());
        insuranceQueryForm.setCreateStartDate(startTime);
        insuranceQueryForm.setCreateEndDate(endTime);
        List<InsuranceVO> repairList = insuranceDao.queryByPage(null, insuranceQueryForm);
        repairList.forEach(e -> {
            e.setInsuranceCompanyName(dictCacheService.selectValueNameByValueCode(e.getInsuranceCompanyCode()));
        });
        return repairList;
    }

    /**
     * 根据车辆ID以及创建时间筛选
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    public List<InsuranceVO> selectListByVehicleIdList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        InsuranceQueryForm insuranceQueryForm = new InsuranceQueryForm();
        insuranceQueryForm.setModuleIdList(vehicleIdList);
        insuranceQueryForm.setModuleType(RepairModuleTypeEnum.VEHICLE.getValue());
        insuranceQueryForm.setCreateStartDate(startTime);
        insuranceQueryForm.setCreateEndDate(endTime);
        insuranceQueryForm.setEnterpriseId(enterpriseId);
        List<InsuranceVO> repairList = insuranceDao.queryByPage(null, insuranceQueryForm);
        repairList.forEach(e -> {
            e.setInsuranceCompanyName(dictCacheService.selectValueNameByValueCode(e.getInsuranceCompanyCode()));
        });
        return repairList;
    }

    /**
     * 保险费用
     *
     * @param queryForm
     * @return
     */
    public Map<Long, BigDecimal> getCarCostInsuranceAmount(CarCostMonthStatisticQueryForm queryForm) {
        List<CarCostVehicleMonthAmountDTO> carCostVehicleMonthAmountDTOList = insuranceDao.sumByModuleIdListAndType(queryForm, RepairModuleTypeEnum.VEHICLE.getValue());
        return carCostVehicleMonthAmountDTOList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
    }

    /**
     * 保险导出
     * @param queryDTO
     * @return
     */
    public List<InsuranceExcelVO> queryByExport(InsuranceQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        List<InsuranceVO> insuranceList = insuranceDao.queryByPage(null, queryDTO);
        if (CollectionUtils.isEmpty(insuranceList)) {
            return Lists.newArrayList();
        }
        for (InsuranceVO insuranceVO : insuranceList) {
            // 保险公司名称
            insuranceVO.setInsuranceCompanyName(dictCacheService.selectValueNameByValueCode(insuranceVO.getInsuranceCompanyCode()));
        }
        // 对应模块名称
        this.buildModuleName(insuranceList);
        return SmartBeanUtil.copyList(insuranceList, InsuranceExcelVO.class);
    }
}
