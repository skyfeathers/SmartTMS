package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 获取签署链接
 *
 * @author lihaifan
 * @date 2022/9/16 11:40
 */
@Data
public class ESignExecuteUrlVO {

    /**
     * 长链地址(永久有效)，通过此链接访问不需要登录框。
     */
    private String url;

    /**
     * 短链地址（30天有效），通过此链接访问不需要登录框。
     */
    private String shortUrl;
}
