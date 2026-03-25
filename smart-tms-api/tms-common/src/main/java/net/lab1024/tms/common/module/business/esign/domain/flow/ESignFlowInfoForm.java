package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 本次签署流程的基本信息
 *
 * @author lihaifan
 * @date 2022/7/18 17:47
 */
@Data
public class ESignFlowInfoForm {

    /**
     * 本次签署流程的文件主题名称
     *
     * 注：不支持以下9个字符：/ \ : * " < > | ？
     */
    private String businessScene;

    /**
     * 全部签章后流程自动结束，默认false。
     *
     * true - 自动结束
     *
     * false - 非自动结束
     *
     * false时需要开发者调用【签署流程结束】接口结束流程
     */
    private Boolean autoArchive;

    /**
     * 是否自动开启，默认false。
     */
    private Boolean autoInitiate;

    /**
     * 本次签署流程的截止时间。
     *
     * 时间戳格式，单位：毫秒，默认永久有效。
     *
     * 注：超过签署有效截止时间，则无法继续签署。
     */
    private Integer signValidity;

    /**
     * 本次签署文件的到期时间。
     *
     * 时间戳格式，单位：毫秒，默认永久有效。
     *
     * 注：只有设置了contractValidity，
     *
     * 才能设置续签提醒时间contractRemind。
     */
    private Integer contractValidity;

    /**
     * 本次签署的续签提醒时间。
     *
     * 单位：小时，默认不提前提醒。
     *
     * 注：时间区间[1-360]，可通过
     *
     * 异步通知方式触发续签通知
     */
    private Integer contractRemind;

    /**
     * 发起人账户id，发起本次签署的操作人个人账号id；如不传，默认由对接平台发起
     */
    private String initiatorAccountId;

    /**
     * 发起方主体id，如存在个人代机构发起签约，则需传入机构id；如不传，则默认是对接平台
     */
    private String initiatorAuthorizedAccountId;

    /**
     * 本次签署流程的任务信息配置
     */
    private ESignFlowConfigForm flowConfigInfo;
}
