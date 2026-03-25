package net.lab1024.tms.admin.module.business.esign;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.contacttemplate.ContractTemplateService;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateFieldValueForm;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateForm;
import net.lab1024.tms.common.module.business.esign.ESignService;
import net.lab1024.tms.common.module.business.esign.domain.flow.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description
 *
 * @author lihaifan
 * @date 2022/7/14 16:08
 */
class ESignServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private ESignService eSignService;

    @Autowired
    private ContractTemplateService contractTemplateService;

    @Test
    void uploadFile(){
        File file = new File("/Users/haifanli/hiDoc/中邦合同/直接承运运输合同2022.7.15.pdf");
        eSignService.uploadFile(file,8L);
    }

    @Test
    void getFileStatus(){
        eSignService.getFileStatus("6129d4b1f3bb424082ce3a78a69a04bc",8L);
    }

    @Test
    void searchWordsPosition(){
        eSignService.searchWordsPosition("6129d4b1f3bb424082ce3a78a69a04bc","甲方(签章)",8L);
    }

    @Test
    void createFlowOneStep(){
        ESignFlowOneStepCreateForm oneStepCreateForm = new ESignFlowOneStepCreateForm();
        oneStepCreateForm.setEnterpriseId(8L);
        oneStepCreateForm.setFileId("6129d4b1f3bb424082ce3a78a69a04bc");
        oneStepCreateForm.setBusinessScene("直接承运运输合同2022.7.19");
        oneStepCreateForm.setPlatformKeyword("甲方(签章)");
        oneStepCreateForm.setPlatformTimeKeyword(null);
        oneStepCreateForm.setUserKeyword("乙方(签章)");
        oneStepCreateForm.setUserTimeKeyword("时间");
        oneStepCreateForm.setThirdOrderNo("202209160001");
        oneStepCreateForm.setSignerAccount("17630506613");
        ESignSignerAccountInfoForm signSignerAccountInfoForm = new ESignSignerAccountInfoForm();
        signSignerAccountInfoForm.setName("李海帆");
        signSignerAccountInfoForm.setCertNo("410303199305060534");
        signSignerAccountInfoForm.setCertType("CRED_PSN_CH_IDCARD");
        signSignerAccountInfoForm.setBankCardNo(null);
        oneStepCreateForm.setSignerAccountInfo(signSignerAccountInfoForm);
        oneStepCreateForm.setAuthorizedAccount(null);
        oneStepCreateForm.setOrgAccountInfo(null);
        eSignService.createFlowOneStep(oneStepCreateForm);
        // {"code":0,"data":{"flowId":"60ab1a7342d442f68f767a12a5ee8680"},"message":"成功"}
    }

    @Test
    void getExecuteUrl(){
        ESignExecuteUrlForm eSignExecuteUrlForm = new ESignExecuteUrlForm();
        eSignExecuteUrlForm.setEnterpriseId(8L);
        eSignExecuteUrlForm.setFlowId("60ab1a7342d442f68f767a12a5ee8680");
        eSignExecuteUrlForm.setUrlType(2);
        eSignExecuteUrlForm.setClientType("ALL");
        eSignExecuteUrlForm.setAppScheme(null);
        eSignExecuteUrlForm.setSignerAccount("17630506613");
        eSignExecuteUrlForm.setAuthorizedAccount(null);
        eSignExecuteUrlForm.setAccountId(null);
        eSignExecuteUrlForm.setAuthorizedAccountId(null);
        ESignExecuteUrlVO executeUrl = eSignService.getExecuteUrl(eSignExecuteUrlForm);
        System.out.println(executeUrl);
    }

    @Test
    void getDocuments(){
        ESignDocumentsVO documents = eSignService.getDocuments("60ab1a7342d442f68f767a12a5ee8680",8L);
        System.out.println(documents);
    }

    @Test
    void revoke(){
        eSignService.revoke("60ab1a7342d442f68f767a12a5ee8680",8L);
    }

    @Test
    void generateContractFile() {
        ContractGenerateForm form = ContractGenerateForm.builder()
                .templateId(1L)//网络货运-司机合同
                .serialNumber("WLHY-SJ-" + UUID.randomUUID())
                .fieldValueList(null)
                .build();

        List<ContractGenerateFieldValueForm> fields = new ArrayList<>();
        fields.add(ContractGenerateFieldValueForm.builder()
                .fieldKey("driverName")
                .fieldValue("张三")
                .build());
        fields.add(ContractGenerateFieldValueForm.builder()
                .fieldKey("enterpriseName")
                .fieldValue("苹果公司")
                .build());
        fields.add(ContractGenerateFieldValueForm.builder()
                .fieldKey("networkFreightTransportCode")
                .fieldValue("110+120+119")
                .build());

        form.setFieldValueList(fields);

        ResponseDTO<File> contractEntityResponseDTO =
                contractTemplateService.contractGenerate(form);
        File data = contractEntityResponseDTO.getData();
    }
}