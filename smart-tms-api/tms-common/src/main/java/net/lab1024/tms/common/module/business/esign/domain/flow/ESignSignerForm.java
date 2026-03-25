package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

import java.util.List;

/**
 * Description
 *
 * @author lihaifan
 * @date 2022/7/18 17:55
 */
@Data
public class ESignSignerForm {

    /**
     * 是否平台方自动签署，默认false
     *
     * true-平台方自动签署
     *
     * false-平台用户签署
     */
    private Boolean platformSign;

    /**
     * 签署顺序，默认1，且不小于1。
     *
     * 顺序越小越先处理
     */
    private Integer signOrder;

    /**
     * 签署强制阅读时间, 单位为秒，不传默认为0，最大999
     *
     * 注：
     *
     * （1）签署页提交倒计时，仅限平台用户手动签署时可用。
     *
     * （2）与流程中的签署配置项countdown字段作用相同，只是此字段在签署人维度控制，优先级更高。
     */
    private Integer forceReadTime;

    /**
     * 签署方账号信息
     *
     * （平台方自动签署时，无需传入该参数）
     */
    private ESignSignerAccountForm signerAccount;

    /**
     * 签署方的签署区列表数据
     */
    private List<ESignSignFieldForm> signfields;

    /**
     * 第三方流水号
     */
    private String thirdOrderNo;


}
