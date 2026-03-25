package net.lab1024.tms.common.module.business.esign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.esign.domain.base.ESignUploadResponseDTO;
import net.lab1024.tms.common.module.business.esign.domain.file.ESignFileDetailVO;
import net.lab1024.tms.common.module.business.esign.domain.file.ESignFileStatusVO;
import net.lab1024.tms.common.module.business.esign.domain.file.ESignFileUploadForm;
import net.lab1024.tms.common.module.business.esign.domain.file.ESignFileUploadVO;
import net.lab1024.tms.common.module.business.esign.domain.flow.*;
import net.lab1024.tms.common.module.business.esign.domain.position.ESignCoordinateVO;
import net.lab1024.tms.common.module.business.esign.domain.position.ESignPositionInfoVO;
import net.lab1024.tms.common.module.business.esign.domain.position.ESignSearchPositionVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * e签宝接口
 *
 * @author lihaifan
 * @date 2022/7/14 15:42
 */
@Service
public class ESignService {

    @Autowired
    private ESignRequestService requestService;

    @Value("${esign.noticeDeveloperUrl}")
    private String noticeDeveloperUrl;

    /**
     * 文件上传
     *
     * @param file
     */
    public String uploadFile(File file, Long enterpriseId) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        // 获取文件MD5-二进制数组（128位）
        String fileMD5Bytes1282 = requestService.getFileMD5Bytes1282(file);
        ESignFileUploadForm eSignFileUploadForm = new ESignFileUploadForm();
        eSignFileUploadForm.setContentMd5(fileMD5Bytes1282);
        eSignFileUploadForm.setContentType(MediaType.APPLICATION_PDF_VALUE);
        eSignFileUploadForm.setFileName(file.getName());
        eSignFileUploadForm.setFileSize(file.length());
        eSignFileUploadForm.setConvert2Pdf(false);
        ESignFileUploadVO uploadVO = requestService.post(eSignFileUploadForm, ESignApiConst.GET_UPLOAD_URL, ESignFileUploadVO.class, enterpriseId);
        String uploadUrl = uploadVO.getUploadUrl();
        if (StringUtils.isBlank(uploadUrl)) {
            throw new BusinessException("e签宝文件上传失败，上传地址为空");
        }
        // 文件上传
        ESignUploadResponseDTO upload = requestService.upload(file, uploadVO.getUploadUrl(), fileMD5Bytes1282);
        if (!upload.getErrCode().equals(0)) {
            throw new BusinessException("e签宝文件上传失败，" + upload.getMsg());
        }
        return uploadVO.getFileId();
    }

    /**
     * 查询文件上传状态
     * 文件流上传成功仅代表文件上传到OSS服务，OSS服务将异步通知e签宝变更文件状态。
     * 受网络等各类因素影响，异步通知可能存在一定时差，建议开发者先调用【查询文件上传状态】接口查询文件当前状态，如果文件状态是2或5时，再发起签署流程。
     *
     * @param fileId
     */
    public ESignFileStatusVO getFileStatus(String fileId, Long enterpriseId) {
        String api = String.format(ESignApiConst.GET_FILE_STATUS, fileId);
        return requestService.get(api, ESignFileStatusVO.class, enterpriseId);
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @return
     */
    public ESignFileDetailVO getFileDetail(String fileId, Long enterpriseId) {
        if (StringUtils.isBlank(fileId)) {
            return null;
        }
        String api = String.format(ESignApiConst.GET_FILE_DETAIL, fileId);
        return requestService.get(api, ESignFileDetailVO.class, enterpriseId);
    }

    /**
     * 搜索关键字坐标
     *
     * @param fileId
     * @param keyword
     */
    public List<ESignSearchPositionVO> searchWordsPosition(String fileId, String keyword, Long enterpriseId) {
        String api = String.format(ESignApiConst.SEARCH_WORDS_POSITION, fileId, keyword);
        TypeReference<List<ESignSearchPositionVO>> typeReference = new TypeReference<List<ESignSearchPositionVO>>() {
        };
        return requestService.get(api, typeReference, enterpriseId);
    }

    /**
     * 寻找签章坐标（左下角为原点） 默认寻找第一个
     *
     * @param fileId
     * @param keyword
     * @return
     */
    private ESignPosBeanForm getPosBean(String fileId, String keyword, Long enterpriseId) {
        List<ESignSearchPositionVO> eSignSearchPositionVOS = this.searchWordsPosition(fileId, keyword, enterpriseId);
        if (CollectionUtils.isEmpty(eSignSearchPositionVOS)) {
            throw new BusinessException("未在文档中找到关键字：" + keyword);
        }
        Optional<ESignSearchPositionVO> findKeyword = eSignSearchPositionVOS.stream().filter(e -> e.getKeyword().equals(keyword)).findFirst();
        if (!findKeyword.isPresent()) {
            throw new BusinessException("未在文档中找到关键字：" + keyword + "位置");
        }
        ESignSearchPositionVO eSignSearchPositionVO = findKeyword.get();
        List<ESignPositionInfoVO> positionList = eSignSearchPositionVO.getPositionList();
        if (CollectionUtils.isEmpty(positionList)) {
            throw new BusinessException("未在文档中找到关键字：" + keyword + "位置");
        }
        ESignPositionInfoVO eSignPositionInfoVO = positionList.get(0);
        List<ESignCoordinateVO> coordinateList = eSignPositionInfoVO.getCoordinateList();
        if (CollectionUtils.isEmpty(coordinateList)) {
            throw new BusinessException("未在文档中找到关键字：" + keyword + "位置");
        }
        ESignCoordinateVO eSignCoordinateVO = coordinateList.get(0);
        ESignPosBeanForm posBeanForm = new ESignPosBeanForm();
        posBeanForm.setPosPage(eSignPositionInfoVO.getPageIndex().toString());
        posBeanForm.setPosX(eSignCoordinateVO.getPosx());
        posBeanForm.setPosY(eSignCoordinateVO.getPosy());
        return posBeanForm;
    }

    /**
     * 一步发起签章
     *
     * @param createForm
     * @return
     */
    public ESignOneStepVO createFlowOneStep(ESignFlowOneStepCreateForm createForm) {
        Long enterpriseId = createForm.getEnterpriseId();
        // 查询文件详情
        String fileId = createForm.getFileId();
        ESignFileDetailVO fileDetail = this.getFileDetail(fileId, enterpriseId);
        List<Integer> okStatus = Lists.newArrayList(2, 5);
        if (fileDetail == null) {
            throw new BusinessException("待签署文件不存在");
        }
        if (!okStatus.contains(fileDetail.getStatus())) {
            throw new BusinessException("待签署文件状态异常请稍后再试（" + fileDetail.getStatus() + "）");
        }
        // ----流程基本信息
        ESignFlowInfoForm flowInfoForm = new ESignFlowInfoForm();
        flowInfoForm.setBusinessScene(createForm.getBusinessScene());
        flowInfoForm.setAutoArchive(Boolean.TRUE);
        flowInfoForm.setAutoInitiate(Boolean.TRUE);
        flowInfoForm.setSignValidity(null);
        flowInfoForm.setContractValidity(null);
        flowInfoForm.setContractRemind(null);
        flowInfoForm.setInitiatorAccountId(null);
        flowInfoForm.setInitiatorAuthorizedAccountId(null);
        // --本次签署流程的任务信息配置
        ESignFlowConfigForm flowConfigForm = new ESignFlowConfigForm();
        // 认证配置项
        ESignFlowIdentificationConfigForm identificationConfigForm = new ESignFlowIdentificationConfigForm();
        identificationConfigForm.setPersonAvailableAuthTypes(Lists.newArrayList("PSN_TELECOM_AUTHCODE"));
        identificationConfigForm.setPersonAuthAdvancedEnabled(null);
        identificationConfigForm.setOrgAvailableAuthTypes(Lists.newArrayList("ORG_LEGAL_AUTHORIZE", "LEGAL_REP_AUTH"));
        identificationConfigForm.setWillTypes(Lists.newArrayList("CODE_SMS"));
        identificationConfigForm.setFaceVideoTemplate(null);
        flowConfigForm.setIdentificationConfig(identificationConfigForm);
        // 通知配置项
        ESignFlowNotifyConfigForm notifyConfigForm = new ESignFlowNotifyConfigForm();
        notifyConfigForm.setNoticeDeveloperUrl(noticeDeveloperUrl);
        notifyConfigForm.setNoticeType("1");
        flowConfigForm.setNotifyConfig(notifyConfigForm);
        // 签署配置项
        ESignFlowSignConfigForm signFlowSignConfigForm = new ESignFlowSignConfigForm();
        signFlowSignConfigForm.setRedirectUrl(null);
        signFlowSignConfigForm.setSignPlatform("1");
        signFlowSignConfigForm.setBatchDropSeal(Boolean.TRUE);
        signFlowSignConfigForm.setCountdown(0);
        signFlowSignConfigForm.setRedirectDelayTime(null);
        signFlowSignConfigForm.setMobileShieldWay("1");
        flowConfigForm.setSignConfig(signFlowSignConfigForm);
        flowInfoForm.setFlowConfigInfo(flowConfigForm);
        // ----签署人配置
        List<ESignSignerForm> signers = Lists.newArrayList();
        String platformKeyword = createForm.getPlatformKeyword();
        // --平台方-签署配置
        // 平台时间
        String platformTimeKeyword = createForm.getPlatformTimeKeyword();
        ESignDateBeanForm platformDateBeanForm = null;
        if (StringUtils.isNotBlank(platformTimeKeyword)) {
            // 寻找平台时间签署位置
            ESignPosBeanForm posBeanTimeForm = this.getPosBean(fileId, platformTimeKeyword, enterpriseId);
            platformDateBeanForm = new ESignDateBeanForm();
            platformDateBeanForm.setFontSize(12);
            platformDateBeanForm.setFormat("yyyy年MM月dd日");
            platformDateBeanForm.setPosPage(Integer.parseInt(posBeanTimeForm.getPosPage()));
            platformDateBeanForm.setPosX(posBeanTimeForm.getPosX());
            platformDateBeanForm.setPosY(posBeanTimeForm.getPosY());
        }
        if (StringUtils.isNotBlank(platformKeyword)) {
            // 平台方-寻找签署位置
            ESignPosBeanForm posBeanPlatformForm = this.getPosBean(fileId, platformKeyword, enterpriseId);
            // 平台方-设置签署配置
            ESignSignFieldForm fieldPlatformForm = new ESignSignFieldForm();
            fieldPlatformForm.setAssignedPosbean(Boolean.TRUE);
            fieldPlatformForm.setAutoExecute(Boolean.TRUE);
            fieldPlatformForm.setActorIndentityType(2);
            fieldPlatformForm.setFileId(fileId);
            fieldPlatformForm.setSealIds(null);
            fieldPlatformForm.setSealId(null);
            fieldPlatformForm.setSealBizTypes(null);
            fieldPlatformForm.setSealType("1");
            fieldPlatformForm.setSignType(1);
            fieldPlatformForm.setPosBean(posBeanPlatformForm);
            fieldPlatformForm.setWidth(null);
            fieldPlatformForm.setSignDateBeanType(0);
            fieldPlatformForm.setSignDateBean(null);
            // 平台方-骑缝章签署位置
            ESignPosBeanForm posBeanRidingTheSeamForm = new ESignPosBeanForm();
            posBeanRidingTheSeamForm.setPosPage("all");
            posBeanRidingTheSeamForm.setPosY(Float.valueOf("250"));
            // 平台方-骑缝章签署配置
            ESignSignFieldForm ridingTheSeamForm = new ESignSignFieldForm();
            ridingTheSeamForm.setAssignedPosbean(Boolean.TRUE);
            ridingTheSeamForm.setAutoExecute(Boolean.TRUE);
            ridingTheSeamForm.setActorIndentityType(2);
            ridingTheSeamForm.setFileId(fileId);
            ridingTheSeamForm.setSealIds(null);
            ridingTheSeamForm.setSealId(null);
            ridingTheSeamForm.setSealBizTypes(null);
            ridingTheSeamForm.setSealType("1");
            ridingTheSeamForm.setSignType(2);
            ridingTheSeamForm.setPosBean(posBeanRidingTheSeamForm);
            ridingTheSeamForm.setWidth(null);
            ridingTheSeamForm.setSignDateBeanType(platformDateBeanForm == null ? 0 : 1);
            ridingTheSeamForm.setSignDateBean(platformDateBeanForm);
            // 平台方签署配置
            ESignSignerForm signSignerPlatformForm = new ESignSignerForm();
            signSignerPlatformForm.setPlatformSign(Boolean.TRUE);
            signSignerPlatformForm.setSignOrder(1);
            signSignerPlatformForm.setForceReadTime(0);
            signSignerPlatformForm.setSignerAccount(null);
            signSignerPlatformForm.setSignfields(Lists.newArrayList(fieldPlatformForm, ridingTheSeamForm));
            signSignerPlatformForm.setThirdOrderNo(createForm.getThirdOrderNo());
            signers.add(signSignerPlatformForm);
        }
        // ----用户
        // 用户时间
        String userTimeKeyword = createForm.getUserTimeKeyword();
        ESignDateBeanForm userDateBeanForm = null;
        if (StringUtils.isNotBlank(userTimeKeyword)) {
            // 寻找用户时间签署位置
            ESignPosBeanForm posBeanTimeForm = this.getPosBean(fileId, userTimeKeyword, enterpriseId);
            userDateBeanForm = new ESignDateBeanForm();
            userDateBeanForm.setFontSize(12);
            userDateBeanForm.setFormat("yyyy年MM月dd日");
            userDateBeanForm.setPosPage(Integer.parseInt(posBeanTimeForm.getPosPage()));
            userDateBeanForm.setPosX(posBeanTimeForm.getPosX());
            userDateBeanForm.setPosY(posBeanTimeForm.getPosY());
        }
        String userKeyword = createForm.getUserKeyword();
        if (StringUtils.isNotBlank(userKeyword)) {
            if (StringUtils.isBlank(createForm.getSignerAccount()) || createForm.getSignerAccountInfo() == null) {
                throw new BusinessException("签署人信息为空");
            }
            // 用户-寻找签署位置
            ESignPosBeanForm posBeanUserForm = this.getPosBean(fileId, userKeyword, enterpriseId);
            // 用户方账户信息
            ESignSignerAccountForm signerAccountForm = new ESignSignerAccountForm();
            signerAccountForm.setSignerAccount(createForm.getSignerAccount());
            signerAccountForm.setSignerAccountInfo(createForm.getSignerAccountInfo());
            signerAccountForm.setAuthorizedAccount(createForm.getAuthorizedAccount());
            signerAccountForm.setOrgAccountInfo(createForm.getOrgAccountInfo());
            signerAccountForm.setNoticeType("1");
            // 用户方-设置签署配置
            ESignSignFieldForm fieldUserForm = new ESignSignFieldForm();
            fieldUserForm.setAssignedPosbean(Boolean.TRUE);
            fieldUserForm.setAutoExecute(Boolean.FALSE);
            fieldUserForm.setActorIndentityType(createForm.getOrgAccountInfo() == null ? 0 : 2);
            fieldUserForm.setFileId(fileId);
            fieldUserForm.setSealIds(null);
            fieldUserForm.setSealId(null);
            fieldUserForm.setSealBizTypes(null);
            fieldUserForm.setSealType("1");
            fieldUserForm.setSignType(1);
            fieldUserForm.setPosBean(posBeanUserForm);
            fieldUserForm.setWidth(null);
            fieldUserForm.setSignDateBeanType(userDateBeanForm == null ? 0 : 1);
            fieldUserForm.setSignDateBean(userDateBeanForm);
            // 用户方签署配置
            ESignSignerForm signSignerUserForm = new ESignSignerForm();
            signSignerUserForm.setPlatformSign(Boolean.FALSE);
            signSignerUserForm.setSignOrder(2);
            signSignerUserForm.setSignerAccount(signerAccountForm);
            signSignerUserForm.setSignfields(Lists.newArrayList(fieldUserForm));
            signSignerUserForm.setThirdOrderNo(createForm.getThirdOrderNo());
            signers.add(signSignerUserForm);
        }
        if (CollectionUtils.isEmpty(signers)) {
            throw new BusinessException("未设置签署人信息");
        }
        /**
         * 待签署文件
         */
        ESignDocsForm eSignDocsForm = new ESignDocsForm();
        eSignDocsForm.setFileId(fileDetail.getFileId());
        eSignDocsForm.setFileName(fileDetail.getName());
        eSignDocsForm.setEncryption(0);
        eSignDocsForm.setFilePassword(null);
        /**
         * 构建最终请求参数
         */
        ESignFlowOneStepForm oneStepForm = new ESignFlowOneStepForm();
        oneStepForm.setFlowInfo(flowInfoForm);
        oneStepForm.setSigners(signers);
        oneStepForm.setDocs(Lists.newArrayList(eSignDocsForm));
        oneStepForm.setAttachments(null);
        oneStepForm.setCopiers(null);

        ESignOneStepVO eSignOneStepVO = requestService.post(oneStepForm, ESignApiConst.CREATE_FLOW_ONE_STEP, ESignOneStepVO.class, enterpriseId);
        return eSignOneStepVO;
    }

    /**
     * 查询文件详情
     *
     * @param eSignExecuteUrlForm
     * @return
     */
    public ESignExecuteUrlVO getExecuteUrl(ESignExecuteUrlForm eSignExecuteUrlForm) {
        if (eSignExecuteUrlForm == null) {
            return null;
        }
        eSignExecuteUrlForm.setClientType("ALL");
        String api = String.format(ESignApiConst.EXECUTE_URL, eSignExecuteUrlForm.getFlowId());
        eSignExecuteUrlForm.setFlowId(null);
        return requestService.get(api, ESignExecuteUrlVO.class, eSignExecuteUrlForm, eSignExecuteUrlForm.getEnterpriseId());
    }

    /**
     * 下载已签文件
     *
     * @param flowId
     * @return
     */
    public ESignDocumentsVO getDocuments(String flowId, Long enterpriseId) {
        if (StringUtils.isBlank(flowId)) {
            return null;
        }
        String api = String.format(ESignApiConst.DOCUMENTS, flowId);
        return requestService.get(api, ESignDocumentsVO.class, enterpriseId);
    }

    /**
     * 撤销签署流程
     *
     * @param flowId
     * @return
     */
    public void revoke(String flowId, Long enterpriseId) {
        if (StringUtils.isBlank(flowId)) {
            return;
        }
        String api = String.format(ESignApiConst.REVOKE, flowId);
        requestService.put(api, enterpriseId);
    }
}
