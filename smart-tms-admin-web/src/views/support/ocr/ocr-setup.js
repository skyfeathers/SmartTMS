/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 17:38:06
 * @LastEditors:
 * @LastEditTime: 2022-07-13 17:38:06
 */
import {ocrApi} from "/@/api/support/ocr/ocr-api";
import {useSpinStore} from "/@/store/modules/system/spin";

export function ocrSetup() {

    // 识别身份证正面
    async function recognizeIdCardFace(url, cb) {
        try {
            useSpinStore().show();
            let responseModel = await ocrApi.recognizeIdCardFace(url);
            cb(responseModel.data);
        } catch (e) {
            console.log(e);
        } finally {
            useSpinStore().hide();
        }
    }

    // 识别身份证背面
    async function recognizeIdCardBack(url, cb) {
      try {
        useSpinStore().show();
        let responseModel = await ocrApi.recognizeIdCardBack(url);
        cb(responseModel.data);
      } catch (e) {
        console.log(e);
      } finally {
        useSpinStore().hide();
      }
    }

    // 识别营业执照
    async function recognizeBusinessLicense(url, cb) {
        try {
            useSpinStore().show();
            let responseModel = await ocrApi.recognizeBusinessLicense(url);
            cb(responseModel.data);
        } catch (e) {
            console.log(e);
        } finally {
            useSpinStore().hide();
        }
    }

    // 识别驾驶证
    async function recognizeDrivingLicense(url, cb) {
        try {
            useSpinStore().show();
            let responseModel = await ocrApi.recognizeDrivingLicense(url);
            cb(responseModel.data);
        } catch (e) {
            console.log(e);
        } finally {
            useSpinStore().hide();
        }
    }

    // 识别行驶证正本
    async function recognizeVehicleLicenseFace(url, cb) {
        try {
            useSpinStore().show();
            let responseModel = await ocrApi.recognizeVehicleLicenseFace(url);
            cb(responseModel.data);
        } catch (e) {
            console.log(e);
        } finally {
            useSpinStore().hide();
        }
    }

  // 识别行驶证
  async function recognizeVehicleLicenseBack(url, cb) {
    try {
      useSpinStore().show();
      let responseModel = await ocrApi.recognizeVehicleLicenseBack(url);
      cb(responseModel.data);
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

    // 识别行驶证
    async function recognizeBankCard(url, cb, extParam) {
        try {
            useSpinStore().show();
            let responseModel = await ocrApi.recognizeBankCard(url);
            cb(responseModel.data, extParam);
        } catch (e) {
            console.log(e);
        } finally {
            useSpinStore().hide();
        }
    }

  // 道路运输证
  async function recognizeRoadTransportCertificate(url, cb, extParam) {
    try {
      useSpinStore().show();
      let responseModel = await ocrApi.recognizeIdRoadTransportCertificate(url);
      cb(responseModel.data, extParam);
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }


    return {
        recognizeIdCardFace,
        recognizeIdCardBack,
        recognizeBusinessLicense,
        recognizeDrivingLicense,
        recognizeVehicleLicenseFace,
        recognizeVehicleLicenseBack,
        recognizeBankCard,
        recognizeRoadTransportCertificate
    }
}
