package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

import java.util.List;

/**
 * 一步发起签署
 *
 * @author lihaifan
 * @date 2022/7/18 17:45
 */
@Data
public class ESignFlowOneStepForm {

    /**
     * 本次签署流程的基本信息
     */
    private ESignFlowInfoForm flowInfo;

    /**
     * 签署方信息
     *
     * *类型为数组，多方签署建议添加多个对象
     */
    private List<ESignSignerForm> signers;

    /**
     * 待签署文件信息
     */
    private List<ESignDocsForm> docs;

    /**
     * 附件信息（仅用于查看，不需要签署的文档）
     */
    private List<ESignAttachmentsForm> attachments;

    /**
     * 抄送人列表
     *
     * 注：
     *
     * 抄送人指不参与签署但需要查看签署文件的人
     */
    private List<ESignCopiersForm> copiers;
}
