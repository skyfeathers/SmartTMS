package net.lab1024.tms.common.module.support.ocr;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;
import net.lab1024.tms.common.module.support.ocr.domain.bo.*;
import net.lab1024.tms.common.module.support.ocr.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:08 下午
 */
@Slf4j
@Service
public class OcrService {

    @Autowired
    private OcrRequestService ocrRequestService;
    @Autowired
    private OcrBaiduService ocrBaiduService;

    /**
     * 身份证正面
     *
     * @param idCardUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeIdCardFaceVO> recognizeIdCardFace(String idCardUrl) {
        RecognizeIdCardFaceBO recognizeIdCardFaceBO = null;
        try {
            recognizeIdCardFaceBO = ocrRequestService.recognizeIdCardFace(idCardUrl);
        } catch (Exception e) {
            log.error("身份证识别失败:{}", e);
        }
        if (recognizeIdCardFaceBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "身份证识别失败");
        }
        RecognizeIdCardFaceVO recognizeIdCardVO = SmartBeanUtil.copy(recognizeIdCardFaceBO, RecognizeIdCardFaceVO.class);
        // 时间格式化
        DateTime birthDate = IdcardUtil.getBirthDate(recognizeIdCardFaceBO.getIdNumber());
        LocalDate birthLocalDate = birthDate.toLocalDateTime().toLocalDate();
        recognizeIdCardVO.setBirthDate(birthLocalDate);

        return ResponseDTO.ok(recognizeIdCardVO);
    }

    /**
     * 身份证背面
     *
     * @param idCardUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeIdCardBackVO> recognizeIdCardBack(String idCardUrl) {
        RecognizeIdCardBackBO recognizeIdCardBackBO = null;
        try {
            recognizeIdCardBackBO = ocrRequestService.recognizeIdCardBack(idCardUrl);
        } catch (Exception e) {
            log.error("身份证识别失败:{}", e);
        }
        if (recognizeIdCardBackBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "身份证识别失败");
        }
        RecognizeIdCardBackVO recognizeIdCardVO = SmartBeanUtil.copy(recognizeIdCardBackBO, RecognizeIdCardBackVO.class);
        // validPeriod格式为： 2012.01.18-2022.01.18
        String validPeriod = recognizeIdCardBackBO.getValidPeriod();
        List<String> validPeriodList = SmartStringUtil.splitConvertToList(validPeriod, "-");
        if (CollectionUtils.isEmpty(validPeriodList)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "身份证识别失败");
        }
        // 时间格式化
        LocalDate validPeriodStartDate = null;
        try {
            validPeriodStartDate = SmartLocalDateUtil.parseSpecialYMD(validPeriodList.get(0).replace(".", ""));
        } catch (Exception e) {
            log.error("身份证背面识别结果：{},有效期开始时间识别错误:{}", JSON.toJSONString(recognizeIdCardBackBO), e);
        }
        recognizeIdCardVO.setValidPeriodStartDate(validPeriodStartDate);

        Boolean idCardEndlessFlag = false;
        LocalDate validPeriodEndTime = null;
        String endDate = validPeriodList.get(1).replace(".", "");
        if ("长期".equals(endDate)) {
            idCardEndlessFlag = true;
        } else {
            idCardEndlessFlag = false;
            validPeriodEndTime = null;
            try {
                validPeriodEndTime = SmartLocalDateUtil.parseSpecialYMD(endDate);
            } catch (Exception e) {
                log.error("身份证背面识别结果：{},有效期截止时间识别错误:{}", JSON.toJSONString(recognizeIdCardBackBO), e);
            }
        }
        recognizeIdCardVO.setValidPeriodEndDate(validPeriodEndTime);
        recognizeIdCardVO.setIdCardEndlessFlag(idCardEndlessFlag);
        return ResponseDTO.ok(recognizeIdCardVO);
    }

    /**
     * 营业执照
     *
     * @param businessLicenseUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeBusinessLicenseVO> recognizeBusinessLicense(String businessLicenseUrl) {
        RecognizeBusinessLicenseBO recognizeBusinessLicenseBO = null;
        try {
            recognizeBusinessLicenseBO = ocrRequestService.recognizeBusinessLicense(businessLicenseUrl);
        } catch (Exception e) {
            log.error("营业执照识别失败:{}", e);
        }
        if (recognizeBusinessLicenseBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "营业执照识别失败");
        }
        RecognizeBusinessLicenseVO recognizeBusinessLicenseVO = SmartBeanUtil.copy(recognizeBusinessLicenseBO, RecognizeBusinessLicenseVO.class);
        // 注册日期 时间格式化
        try {
            String registrationDate = recognizeBusinessLicenseBO.getRegistrationDate();
            if (StringUtils.isNotBlank(registrationDate)) {
                LocalDate registrationLocalDate = SmartLocalDateUtil.parseChineseYMD(registrationDate);
                recognizeBusinessLicenseVO.setRegistrationDate(registrationLocalDate);
            }
            // 有效期 开始日期 时间格式化
            String validFromDate = recognizeBusinessLicenseBO.getValidFromDate();
            if (StringUtils.isNotBlank(registrationDate)) {
                LocalDate validFromLocalDate = SmartLocalDateUtil.parseSpecialYMD(validFromDate);
                recognizeBusinessLicenseVO.setValidFromDate(validFromLocalDate);
            }
            // 有效期 截止日期 时间格式化
            String validToDate = recognizeBusinessLicenseBO.getValidToDate();
            if (StringUtils.isNotBlank(registrationDate)) {
                LocalDate validToLocalDate = SmartLocalDateUtil.parseSpecialYMD(validToDate);
                recognizeBusinessLicenseVO.setValidToDate(validToLocalDate);
            }
        } catch (Exception e) {
            log.error("营业执照识别结果：{},日期识别错误:{}", JSON.toJSONString(recognizeBusinessLicenseBO), e);
        }
        return ResponseDTO.ok(recognizeBusinessLicenseVO);
    }

    /**
     * 驾驶证
     *
     * @param drivingLicenseUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeDrivingLicenseVO> recognizeDrivingLicense(String drivingLicenseUrl) {
        RecognizeDrivingLicenseBO recognizeDrivingLicenseBO = null;
        try {
            recognizeDrivingLicenseBO = ocrRequestService.recognizeDrivingLicense(drivingLicenseUrl);
        } catch (Exception e) {
            log.error("驾驶证识别失败:{}", e);
        }
        if (recognizeDrivingLicenseBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "驾驶证识别失败");
        }
        RecognizeDrivingLicenseVO recognizeBusinessLicenseVO = SmartBeanUtil.copy(recognizeDrivingLicenseBO, RecognizeDrivingLicenseVO.class);
        try {
            // 出生日期 时间格式化
            String birthDate = recognizeDrivingLicenseBO.getBirthDate();
            if (StringUtils.isNotBlank(birthDate)) {
                LocalDate birthLocalDate = SmartLocalDateUtil.parseYMD(birthDate);
                recognizeBusinessLicenseVO.setBirthDate(birthLocalDate);
            }
            // 有效期 开始日期 时间格式化
            String validFromDate = recognizeDrivingLicenseBO.getValidFromDate();
            if (StringUtils.isNotBlank(validFromDate)) {
                LocalDate validFromLocalDate = SmartLocalDateUtil.parseYMD(validFromDate);
                recognizeBusinessLicenseVO.setValidFromDate(validFromLocalDate);
            }
            // 有效期 截止日期 时间格式化
            String validPeriod = recognizeDrivingLicenseBO.getValidPeriod();
            if (StringUtils.isNotBlank(validPeriod)) {
                String[] validDateArray = validPeriod.split("至");
                if (validDateArray.length == 2) {
                    String validToDate = validDateArray[1];
                    LocalDate validToLocalDate = SmartLocalDateUtil.parseYMD(validToDate);
                    recognizeBusinessLicenseVO.setValidToDate(validToLocalDate);
                }
            }
        } catch (Exception e) {
            log.error("驾驶证识别结果：{},日期识别错误:{}", JSON.toJSONString(recognizeDrivingLicenseBO), e);
        }
        VehicleClassEnum vehicleClassEnum = SmartBaseEnumUtil.getEnumByDesc(recognizeDrivingLicenseBO.getApprovedType(), VehicleClassEnum.class);
        if(null != vehicleClassEnum){
            recognizeBusinessLicenseVO.setApprovedType(vehicleClassEnum.getValue());
        }
        return ResponseDTO.ok(recognizeBusinessLicenseVO);
    }


    /**
     * 行驶证
     *
     * @param vehicleLicenseUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeVehicleLicenseFaceVO> recognizeVehicleLicenseFront(String vehicleLicenseUrl) {
        RecognizeVehicleLicenseFaceBO recognizeVehicleLicenseFaceBO = null;
        try {
            recognizeVehicleLicenseFaceBO = ocrRequestService.recognizeVehicleLicenseFront(vehicleLicenseUrl);
        } catch (Exception e) {
            log.error("行驶证识别失败:{}", e);
        }
        if (recognizeVehicleLicenseFaceBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "行驶证识别失败");
        }
        RecognizeVehicleLicenseFaceVO recognizeBusinessLicenseVO = SmartBeanUtil.copy(recognizeVehicleLicenseFaceBO, RecognizeVehicleLicenseFaceVO.class);
        // 发证日期 时间格式化
        String issueDate = recognizeVehicleLicenseFaceBO.getIssueDate();
        if (StringUtils.isNotBlank(issueDate)) {
            try {
                LocalDate issueLocalDate = SmartLocalDateUtil.parseYMD(issueDate);
                recognizeBusinessLicenseVO.setIssueDate(issueLocalDate);
            } catch (Exception e) {
                log.error("行驶证正面识别结果：{},发证日期识别错误:{}", JSON.toJSONString(recognizeVehicleLicenseFaceBO), e);
            }
        }
        // 注册日期 时间格式化
        String registrationDate = recognizeVehicleLicenseFaceBO.getRegistrationDate();
        if (StringUtils.isNotBlank(registrationDate)) {
            try {
                LocalDate registrationLocalDate = SmartLocalDateUtil.parseYMD(registrationDate);
                recognizeBusinessLicenseVO.setRegistrationDate(registrationLocalDate);
            } catch (Exception e) {
                log.error("行驶证正面识别结果：{},注册日期识别错误:{}", JSON.toJSONString(recognizeVehicleLicenseFaceBO), e);
            }
        }
        return ResponseDTO.ok(recognizeBusinessLicenseVO);
    }

    /**
     * 行驶证
     *
     * @param vehicleLicenseUrl
     * @return
     * @throws Exception
     */
    public ResponseDTO<RecognizeVehicleLicenseBackVO> recognizeVehicleLicenseBack(String vehicleLicenseUrl) {
        RecognizeVehicleLicenseBackBO recognizeVehicleLicenseBackBO = null;
        try {
            recognizeVehicleLicenseBackBO = ocrRequestService.recognizeVehicleLicenseBack(vehicleLicenseUrl);
        } catch (Exception e) {
            log.error("行驶证识别失败:{}", e);
        }
        if (recognizeVehicleLicenseBackBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "行驶证识别失败");
        }
        RecognizeVehicleLicenseBackVO recognizeBusinessLicenseBackVO = SmartBeanUtil.copy(recognizeVehicleLicenseBackBO, RecognizeVehicleLicenseBackVO.class);
        return ResponseDTO.ok(recognizeBusinessLicenseBackVO);
    }


    /**
     * 银行卡识别
     * @param url
     * @return
     */
    public ResponseDTO<RecognizeBankCardVO> recognizeBankCard(String url) {
        RecognizeBankCardBO recognizeBankCardBO = null;
        try {
            recognizeBankCardBO = ocrRequestService.recognizeBankCard(url);
        } catch (Exception e) {
            log.error("银行卡识别失败:{}", e);
        }
        if (recognizeBankCardBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "行驶证识别失败");
        }
        RecognizeBankCardVO recognizeBankCardVO = SmartBeanUtil.copy(recognizeBankCardBO, RecognizeBankCardVO.class);
        return ResponseDTO.ok(recognizeBankCardVO);
    }


    /**
     * 道路运输证
     *
     * @param url
     * @return
     */
    public ResponseDTO<RecognizeIdRoadTransportCertificateVO> recognizeIdRoadTransportCertificate(String url) {
        RecognizeIdRoadTransportCertificateBO roadTransportCertificateBO = ocrBaiduService.recognizeIdRoadTransportCertificate(url);
        if (roadTransportCertificateBO == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "道路运输证识别失败");
        }
        String roadTransportBusinessLicenseNumber = roadTransportCertificateBO.getRoadTransportBusinessLicenseNumber();
        //只保留数字
        roadTransportBusinessLicenseNumber = this.getNumberString(roadTransportBusinessLicenseNumber);

        RecognizeIdRoadTransportCertificateVO roadTransportCertificateVO = new RecognizeIdRoadTransportCertificateVO();
        roadTransportCertificateVO.setRoadTransportBusinessLicenseNumber(roadTransportBusinessLicenseNumber);
        return ResponseDTO.ok(roadTransportCertificateVO);
    }

    private String getNumberString(String str){
        String regexNumber = "[0-9]";
        if(StringUtils.isBlank(str)){
            return null;
        }
        StringBuffer numberStringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile(regexNumber);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String group = matcher.group();
            numberStringBuffer.append(group);
        }
        return  numberStringBuffer.toString();
    }
}

