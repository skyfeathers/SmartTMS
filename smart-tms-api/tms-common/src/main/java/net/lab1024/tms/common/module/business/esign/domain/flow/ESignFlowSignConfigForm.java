package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 本次签署流程的任务信息配置 - 签署配置项
 *
 * @author lihaifan
 * @date 2022/9/16 10:08
 */
@Data
public class ESignFlowSignConfigForm {

    /**
     * 签署完成后，重定向跳转地址（http/https）
     */
    private String redirectUrl;

    /**
     * 签署平台，默认值1,2。
     *
     * 可选择多种签署平台，逗号分割。
     *
     * 1-H5网页版方式进行签署；
     *
     * 2-跳转支付宝(移动端)或支付宝扫码进行签署(PC端)。
     */
    private String signPlatform;

    /**
     * 签署页是否显示“一键落章”按钮，
     *
     * 默认显示。
     *
     * 关闭显示 - false
     *
     * 显示 - true
     */
    private Boolean batchDropSeal;

    /**
     * 签署页提交倒计时，单位为秒，
     *
     * 不传默认为0，最大999
     */
    private Integer countdown;

    /**
     * 签署完成重定向跳转延迟时间，默认3。
     *
     * 0-不展示签署完成结果页，签署完成直接跳转重定向地址
     *
     * 3-展示签署完成结果页，倒计时3秒后，自动跳转重定向地址
     *
     * 注：当redirectUrl不传的情况下，该字段无需传入，默认签署完成结果页不跳转
     */
    private Integer redirectDelayTime;

    /**
     * 是否使用e签盾签署，默认为1.
     *
     * 1-不使用e签盾；
     *
     * 2-使用e签盾
     *
     * 注：使用e签盾签署后，签署平台默认提示跳转e签宝app进行签署
     */
    private String mobileShieldWay;
}
