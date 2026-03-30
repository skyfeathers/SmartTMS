package net.lab1024.tms.admin.module.business.bracket;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketDetailVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.constant.BracketDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
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

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class BracketDataTracerService {


    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新增日志
     *
     * @param bracketId
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long bracketId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(bracketId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.BRACKET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(BracketDetailVO beforeData, BracketDetailVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getBracketId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.BRACKET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getBracketContent(beforeData), this.getBracketContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(BracketDetailVO.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 获取车辆信息
     *
     * @param detailVO
     * @return
     */
    private String getBracketContent(BracketDetailVO detailVO) {
        //基本信息
        String baseContent = this.getBaseContent(detailVO);
        //证件信息
        String licenseContent = this.getLicenseContent(detailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(licenseContent);
        return builder.toString();
    }

    private String getLicenseContent(BracketDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("证件信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证正本】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicensePic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证副本】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseEctypePic()).append(DataTracerConstant.LINE)
//                .append(DataTracerConstant.TAB).append("【所属人性质】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getOwnerType(), OwnerTypeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【实际所属人】").append(DataTracerConstant.SPLIT).append(detailVO.getOwner()).append(DataTracerConstant.LINE)
//                .append(DataTracerConstant.TAB).append("【住址】").append(DataTracerConstant.SPLIT).append(detailVO.getAddress()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用性质】").append(DataTracerConstant.SPLIT).append(detailVO.getNature()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆识别代号】").append(DataTracerConstant.SPLIT).append(detailVO.getVin()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂车车牌号】").append(DataTracerConstant.SPLIT).append(detailVO.getBracketNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌颜色】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getPlateColorCode(), VehiclePlateColorEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【载重（kg）】").append(DataTracerConstant.SPLIT).append(detailVO.getTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【重量（kg）】").append(DataTracerConstant.SPLIT).append(detailVO.getWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【注册日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRegisterTime()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证日期】").append(DataTracerConstant.SPLIT).append(detailVO.getIssueTime()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getBaseContent(BracketDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getBusinessMode(), BusinessModeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【关联企业】").append(DataTracerConstant.SPLIT).append(detailVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【负责人】").append(DataTracerConstant.SPLIT).append(detailVO.getManagerName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【速记码】").append(DataTracerConstant.SPLIT).append(detailVO.getShorthandCode()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【品牌型号】").append(DataTracerConstant.SPLIT).append(detailVO.getType()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量删除日志
     *
     * @param bracketIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchDeleteLog(List<Long> bracketIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(bracketIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracketId : bracketIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracketId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.BRACKET);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 批量审核日志
     *
     * @param bracketIdList
     * @param auditStatus
     * @param auditRemark
     * @param dataTracerRequestForm
     */
    public void batchAuditLog(List<Long> bracketIdList, Integer auditStatus, String auditRemark, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(bracketIdList)) {
            return;
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        String content = "修改审核状态为：" + auditStatusEnum.getDesc() + "；备注为：" + (StringUtils.isEmpty(auditRemark) ? "" : auditRemark);
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracket : bracketIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracket);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.BRACKET);
            dataTracerForm.setOperateType(BracketDataTracerOperateTypeEnum.AUDIT);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 批量修改负责人日志
     *
     * @param bracketIdList
     * @param employeeEntity
     * @param dataTracerRequestForm
     */
    public void batchUpdateManagerLog(List<Long> bracketIdList, EmployeeEntity employeeEntity, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(bracketIdList)) {
            return;
        }
        String content = "修改负责人为:" + employeeEntity.getActualName();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracket : bracketIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracket);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.BRACKET);
            dataTracerForm.setOperateType(BracketDataTracerOperateTypeEnum.UPDATE_MANAGER);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}