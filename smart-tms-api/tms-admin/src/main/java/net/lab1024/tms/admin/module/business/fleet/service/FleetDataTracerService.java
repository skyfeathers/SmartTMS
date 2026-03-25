package net.lab1024.tms.admin.module.business.fleet.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;
import net.lab1024.tms.admin.module.business.fleet.domain.form.FleetItemAddForm;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetDetailVO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.constants.FleetDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.fleet.constants.FleetItemTypeEnum;
import net.lab1024.tms.common.module.business.fleet.domain.FleetBankEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetItemEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class FleetDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;

    /**
     * 新增日志
     *
     * @param fleetId
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long fleetId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(fleetId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(FleetDetailVO beforeData, FleetDetailVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getFleetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDriverContent(beforeData), this.getDriverContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(FleetDetailVO.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 获取车辆信息
     *
     * @param detailVO
     * @return
     */
    private String getDriverContent(FleetDetailVO detailVO) {
        //基本信息
        String baseContent = this.getBaseContent(detailVO);
        //车队长信息
        String captainContent = this.getCaptain(detailVO);
        //银行卡
        String bankContent = this.getBankContent(detailVO.getBankList());

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(captainContent)
                .append(bankContent);
        return builder.toString();
    }

    private String getBaseContent(FleetDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【速记码】").append(DataTracerConstant.SPLIT).append(detailVO.getShorthandCode()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车队名称】").append(DataTracerConstant.SPLIT).append(detailVO.getFleetName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车队长】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车队长联系方式】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainPhone()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【负责人】").append(DataTracerConstant.SPLIT).append(detailVO.getManagerName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(detailVO.getRemark()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（人像面）】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCardBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（国徽面）】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCardFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证号】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCard()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getCaptain(FleetDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("车队长信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（人像面）】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCardBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（国徽面）】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCardFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证号】").append(DataTracerConstant.SPLIT).append(detailVO.getCaptainIdCard()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getAddress(FleetDetailVO fleetDetailVO) {
        if (fleetDetailVO == null) {
            return "";
        }
        String mailArea = "";
        if (StringUtils.isNotBlank(fleetDetailVO.getProvinceName())) {
            mailArea = mailArea + fleetDetailVO.getProvinceName();
        }
        if (StringUtils.isNotBlank(fleetDetailVO.getCityName())) {
            mailArea = mailArea + fleetDetailVO.getCityName();
        }
        if (StringUtils.isNotBlank(fleetDetailVO.getDistrictName())) {
            mailArea = mailArea + fleetDetailVO.getDistrictName();
        }
        return mailArea;
    }
    /**
     * 货主付款方式信息
     *
     * @param bankList
     * @return
     */
    private String getBankContent(List<FleetBankDTO> bankList) {
        StringBuilder builder = new StringBuilder();
        builder.append("银行卡信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(bankList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (FleetBankDTO fleetBankDTO : bankList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【开户名】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getAccountName()).append(DataTracerConstant.BLANK)
                    .append("【银行账号】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getBankAccount()).append(DataTracerConstant.BLANK)
                    .append("【银行名称】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getBankName()).append(DataTracerConstant.BLANK)
                    .append("【支行名称】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getBankBranchName()).append(DataTracerConstant.BLANK)
                    .append("【附件信息】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getAttachment()).append(DataTracerConstant.BLANK)
                    .append("【是否默认】").append(DataTracerConstant.SPLIT).append(fleetBankDTO.getDefaultFlag() ? "是" : "否").append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量删除日志
     *
     * @param fleetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchDeleteLog(List<Long> fleetIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(fleetIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long fleet : fleetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(fleet);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operateType);
    }



    /**
     * 批量审核日志
     *
     * @param driverIdList
     * @param auditStatus
     * @param auditRemark
     * @param dataTracerRequestForm
     */
    public void batchAuditLog(List<Long> driverIdList, Integer auditStatus, String auditRemark, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        String content = "修改审核状态为：" + auditStatusEnum.getDesc() + "；备注为：" + (StringUtils.isEmpty(auditRemark) ? "" : auditRemark);
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long driverId : driverIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(driverId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
            dataTracerForm.setOperateType(FleetDataTracerOperateTypeEnum.AUDIT);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operateType);
    }

    /**
     * 删除关联项
     * @param fleetItemEntity
     * @param dataTracerRequestForm
     */
    public void deleteItemLog(FleetItemEntity fleetItemEntity, DataTracerRequestForm dataTracerRequestForm) {

        FleetItemTypeEnum fleetItemTypeEnum = SmartBaseEnumUtil.getEnumByValue(fleetItemEntity.getItemType(),FleetItemTypeEnum.class);

        String itemName = "";
        FleetDataTracerOperateTypeEnum dataTracerOperateTypeEnum = null;
        if(fleetItemTypeEnum == FleetItemTypeEnum.DRIVER){
            dataTracerOperateTypeEnum = FleetDataTracerOperateTypeEnum.DEL_DRIVER;
            DriverEntity driverEntity = driverDao.selectById(fleetItemEntity.getItemId());
            itemName = driverEntity.getDriverName();
        }
        if(fleetItemTypeEnum == FleetItemTypeEnum.VEHICLE){
            dataTracerOperateTypeEnum = FleetDataTracerOperateTypeEnum.DEL_VEHICLE;
            VehicleEntity vehicleEntity = vehicleDao.selectById(fleetItemEntity.getItemId());
            itemName = vehicleEntity.getVehicleNumber();
        }
        if(dataTracerOperateTypeEnum == null){
            return;
        }
        String content = dataTracerOperateTypeEnum.getDesc() + itemName;

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(fleetItemEntity.getFleetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
        dataTracerForm.setOperateType(dataTracerOperateTypeEnum);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 添加关联项
     * @param addForm
     * @param dataTracerRequestForm
     */
    public void addItemLog(FleetItemAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        FleetItemTypeEnum fleetItemTypeEnum = SmartBaseEnumUtil.getEnumByValue(addForm.getItemType(),FleetItemTypeEnum.class);

        String itemName = "";
        FleetDataTracerOperateTypeEnum dataTracerOperateTypeEnum = null;
        if(fleetItemTypeEnum == FleetItemTypeEnum.DRIVER){
            dataTracerOperateTypeEnum = FleetDataTracerOperateTypeEnum.ADD_DRIVER;
            List<DriverEntity> driverEntityList = driverDao.selectBatchIds(addForm.getItemIdList());
            itemName = driverEntityList.stream().map(DriverEntity :: getDriverName).collect(Collectors.joining(","));
        }
        if(fleetItemTypeEnum == FleetItemTypeEnum.VEHICLE){
            dataTracerOperateTypeEnum = FleetDataTracerOperateTypeEnum.ADD_VEHICLE;
            List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(addForm.getItemIdList());
            itemName = vehicleEntityList.stream().map(VehicleEntity :: getVehicleNumber).collect(Collectors.joining(","));
        }
        if(dataTracerOperateTypeEnum == null){
            return;
        }
        String content = dataTracerOperateTypeEnum.getDesc() + itemName;

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(addForm.getFleetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
        dataTracerForm.setOperateType(dataTracerOperateTypeEnum);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 添加银行日志
     * @param fleetBankEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void bankAddLog(FleetBankEntity fleetBankEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(fleetBankEntity.getFleetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
        dataTracerForm.setOperateType(FleetDataTracerOperateTypeEnum.ADD_BANK);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(FleetDataTracerOperateTypeEnum.ADD_BANK.getDesc(), fleetBankEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 批量修改负责人日志
     *
     * @param fleetIdList
     * @param employeeEntity
     * @param dataTracerRequestForm
     */
    public void batchUpdateManagerLog(List<Long> fleetIdList, EmployeeEntity employeeEntity, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(fleetIdList)) {
            return;
        }
        String content = "修改负责人为:" + employeeEntity.getActualName();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracket : fleetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracket);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.FLEET);
            dataTracerForm.setOperateType(FleetDataTracerOperateTypeEnum.UPDATE_MANAGER);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operateType);
    }
}