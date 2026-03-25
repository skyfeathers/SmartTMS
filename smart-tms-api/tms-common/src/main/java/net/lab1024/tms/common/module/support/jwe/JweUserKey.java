package net.lab1024.tms.common.module.support.jwe;

import lombok.Data;

/**
 * @Description: 解密用用户信息作为key
 * @Author: 罗伊
 */
@Data
public class JweUserKey {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展信息
     */
    private String extData;

}
