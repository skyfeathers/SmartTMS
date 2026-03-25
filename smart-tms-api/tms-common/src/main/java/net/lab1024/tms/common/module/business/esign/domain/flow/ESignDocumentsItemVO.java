package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 下载已签文件-单个文档
 *
 * @author lihaifan
 * @date 2022/9/16 15:47
 */
@Data
public class ESignDocumentsItemVO {

    /**
     * 文档id
     */
    private String fileId;

    /**
     * 文档名称,默认文件名称
     */
    private String fileName;

    /**
     * 文档地址（有效期60分钟）
     *
     * 该链接建议只用于下载，不要直接预览
     */
    private String fileUrl;
}
