package net.lab1024.tms.admin.module.business.contract.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.module.business.contacttemplate.domain.form.ContractGenerateForm;
import net.lab1024.tms.common.module.business.esign.domain.flow.ESignFlowOneStepCreateForm;

/**
 * 合同构建BO
 *
 * @author lihaifan
 * @date 2022/9/25 17:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractBuildBO {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 生成合同文件
     */
    private ContractGenerateForm contractGenerateForm;

    /**
     * 发起合同签署
     */
    private ESignFlowOneStepCreateForm eSignFlowOneStepCreateForm;
}
