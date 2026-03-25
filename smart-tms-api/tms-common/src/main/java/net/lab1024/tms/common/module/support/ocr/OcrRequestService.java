package net.lab1024.tms.common.module.support.ocr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.*;
import net.lab1024.tms.common.module.support.ocr.domain.bo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:08 下午
 */
@Service
public class OcrRequestService {

    @Autowired
    private Client client;

    /**
     * 身份证正面
     * @param idCardUrl
     * @return
     * @throws Exception
     */
    public RecognizeIdCardFaceBO recognizeIdCardFace(String idCardUrl) throws Exception {
        RecognizeIdcardRequest recognizeIdcardRequest = new RecognizeIdcardRequest();
        recognizeIdcardRequest.setUrl(idCardUrl);

        RecognizeIdcardResponse recognizeIdcardResponse = client.recognizeIdcard(recognizeIdcardRequest);
        RecognizeIdcardResponseBody responseBody = recognizeIdcardResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject faceObject = dataObject.getJSONObject("face");
        if(faceObject == null){
            return null;
        }
        RecognizeIdCardFaceBO recognizeIdCardFaceBO = faceObject.getObject("data", RecognizeIdCardFaceBO.class);
        return recognizeIdCardFaceBO;
    }

    /**
     * 身份证背面
     * @param idCardUrl
     * @return
     * @throws Exception
     */
    public RecognizeIdCardBackBO recognizeIdCardBack(String idCardUrl) throws Exception {
        RecognizeIdcardRequest recognizeIdcardRequest = new RecognizeIdcardRequest();
        recognizeIdcardRequest.setUrl(idCardUrl);

        RecognizeIdcardResponse recognizeIdcardResponse = client.recognizeIdcard(recognizeIdcardRequest);
        RecognizeIdcardResponseBody responseBody = recognizeIdcardResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject faceObject = dataObject.getJSONObject("back");
        if(faceObject == null){
            return null;
        }
        RecognizeIdCardBackBO recognizeIdCardBackBO = faceObject.getObject("data", RecognizeIdCardBackBO.class);
        return recognizeIdCardBackBO;
    }

    /**
     * 营业执照
     * @param businessLicenseUrl
     * @return
     * @throws Exception
     */
    public RecognizeBusinessLicenseBO recognizeBusinessLicense(String businessLicenseUrl) throws Exception {
        RecognizeBusinessLicenseRequest recognizeBusinessLicenseRequest = new RecognizeBusinessLicenseRequest();
        recognizeBusinessLicenseRequest.setUrl(businessLicenseUrl);

        RecognizeBusinessLicenseResponse recognizeBusinessLicenseResponse = client.recognizeBusinessLicense(recognizeBusinessLicenseRequest);
        RecognizeBusinessLicenseResponseBody responseBody = recognizeBusinessLicenseResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        RecognizeBusinessLicenseBO recognizeIdCardBO = jsonObject.getObject("data", RecognizeBusinessLicenseBO.class);
        return recognizeIdCardBO;
    }

    /**
     * 驾驶证
     * @param drivingLicenseUrl
     * @return
     * @throws Exception
     */
    public RecognizeDrivingLicenseBO recognizeDrivingLicense(String drivingLicenseUrl) throws Exception {
        RecognizeDrivingLicenseRequest recognizeDrivingLicenseRequest = new RecognizeDrivingLicenseRequest();
        recognizeDrivingLicenseRequest.setUrl(drivingLicenseUrl);

        RecognizeDrivingLicenseResponse recognizeDrivingLicenseResponse = client.recognizeDrivingLicense(recognizeDrivingLicenseRequest);
        RecognizeDrivingLicenseResponseBody responseBody = recognizeDrivingLicenseResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject faceObject = dataObject.getJSONObject("face");
        if(faceObject == null){
            return null;
        }
        RecognizeDrivingLicenseBO recognizeDrivingLicenseBO = faceObject.getObject("data", RecognizeDrivingLicenseBO.class);
        return recognizeDrivingLicenseBO;
    }


    /**
     * 行驶证
     * @param vehicleLicenseUrl
     * @return
     * @throws Exception
     */
    public RecognizeVehicleLicenseFaceBO recognizeVehicleLicenseFront(String vehicleLicenseUrl) throws Exception {
        RecognizeVehicleLicenseRequest recognizeVehicleLicenseRequest = new RecognizeVehicleLicenseRequest();
        recognizeVehicleLicenseRequest.setUrl(vehicleLicenseUrl);

        RecognizeVehicleLicenseResponse recognizeVehicleLicenseResponse = client.recognizeVehicleLicense(recognizeVehicleLicenseRequest);
        RecognizeVehicleLicenseResponseBody responseBody = recognizeVehicleLicenseResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject faceObject = dataObject.getJSONObject("face");
        if(faceObject == null){
            return null;
        }
        RecognizeVehicleLicenseFaceBO recognizeVehicleLicenseFaceBO = faceObject.getObject("data", RecognizeVehicleLicenseFaceBO.class);
        return recognizeVehicleLicenseFaceBO;
    }

    /**
     * 行驶证
     * @param vehicleLicenseUrl
     * @return
     * @throws Exception
     */
    public RecognizeVehicleLicenseBackBO recognizeVehicleLicenseBack(String vehicleLicenseUrl) throws Exception {
        RecognizeVehicleLicenseRequest recognizeVehicleLicenseRequest = new RecognizeVehicleLicenseRequest();
        recognizeVehicleLicenseRequest.setUrl(vehicleLicenseUrl);

        RecognizeVehicleLicenseResponse recognizeVehicleLicenseResponse = client.recognizeVehicleLicense(recognizeVehicleLicenseRequest);
        RecognizeVehicleLicenseResponseBody responseBody = recognizeVehicleLicenseResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject backObject = dataObject.getJSONObject("back");
        if(backObject == null){
            return null;
        }
        RecognizeVehicleLicenseBackBO recognizeVehicleLicenseBackBO = backObject.getObject("data", RecognizeVehicleLicenseBackBO.class);
        return recognizeVehicleLicenseBackBO;
    }


    /**
     * 银行卡
     * @param bankCardUrl
     * @return
     * @throws Exception
     */
    public RecognizeBankCardBO recognizeBankCard(String bankCardUrl) throws Exception {
        RecognizeBankCardRequest recognizeBankCardRequest = new RecognizeBankCardRequest();
        recognizeBankCardRequest.setUrl(bankCardUrl);

        RecognizeBankCardResponse recognizeBankCardResponse = client.recognizeBankCard(recognizeBankCardRequest);
        RecognizeBankCardResponseBody responseBody = recognizeBankCardResponse.getBody();
        String data = responseBody.getData();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(data);
        RecognizeBankCardBO recognizeBankCardBO = jsonObject.getObject("data", RecognizeBankCardBO.class);
        return recognizeBankCardBO;
    }
}

