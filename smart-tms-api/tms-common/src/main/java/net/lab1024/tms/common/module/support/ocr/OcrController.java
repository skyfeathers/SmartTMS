package net.lab1024.tms.common.module.support.ocr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.ocr.domain.form.OrcForm;
import net.lab1024.tms.common.module.support.ocr.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 3:12 下午
 */
@Api(tags = {SwaggerTagConst.Support.SUPPORT_OCR})
@RestController
public class OcrController extends SupportBaseController {

    @Autowired
    private OcrService ocrService;

    @ApiOperation("识别身份证正面 - @author yandy")
    @PostMapping("/ocr/recognizeIdCardFace")
    public ResponseDTO<RecognizeIdCardFaceVO> recognizeIdCardFront(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeIdCardFace(urlForm.getUrl());
    }

    @ApiOperation("识别身份证背面 - @author yandy")
    @PostMapping("/ocr/recognizeIdCardBack")
    public ResponseDTO<RecognizeIdCardBackVO> recognizeIdCardBack(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeIdCardBack(urlForm.getUrl());
    }

    @ApiOperation("识别营业执照 - @author yandy")
    @PostMapping("/ocr/recognizeBusinessLicense")
    public ResponseDTO<RecognizeBusinessLicenseVO> recognizeBusinessLicense(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeBusinessLicense(urlForm.getUrl());
    }

    @ApiOperation("识别驾驶证 - @author yandy")
    @PostMapping("/ocr/recognizeDrivingLicense")
    public ResponseDTO<RecognizeDrivingLicenseVO> recognizeDrivingLicense(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeDrivingLicense(urlForm.getUrl());
    }

    @ApiOperation("识别行驶证正本 - @author yandy")
    @PostMapping("/ocr/recognizeVehicleLicenseFace")
    public ResponseDTO<RecognizeVehicleLicenseFaceVO> recognizeVehicleLicenseFront(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeVehicleLicenseFront(urlForm.getUrl());
    }

    @ApiOperation("识别行驶证副本 - @author yandy")
    @PostMapping("/ocr/recognizeVehicleLicenseBack")
    public ResponseDTO<RecognizeVehicleLicenseBackVO> recognizeVehicleLicenseBack(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeVehicleLicenseBack(urlForm.getUrl());
    }

    @ApiOperation("识别银行卡 - @author yandy")
    @PostMapping("/ocr/recognizeBankCard")
    public ResponseDTO<RecognizeBankCardVO> recognizeBankCard(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeBankCard(urlForm.getUrl());
    }

    @ApiOperation("识别道路运输证 - @author yandy")
    @PostMapping("/ocr/recognizeIdRoadTransportCertificate")
    public ResponseDTO<RecognizeIdRoadTransportCertificateVO> recognizeIdRoadTransportCertificate(@RequestBody OrcForm urlForm) {
        return ocrService.recognizeIdRoadTransportCertificate(urlForm.getUrl());
    }
}