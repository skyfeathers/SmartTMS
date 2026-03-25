/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest } from '/@/lib/axios';

export const ocrApi = {

    // 识别身份证正面 - @author yandy
    recognizeIdCardFace: (url) => {
      return postRequest('/support/ocr/recognizeIdCardFace', { url });
    },

    // 识别身份证背面 - @author yandy
    recognizeIdCardBack: (url) => {
      return postRequest('/support/ocr/recognizeIdCardBack', { url });
    },

    // 识别营业执照 - @author yandy
    recognizeBusinessLicense: (url) => {
        return postRequest('/support/ocr/recognizeBusinessLicense', { url });
    },

    // 识别驾驶证 - @author yandy
    recognizeDrivingLicense: (url) => {
        return postRequest('/support/ocr/recognizeDrivingLicense', { url });
    },

    // 识别行驶证正本 - @author yandy
    recognizeVehicleLicenseFace: (url) => {
        return postRequest('/support/ocr/recognizeVehicleLicenseFace', { url });
    },

    // 识别行驶证副本 - @author yandy
    recognizeVehicleLicenseBack: (url) => {
      return postRequest('/support/ocr/recognizeVehicleLicenseBack', { url });
    },

    // 识别银行卡 - @author yandy
    recognizeBankCard: (url) => {
        return postRequest('/support/ocr/recognizeBankCard', { url });
    },
    // 道路运输证 - @author yandy
    recognizeIdRoadTransportCertificate: (url) => {
      return postRequest('/support/ocr/recognizeIdRoadTransportCertificate', { url });
    },
};
