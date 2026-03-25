package net.lab1024.tms.driver.module.business.bracket;

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
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
     * 获取挂车信息
     *
     * @param detailVO
     * @return
     */
    private String getBracketContent(BracketDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("挂车信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【品牌型号】").append(DataTracerConstant.SPLIT).append(detailVO.getType()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证正本】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicensePic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证副本】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseEctypePic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【实际所属人】").append(DataTracerConstant.SPLIT).append(detailVO.getOwner()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用性质】").append(DataTracerConstant.SPLIT).append(detailVO.getNature()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆识别代号】").append(DataTracerConstant.SPLIT).append(detailVO.getVin()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂车车牌号】").append(DataTracerConstant.SPLIT).append(detailVO.getBracketNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌颜色】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getPlateColorCode(), VehiclePlateColorEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【载重（吨）】").append(DataTracerConstant.SPLIT).append(detailVO.getTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【重量（吨）】").append(DataTracerConstant.SPLIT).append(detailVO.getWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【注册日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRegisterTime()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证日期】").append(DataTracerConstant.SPLIT).append(detailVO.getIssueTime()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

}