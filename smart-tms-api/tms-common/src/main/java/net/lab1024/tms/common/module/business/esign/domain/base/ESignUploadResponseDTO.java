package net.lab1024.tms.common.module.business.esign.domain.base;

import lombok.Data;

/**
 * 文件上传返回值
 *
 * @author lihaifan
 * @date 2022/7/16 15:53
 */
@Data
public class ESignUploadResponseDTO {

    /**
     * 业务码，0表示成功
     */
    private Integer errCode;

    /**
     * 信息
     */
    private String msg;
}
