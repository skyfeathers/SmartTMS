package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 获取签署链接
 *
 * @author lihaifan
 * @date 2022/9/16 11:43
 */
@Data
public class ESignExecuteUrlForm {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 签署流程ID
     */
    private String flowId;

    /**
     * 链接类型（默认值 2）
     *
     * 1 - 预览链接（只能查看，不能签署）
     *
     * 2 - 签署链接
     */
    private Integer urlType;

    /**
     * 客户端类型，默认值：H5
     *
     * H5 - 移动端
     *
     * PC - PC端
     *
     * ALL - 自动适配移动端或PC端
     */
    private String clientType;

    /**
     * AppScheme，用于唤起App。
     *
     * 示例：
     *
     * appScheme=xxxdev://cn.demo/signBackPage
     */
    private String appScheme;

    /**
     * 签署人账号（手机号/邮箱）
     *
     * 用于获取个人签署链接，需与发起签署时账号一致。
     */
    private String signerAccount;

    /**
     * 签署机构账号名称，默认为空。
     *
     * 用于获取机构签署链接，需与发起签署时名称一致。
     *
     * 注：
     *
     * 一个签署流程中某人代多个机构签署时，需要通过此参数分别获取对应机构的签署链接。
     */
    private String authorizedAccount;

    /**
     * 签署人账号ID（个人签署账号ID）
     *
     * 用于获取个人签署链接，需与发起签署时账号ID一致。
     *
     * 注：传入e签宝SaaS官网的账号ID，即通过【获取认证链接】操作认证后生成的账号accountId。
     */
    private String accountId;

    /**
     * 签署机构账号ID，默认为空。
     *
     * 用于获取机构签署链接，需与发起签署时账号ID一致。
     *
     * 注：
     *
     * （1）一个签署流程中某人代多个机构签署时，需要通过此参数分别获取对应机构的签署链接。
     *
     * （2）传入e签宝SaaS官网的账号ID，即通过【获取认证链接】操作认证后生成的账号orgId。
     */
    private String authorizedAccountId;
}
