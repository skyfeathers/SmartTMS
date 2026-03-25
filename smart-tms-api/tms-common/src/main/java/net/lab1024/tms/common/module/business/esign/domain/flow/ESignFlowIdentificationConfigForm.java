package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

import java.util.List;

/**
 * 本次签署流程的任务信息配置 - 认证配置项
 *
 * @author lihaifan
 * @date 2022/9/16 10:08
 */
@Data
public class ESignFlowIdentificationConfigForm {

    /**
     * 个人页面显示实名认证方式
     *
     * PSN_BANK4_AUTHCODE
     *
     * 个人银行卡四要素认证
     *
     * PSN_TELECOM_AUTHCODE
     *
     * 个人运营商三要素认证
     *
     * PSN_FACEAUTH_BYURL
     *
     * 个人刷脸认证
     */
    private List<String> personAvailableAuthTypes;

    /**
     * 指定通过银行卡认证或运营商认证方式时，是否使用详情版，如指定则核验失败可返回具体不匹配信息，传空默认为普通版。
     *
     * PSN_BANK4_AUTHCODE
     *
     * 个人银行卡四要素认证
     *
     * PSN_TELECOM_AUTHCODE
     *
     * 个人运营商三要素认证
     *
     * 注：详情版，需要单独购买，具体购买方式请咨询e签宝工作人员
     *
     * 普通版，信息比对核验失败，不会返回具体的不匹配信息
     */
    private List<String> personAuthAdvancedEnabled;

    /**
     * 企业页面显示实名认证方式
     *
     * ORG_BANK_TRANSFER 组织机构对公账户打款认证
     *
     * ORG_ZM_AUTHORIZE 企业芝麻认证
     *
     * ORG_LEGAL_AUTHORIZE 组织机构法定代表人授权书签署认证
     *
     * LEGAL_REP_AUTH 法定代表人认证
     *
     * 注：
     *
     * （1）法定代表人本人认证的方式默认都是在页面存在的，无法去掉
     *
     * （2）当只希望用户选择【法定代表人认证】一种方式时，此参数仅传入 LEGAL_REP_AUTH 即可
     */
    private List<String> orgAvailableAuthTypes;

    /**
     * 页面指定意愿认证方式，可指定类型如
     *
     * CODE_SMS 短信验证码
     *
     * FACE_ZHIMA_XY 支付宝刷脸
     *
     * FACE_TECENT_CLOUD_H5 腾讯云刷脸
     *
     * FACE_FACE_LIVENESS_RECOGNITION e签宝刷脸
     *
     * 以下三种方式需联系交付顾问开通后方可使用：
     *
     * FACE_WE_CHAT_FACE 微信小程序刷脸
     *
     * FACE_AUDIO_VIDEO_DUAL 支付宝智能视频认证
     *
     * VIDEO_WE_CHAT_VIDEO_DUAL 微信智能视频认证
     */
    private List<String> willTypes;

    /**
     * 视频认证模板id，请联系交付顾问提供
     */
    private String faceVideoTemplate;
}
