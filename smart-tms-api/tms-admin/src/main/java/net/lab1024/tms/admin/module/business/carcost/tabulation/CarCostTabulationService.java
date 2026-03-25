package net.lab1024.tms.admin.module.business.carcost.tabulation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.carcost.cashpay.CarCostCashPayDao;
import net.lab1024.tms.admin.module.business.carcost.cashpay.CarCostCashPayService;
import net.lab1024.tms.admin.module.business.carcost.cashpay.domain.CarCostCashPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.CarCostCashReceiveDao;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.CarCostCashReceiveService;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveAuditForm;
import net.lab1024.tms.admin.module.business.carcost.etcpay.CarCostEtcPayDao;
import net.lab1024.tms.admin.module.business.carcost.etcpay.CarCostEtcPayService;
import net.lab1024.tms.admin.module.business.carcost.etcpay.domain.CarCostEtcPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.CarCostOilCardReceiveDao;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.CarCostOilCardReceiveService;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveAuditForm;
import net.lab1024.tms.admin.module.business.carcost.oilpay.CarCostOilPayDao;
import net.lab1024.tms.admin.module.business.carcost.oilpay.CarCostOilPayService;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayAuditForm;
import net.lab1024.tms.admin.module.business.carcost.tabulation.domain.*;
import net.lab1024.tms.admin.module.business.carcost.ureapay.CarCostUreaPayDao;
import net.lab1024.tms.admin.module.business.carcost.ureapay.CarCostUreaPayService;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayAuditForm;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillCommonService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarCostTabulationService {

    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;
    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;
    @Resource
    private CarCostCashPayDao carCostCashPayDao;
    @Resource
    private CarCostOilPayDao carCostOilPayDao;
    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;
    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;
    @Resource
    private CarCostCashReceiveService carCostCashReceiveService;
    @Resource
    private CarCostOilCardReceiveService carCostOilCardReceiveService;
    @Resource
    private CarCostCashPayService carCostCashPayService;
    @Resource
    private CarCostOilPayService carCostOilPayService;
    @Resource
    private CarCostEtcPayService carCostEtcPayService;
    @Resource
    private CarCostUreaPayService carCostUreaPayService;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private WaybillCommonService waybillCommonService;
    @Resource
    private DriverDao driverDao;

    /**
     * 自有车费用列表-简单列表
     *
     * @return
     */
    public ResponseDTO<List<CarCostTabulationSimpleVO>> simpleList(Long enterpriseId) {
        List<CarCostTabulationSimpleVO> tabulationSimpleVOList = carCostTabulationDao.selectByEnterpriseId(enterpriseId, 5);
        return ResponseDTO.ok(tabulationSimpleVOList);
    }

    /**
     * 自有车费用列表-列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<CarCostTabulationVO>> page(CarCostTabulationQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CarCostTabulationSimpleVO> simpleVOList = carCostTabulationDao.selectByPage(page, queryForm);
        List<CarCostTabulationVO> tabulationVOList = this.buildDetailInfo(simpleVOList);
        PageResult<CarCostTabulationVO> pageResult = SmartPageUtil.convert2PageResult(page, tabulationVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 根据运单id查询
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostTabulationVO>> queryList(Long waybillId) {
        List<CarCostTabulationSimpleVO> simpleVOList = carCostTabulationDao.selectByWaybillId(waybillId);
        List<CarCostTabulationVO> tabulationVOList = this.buildDetailInfo(simpleVOList);
        return ResponseDTO.ok(tabulationVOList);
    }


    /**
     * 设置详情字段信息
     *
     * @param simpleVOList
     * @return
     */
    private List<CarCostTabulationVO> buildDetailInfo(List<CarCostTabulationSimpleVO> simpleVOList) {
        if (CollUtil.isEmpty(simpleVOList)) {
            return Lists.newArrayList();
        }

        Map<CarCostModuleTypeEnum, List<Long>> tabulationIdMap =
                simpleVOList.stream().collect(Collectors.groupingBy(e ->
                        SmartBaseEnumUtil.getEnumByValue(e.getModuleType(), CarCostModuleTypeEnum.class),
                        Collectors.mapping(e -> e.getTabulationId(), Collectors.toList())));

        List<CarCostTabulationVO> tabulationVOList = Lists.newArrayList();
        for (Map.Entry<CarCostModuleTypeEnum, List<Long>> entry : tabulationIdMap.entrySet()) {
            CarCostModuleTypeEnum moduleTypeEnum = entry.getKey();
            List<Long> tabulationIdList = entry.getValue();

            List<CarCostTabulationVO> tabulationList;
            switch (moduleTypeEnum) {
                case CASH_RECEIVE: {
                    tabulationList = carCostCashReceiveDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                    break;
                }
                case OIL_CARD_RECEIVE: {
                    tabulationList = carCostOilCardReceiveDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                    break;
                }
                case CASH_PAY: {
                    tabulationList = carCostCashPayDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                    break;
                }
                case OIL_PAY: {
                    tabulationList = carCostOilPayDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                    break;
                }
                case ETC_PAY: {
                    tabulationList = carCostEtcPayDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                    break;
                }
                case UREA_PAY: {
                    tabulationList = carCostUreaPayDao.selectDetailByTabulationIdList(tabulationIdList);
                    tabulationVOList.addAll(tabulationList);
                }
            }
        }

        tabulationVOList = tabulationVOList
                .stream()
                .sorted(Comparator.comparing(CarCostTabulationSimpleVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return tabulationVOList;
    }

    /**
     * 费用详情
     *
     * @param moduleId
     * @param moduleType
     * @return
     */
    public ResponseDTO<CarCostTabulationDetailVO> detail(Long moduleId, Integer moduleType) {
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        CarCostTabulationDetailVO carCostTabulationDetailVO;
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                carCostTabulationDetailVO = carCostCashReceiveDao.selectDetail(moduleId);
                break;
            }
            case OIL_CARD_RECEIVE: {
                carCostTabulationDetailVO = carCostOilCardReceiveDao.selectDetail(moduleId);
                break;
            }
            case CASH_PAY: {
                carCostTabulationDetailVO = carCostCashPayDao.selectDetail(moduleId);
                break;
            }
            case OIL_PAY: {
                carCostTabulationDetailVO = carCostOilPayDao.selectDetail(moduleId);
                break;
            }
            case ETC_PAY: {
                carCostTabulationDetailVO = carCostEtcPayDao.selectDetail(moduleId);
                break;
            }
            case UREA_PAY: {
                carCostTabulationDetailVO = carCostUreaPayDao.selectDetail(moduleId);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "自有车模块类型错误");
            }
        }
        if (ObjectUtil.isEmpty(carCostTabulationDetailVO)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        carCostTabulationDetailVO.setModuleType(moduleType);
        return ResponseDTO.ok(carCostTabulationDetailVO);
    }

    /**
     * 在费用列表中审核审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> audit(CarCostTabulationAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Long tabulationId = auditForm.getTabulationId();
        CarCostTabulationEntity dbTabulationEntity = carCostTabulationDao.selectById(tabulationId);
        if (ObjectUtil.isEmpty(dbTabulationEntity)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用列表数据不存在");
        }
        Long waybillId = dbTabulationEntity.getWaybillId();
        Long moduleId = dbTabulationEntity.getModuleId();
        Integer moduleType = dbTabulationEntity.getModuleType();
        Integer auditStatus = auditForm.getAuditStatus();
        String auditRemark = auditForm.getAuditRemark();
        ResponseDTO<String> res;
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                CarCostCashReceiveAuditForm cashReceiveAuditForm = new CarCostCashReceiveAuditForm();
                cashReceiveAuditForm.setCashReceiveId(moduleId);
                cashReceiveAuditForm.setAuditStatus(auditStatus);
                cashReceiveAuditForm.setAuditRemark(auditRemark);
                res = carCostCashReceiveService.audit(cashReceiveAuditForm, dataTracerRequestForm);
                break;
            }
            case OIL_CARD_RECEIVE: {
                CarCostOilCardReceiveAuditForm oilCardReceiveAuditForm = new CarCostOilCardReceiveAuditForm();
                oilCardReceiveAuditForm.setOilCardReceiveId(moduleId);
                oilCardReceiveAuditForm.setAuditStatus(auditStatus);
                oilCardReceiveAuditForm.setAuditRemark(auditRemark);
                res = carCostOilCardReceiveService.audit(oilCardReceiveAuditForm, dataTracerRequestForm);
                break;
            }
            case CASH_PAY: {
                CarCostCashPayAuditForm cashPayAuditForm = new CarCostCashPayAuditForm();
                cashPayAuditForm.setCashPayId(moduleId);
                cashPayAuditForm.setAuditStatus(auditStatus);
                cashPayAuditForm.setAuditRemark(auditRemark);
                res = carCostCashPayService.audit(cashPayAuditForm, dataTracerRequestForm);
                break;
            }
            case OIL_PAY: {
                CarCostOilPayAuditForm oilPayAuditForm = new CarCostOilPayAuditForm();
                oilPayAuditForm.setOilPayId(moduleId);
                oilPayAuditForm.setAuditStatus(auditStatus);
                oilPayAuditForm.setAuditRemark(auditRemark);
                res = carCostOilPayService.audit(oilPayAuditForm, dataTracerRequestForm);
                break;
            }
            case ETC_PAY: {
                CarCostEtcPayAuditForm etcPayAuditForm = new CarCostEtcPayAuditForm();
                etcPayAuditForm.setEtcPayId(moduleId);
                etcPayAuditForm.setAuditStatus(auditStatus);
                etcPayAuditForm.setAuditRemark(auditRemark);
                res = carCostEtcPayService.audit(etcPayAuditForm, dataTracerRequestForm);
                break;
            }
            case UREA_PAY: {
                CarCostUreaPayAuditForm ureaPayAuditForm = new CarCostUreaPayAuditForm();
                ureaPayAuditForm.setUreaPayId(moduleId);
                ureaPayAuditForm.setAuditStatus(auditStatus);
                ureaPayAuditForm.setAuditRemark(auditRemark);
                res = carCostUreaPayService.audit(ureaPayAuditForm, dataTracerRequestForm);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "模块类型错误");
            }
        }

        if (!res.getOk()) {
            return res;
        }

        CarCostTabulationEntity updateTabulationEntity = new CarCostTabulationEntity();
        updateTabulationEntity.setTabulationId(tabulationId);
        updateTabulationEntity.setAuditStatus(auditStatus);
        updateTabulationEntity.setAuditRemark(auditRemark);
        carCostTabulationDao.updateById(updateTabulationEntity);
        if (waybillId != null) {
            waybillCommonService.updateProfitAmount(waybillId);
        }
        return res;
    }

    /**
     * 批量审核
     *
     * @param batchAuditForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> batchAudit(CarCostTabulationBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> tabulationIdList = batchAuditForm.getTabulationIdList();
        if (tabulationIdList.size() > 15) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "最多只能操作15条");
        }
        List<CarCostTabulationEntity> carCostTabulationEntityList = carCostTabulationDao.selectBatchIds(tabulationIdList);
        if (CollectionUtils.isEmpty(carCostTabulationEntityList)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用列表数据不存在");
        }

        Optional<CarCostTabulationEntity> filterOptional = carCostTabulationEntityList.stream().filter(e->batchAuditForm.getAuditStatus().equals(e.getAuditStatus())).findFirst();
        if (filterOptional.isPresent()) {
            AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(batchAuditForm.getAuditStatus(), AuditStatusEnum.class);
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "存在费用处于"+auditStatusEnum.getDesc()+"状态的数据请移除后再次操作");
        }

        for (Long tabulationId : tabulationIdList) {
            CarCostTabulationAuditForm auditForm = new CarCostTabulationAuditForm();
            auditForm.setTabulationId(tabulationId);
            auditForm.setAuditStatus(batchAuditForm.getAuditStatus());
            auditForm.setAuditRemark(batchAuditForm.getAuditRemark());
            ResponseDTO<String> resp = this.audit(auditForm, dataTracerRequestForm);
            if (!resp.getOk()) {
                throw new BusinessException(resp.getMsg());
            }
        }
        return ResponseDTO.ok();
    }

    /**
     * 关联运单
     *
     * @param tabulationId
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> relateWaybill(Long tabulationId, Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostTabulationEntity dbTabulationEntity = carCostTabulationDao.selectById(tabulationId);
        if (dbTabulationEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用列表数据不存在");
        }
        if (dbTabulationEntity.getWaybillId() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录已关联运单");
        }
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单不存在或已作废");
        }
        if (!dbTabulationEntity.getEnterpriseId().equals(waybillEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("运单所属企业和当前登录企业不一致");
        }
        Long driverId = waybillEntity.getDriverId();
        if (!dbTabulationEntity.getDriverId().equals(driverId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "运单和司机不匹配");
        }
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (ObjectUtil.isEmpty(driverEntity) || driverEntity.getDeletedFlag()
                || DriverStatusEnum.DISABLED.equalsValue(driverEntity.getStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机不存在或状态错误");
        }

        Long moduleId = dbTabulationEntity.getModuleId();
        Integer moduleType = dbTabulationEntity.getModuleType();
        ResponseDTO<String> res;
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                res = carCostCashReceiveService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            case OIL_CARD_RECEIVE: {
                res = carCostOilCardReceiveService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            case CASH_PAY: {
                res = carCostCashPayService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            case OIL_PAY: {
                res = carCostOilPayService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            case ETC_PAY: {
                res = carCostEtcPayService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            case UREA_PAY: {
                res = carCostUreaPayService.relateWaybill(moduleId, waybillId, dataTracerRequestForm);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "模块类型错误");
            }
        }

        if (!res.getOk()) {
            return res;
        }

        CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
        tabulationEntity.setTabulationId(tabulationId);
        tabulationEntity.setWaybillId(waybillId);
        tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
        carCostTabulationDao.updateById(tabulationEntity);
        waybillCommonService.updateProfitAmount(waybillId);
        return ResponseDTO.ok();
    }

    /**
     * 批量关联运单
     *
     * @param relateForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> batchRelateWaybill(CarCostTabulationBatchRelateForm relateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = relateForm.getWaybillId();
        List<Long> tabulationIdList = relateForm.getTabulationIdList();

        StringBuilder errorMsg = new StringBuilder();
        for (Long tabulationId : tabulationIdList) {
            ResponseDTO<String> resp = this.relateWaybill(tabulationId, waybillId, dataTracerRequestForm);
            if (!resp.getOk()) {
                throw new BusinessException(resp.getMsg());
            }
        }

        if (errorMsg.length() > 0) {
            return ResponseDTO.userErrorParam(errorMsg.toString());
        }
        return ResponseDTO.ok();
    }

    /**
     * 取消关联运单
     *
     * @param tabulationId
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> cancelRelateWaybill(Long tabulationId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostTabulationEntity dbTabulationEntity = carCostTabulationDao.selectById(tabulationId);
        if (dbTabulationEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用列表数据不存在");
        }
        if (dbTabulationEntity.getWaybillId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "该费用记录还未关联运单");
        }

        Long moduleId = dbTabulationEntity.getModuleId();
        Integer moduleType = dbTabulationEntity.getModuleType();
        ResponseDTO<String> res;
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                res = carCostCashReceiveService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            case OIL_CARD_RECEIVE: {
                res = carCostOilCardReceiveService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            case CASH_PAY: {
                res = carCostCashPayService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            case OIL_PAY: {
                res = carCostOilPayService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            case ETC_PAY: {
                res = carCostEtcPayService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            case UREA_PAY: {
                res = carCostUreaPayService.cancelRelateWaybill(moduleId, dataTracerRequestForm);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "模块类型错误");
            }
        }

        if (!res.getOk()) {
            return res;
        }
        carCostTabulationDao.updateWaybillId(tabulationId, null);
        waybillCommonService.updateProfitAmount(dbTabulationEntity.getWaybillId());
        return ResponseDTO.ok();
    }

    /**
     * 批量取消关联运单
     *
     * @param relateForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> batchCancelRelateWaybill(CarCostTabulationBatchCancelRelateForm relateForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> tabulationIdList = relateForm.getTabulationIdList();
        if (tabulationIdList.size() > 15) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "最多只能操作15条");
        }
        StringBuilder errorMsg = new StringBuilder();
        for (Long tabulationId : tabulationIdList) {
            ResponseDTO<String> resp = cancelRelateWaybill(tabulationId, dataTracerRequestForm);
            if (!resp.getOk()) {
                throw new BusinessException(resp.getMsg());
            }
        }

        if (errorMsg.length() > 0) {
            return ResponseDTO.userErrorParam(errorMsg.toString());
        }
        return ResponseDTO.ok();
    }

    /**
     * 删除费用
     *
     * @param tabulationId
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> del(Long tabulationId, DataTracerRequestForm dataTracerRequestForm) {
        CarCostTabulationEntity dbTabulationEntity = carCostTabulationDao.selectById(tabulationId);
        if (dbTabulationEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "费用列表数据不存在");
        }
        Long waybillId = dbTabulationEntity.getWaybillId();
        Long moduleId = dbTabulationEntity.getModuleId();
        Integer moduleType = dbTabulationEntity.getModuleType();
        ResponseDTO<String> res;
        CarCostModuleTypeEnum moduleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, CarCostModuleTypeEnum.class);
        switch (moduleTypeEnum) {
            case CASH_RECEIVE: {
                res = carCostCashReceiveService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            case OIL_CARD_RECEIVE: {
                res = carCostOilCardReceiveService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            case CASH_PAY: {
                res = carCostCashPayService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            case OIL_PAY: {
                res = carCostOilPayService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            case ETC_PAY: {
                res = carCostEtcPayService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            case UREA_PAY: {
                res = carCostUreaPayService.del(dbTabulationEntity, moduleId, dataTracerRequestForm);
                break;
            }
            default: {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "模块类型错误");
            }
        }

        if (!res.getOk()) {
            return res;
        }
        if (waybillId != null) {
            waybillCommonService.updateProfitAmount(waybillId);
        }
        return ResponseDTO.ok();
    }

}
