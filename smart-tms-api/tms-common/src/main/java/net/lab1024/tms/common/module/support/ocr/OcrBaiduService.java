package net.lab1024.tms.common.module.support.ocr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.lab1024.tms.common.module.support.ocr.constant.OcrBaiduConstant;
import net.lab1024.tms.common.module.support.ocr.domain.bo.RecognizeIdRoadTransportCertificateBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author yandy
 * @description:
 * @date 2022/12/3 2:10 下午
 */
@Service
public class OcrBaiduService {

    @Autowired
    private OcrBaiduRequestService ocrBaiduRequestService;

    public RecognizeIdRoadTransportCertificateBO recognizeIdRoadTransportCertificate(String imageUrl) {
        JSONObject result = ocrBaiduRequestService.requestOcr(OcrBaiduConstant.ROAD_TRANSPORT_CERTIFICATE, imageUrl);
        JSONObject wordsResult = result.getJSONObject("words_result");
        if (wordsResult == null) {
            return null;
        }
        RecognizeIdRoadTransportCertificateBO roadTransportCertificateBO = new RecognizeIdRoadTransportCertificateBO();
        roadTransportCertificateBO.setVehicleNumber(this.getArrayValue("车辆号牌", wordsResult));
        roadTransportCertificateBO.setEconomicType(this.getArrayValue("经济类型", wordsResult));
        roadTransportCertificateBO.setBusinessScope(this.getArrayValue("经营范围", wordsResult));
        roadTransportCertificateBO.setVehicleType(this.getArrayValue("车辆类型", wordsResult));
        roadTransportCertificateBO.setTonSeat(this.getArrayValue("吨座位", wordsResult));
        roadTransportCertificateBO.setRemark(this.getArrayValue("备注", wordsResult));
        roadTransportCertificateBO.setRoadTransportBusinessLicenseNumber(this.getArrayValue("经营许可证", wordsResult));
        roadTransportCertificateBO.setVehicleHeight(this.getArrayValue("车辆毫米_高", wordsResult));
        roadTransportCertificateBO.setVehicleWidth(this.getArrayValue("车辆毫米_宽", wordsResult));
        roadTransportCertificateBO.setVehicleLength(this.getArrayValue("车辆毫米_长", wordsResult));
        roadTransportCertificateBO.setIssueDate(this.getArrayValue("发证日期", wordsResult));
        roadTransportCertificateBO.setAddress(this.getArrayValue("地址", wordsResult));
        roadTransportCertificateBO.setEnterpriseName(this.getArrayValue("业户名称", wordsResult));
        roadTransportCertificateBO.setInitialIssueDate(this.getArrayValue("初领日期", wordsResult));
        return roadTransportCertificateBO;
    }

    private String getArrayValue(String key, JSONObject wordsResult) {
        JSONArray jsonArray = wordsResult.getJSONArray(key);
        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }
        JSONObject first = jsonArray.getJSONObject(0);
        return first.getString("word");
    }


}