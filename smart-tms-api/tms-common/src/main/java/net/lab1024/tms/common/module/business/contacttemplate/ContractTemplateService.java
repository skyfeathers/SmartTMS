package net.lab1024.tms.common.module.business.contacttemplate;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartHtml2PdfUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.contacttemplate.dao.ContractTemplateDao;
import net.lab1024.tms.common.module.business.contacttemplate.domain.dto.ContractGenerateDTO;
import net.lab1024.tms.common.module.business.contacttemplate.domain.entity.ContractTemplateEntity;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateFieldValueForm;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateForm;
import net.lab1024.tms.common.module.business.contacttemplate.domain.vo.ContractTemplateVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/12 15:07
 */
@Service
@Slf4j
public class ContractTemplateService {

    @Value("${contact.template.file-path}")
    private String contractTemplateTempFilePath;

    @Autowired
    private ContractTemplateDao contractTemplateDao;

    @PostConstruct
    public void checkContractTemplateTempFilePath() {
        File dir = new File(contractTemplateTempFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取合同模板列表
     *
     * @return
     */
    public ResponseDTO<List<ContractTemplateVO>> templateList() {
        List<ContractTemplateEntity> templateEntityList = contractTemplateDao.selectByDisableFlag(false);
        if (CollectionUtils.isEmpty(templateEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<ContractTemplateVO> templateVOList = SmartBeanUtil.copyList(templateEntityList, ContractTemplateVO.class);
        return ResponseDTO.ok(templateVOList);
    }


    /**
     * 根据合同模板生成合同
     *
     * @param generateForm
     * @return
     */
    public ResponseDTO<File> contractGenerate(ContractGenerateForm generateForm) {
        Long templateId = generateForm.getTemplateId();
        ContractTemplateEntity templateEntity = contractTemplateDao.selectById(templateId);
        if (templateEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "模板信息不存在");
        }
        //校验是否存在重复字段信息
        List<ContractGenerateFieldValueForm> fieldValueList = generateForm.getFieldValueList();
        Set<String> fieldKeySet = fieldValueList.stream().map(ContractGenerateFieldValueForm::getFieldKey).collect(Collectors.toSet());
        if (fieldKeySet.size() != fieldValueList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "模板字段信息重复");
        }
        //合同名称
        String contractName = this.buildContractFileName(templateEntity.getTemplateName(), generateForm.getSerialNumber());
        Map<String, String> fieldValueMap = fieldValueList.stream().collect(Collectors.toMap(ContractGenerateFieldValueForm::getFieldKey, e -> {
            return SmartStringUtil.isEmpty(e.getFieldValue()) ? "&emsp;&emsp;&emsp;&emsp;" : e.getFieldValue();
        }));
        //生成合同文件
        ContractGenerateDTO contractGenerateDTO = new ContractGenerateDTO();
        contractGenerateDTO.setTemplateContent(templateEntity.getTemplateContent());
        contractGenerateDTO.setFieldValueMap(fieldValueMap);
        contractGenerateDTO.setContractName(contractName);
        File contractFile = this.generateContract(contractGenerateDTO);
        if (contractFile == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "生成合同失败");
        }
        return ResponseDTO.ok(contractFile);
    }

    /**
     * 合同生成
     *
     * @param contractGenerateDTO
     * @return
     */
    private File generateContract(ContractGenerateDTO contractGenerateDTO) {
        String templateContent = contractGenerateDTO.getTemplateContent();
        Map<String, String> fieldValueMap = contractGenerateDTO.getFieldValueMap();
        String contractName = contractTemplateTempFilePath + contractGenerateDTO.getContractName();
        StringSubstitutor stringSubstitutor = new StringSubstitutor(fieldValueMap);
        String contractHtml = stringSubstitutor.replace(templateContent);
        //解析html 替换签名内容
        Document doc = Jsoup.parse(contractHtml);
        //防止删除html结束标签
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        contractHtml = doc.outerHtml();
        File contractFile = SmartHtml2PdfUtil.html2pdf(contractHtml, contractName);
        return contractFile;
    }

    /**
     * 合同名称
     *
     * @param templateName
     * @param orderItemId
     * @return
     */
    private String buildContractFileName(String templateName, Serializable orderItemId) {
        String nameFormat = "%s_%s_%s.pdf";
        if (templateName == null) {
            templateName = "";
        }
        String contractName = String.format(nameFormat, templateName, orderItemId, UUID.randomUUID().toString());
        return contractName;
    }
}
