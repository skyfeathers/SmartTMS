package net.lab1024.tms.common.module.business.esign.domain.base;

import lombok.Data;

/**
 * e签宝接口返回值
 *
 * @author lihaifan
 * @date 2022/7/14 15:46
 */
@Data
public class ESignResponseDTO {

    /**
     * 业务码，0表示成功
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 业务信息
     */
    private Object data;
}
