package net.lab1024.tms.common.module.business.esign.domain.file;

import lombok.Data;

/**
 * 获取文件上传地址
 *
 * @author lihaifan
 * @date 2022/7/16 15:35
 */
@Data
public class ESignFileUploadVO {

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 文件上传地址，链接有效期60分钟。
     */
    private String uploadUrl;
}
