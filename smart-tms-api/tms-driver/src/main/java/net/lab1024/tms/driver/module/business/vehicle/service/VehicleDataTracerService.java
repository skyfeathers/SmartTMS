package net.lab1024.tms.driver.module.business.vehicle.service;

import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
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
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.driver.module.business.vehicle.domain.vo.VehicleDetailVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class VehicleDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    @Resource
    private DictCacheService dictCacheService;

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(VehicleDetailVO beforeData, VehicleDetailVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getVehicleContent(beforeData), this.getVehicleContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(VehicleDetailVO.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 保存日志
     *
     * @param vehicleId
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long vehicleId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
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
    private String getVehicleContent(VehicleDetailVO detailVO) {
        //基本信息
        String baseContent = this.getBaseContent(detailVO);
        //行驶证信息
        String drivingLicenseContent = this.getDrivingLicenseContent(detailVO);
        //道路运输证
        String roadTransportCertificateContent = this.getRoadTransportCertificateContent(detailVO);
        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(drivingLicenseContent)
                .append(roadTransportCertificateContent);
        return builder.toString();
    }

    private String getBaseContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆审验到期时间】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleAuditExpireDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【绑定挂车】").append(DataTracerConstant.SPLIT).append(detailVO.getBracketNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【品牌型号】").append(DataTracerConstant.SPLIT).append(detailVO.getModel()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发动机号】").append(DataTracerConstant.SPLIT).append(detailVO.getEngineNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【机动车登记证书编号】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleRegistrationCertificateNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(detailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getDrivingLicenseContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("行驶证信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证正本-附件】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌号】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌颜色】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getVehiclePlateColorCode(), VehiclePlateColorEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆类型】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(detailVO.getVehicleType())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆识别代码（车架号）】").append(DataTracerConstant.SPLIT).append(detailVO.getVin()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证机关】").append(DataTracerConstant.SPLIT).append(detailVO.getIssuingOrganizations()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【注册日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRegisterDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证日期】").append(DataTracerConstant.SPLIT).append(detailVO.getIssueDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证副本附件】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseEctypeAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【总质量】").append(DataTracerConstant.SPLIT).append(detailVO.getGrossMass()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核定载质量（载重）】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核定载质量（载重）】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【整备质量】").append(DataTracerConstant.SPLIT).append(detailVO.getCurbWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【牵引总质量）】").append(DataTracerConstant.SPLIT).append(detailVO.getTractionWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【外廓尺寸】").append(DataTracerConstant.SPLIT).append(detailVO.getGabarite()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【能源类型】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(detailVO.getVehicleEnergyType())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用性质】").append(DataTracerConstant.SPLIT).append(detailVO.getNature()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getRoadTransportCertificateContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("道路运输证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输证-附件】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输证号】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输经营许可证号】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportBusinessLicenseNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道运证有效期开始日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道运证有效期结束日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateExpireDate()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

}
