package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 附件信息（仅用于查看，不需要签署的文档）
 *
 * @author lihaifan
 * @date 2022/7/18 18:09
 */
@Data
public class ESignAttachmentsForm {

    /**
     * 附件Id
     */
    private String fileId;

    /**
     * 附件名称
     *
     * 注：文件名称不支持以下9个字符：
     *
     * / \ : * " < > | ？
     */
    private String attachmentName;
}
