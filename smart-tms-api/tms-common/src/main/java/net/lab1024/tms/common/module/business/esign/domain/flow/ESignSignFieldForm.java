package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

import java.util.List;

/**
 * 签署方的签署区列表数据
 *
 * @author lihaifan
 * @date 2022/7/18 17:59
 */
@Data
public class ESignSignFieldForm {

    /**
     * 表示签署过程中，是否固定指定的签署区位置，默认为true。
     *
     * true-固定签署区位置且无法移动，
     *
     * false-不固定签署区位置且签署时可自由移动
     *
     * 注：当signType为1或者2时，该字段才会生效
     */
    private Boolean assignedPosbean;

    /**
     * 是否自动执行签署，默认false
     *
     * true-自动签署，
     *
     * false-手动签署。
     *
     * 注：
     *
     * 1、平台方自动签署时，该字段必传，传入true；
     *
     * 2、SaaS API标准版产品，平台用户不支持自动签署
     */
    private Boolean autoExecute;

    /**
     * 企业主体签约类型：0-个人盖章，2-机构盖章；
     *
     * 默认是0
     *
     * 注：
     *
     * 1、签署主体是个人时，无需传入该参数，或者传0
     *
     * 2、签署主体是企业时，该字段必传，传入2
     */
    private Integer actorIndentityType;

    /**
     * 文件fileId
     */
    private String fileId;

    /**
     * 印章ids，用于手动签署时，指定企业印章进行展示，实现手动选择印章进行签署
     *
     * 1、只支持签约主体为企业时指定印章，通过e签宝官网获取对应实名主体下的印章编号。
     *
     * 2、支持传入多个印章id，入参格式：sealIds:["印章id1","印章id2",....]
     */
    private List<String> sealIds;

    /**
     * 印章ID，通过e签宝官网获取对应实名主体下的印章编号。
     *
     * 注：
     *
     * (1) 当印章ID为空时，取appId对应企业的默认印章；
     *
     * (2) 平台方自动盖章签署时，仅限传入企业公章，不支持指定企业法定代表人印章；
     *
     * (3) 平台方自动盖章签署时，如果指定企业授权印章，签署后的签名信息，印章样式和数字证书均为授权企业主体所有，详细参考【印章授权说明】
     */
    private String sealId;

    /**
     * 指定业务印章类型（仅支持签约主体为企业时指定，
     *
     * 允许指定多个印章类型，英文逗号分隔）
     *
     * ALL -不指定印章类型，
     *
     * COMMON -无业务类型(其他)，
     *
     * CONTRACT -合同专用章，
     *
     * FINANCE -财务章，
     *
     * PERSONNEL -人事专用章，
     *
     * PUBLIC -公章
     */
    private String sealBizTypes;

    /**
     * 签署方式，个人签署时支持多种签署方式，
     *
     * 0-手绘签名  ，
     *
     * 1-模板印章签名，
     *
     * 多种类型时逗号分割，为空不限制
     */
    private String sealType;

    /**
     * 签署类型，0-不限，1-单页签署，2-骑缝签署
     *
     * 默认1
     */
    private Integer signType;

    /**
     * 签署区位置信息 。
     *
     * signType为0时，本参数无效；
     *
     * signType为1时, 页码和XY坐标不能为空；
     *
     * signType为2时, 页码和Y坐标不能为空
     */
    private ESignPosBeanForm posBean;

    /**
     * 指定签署区的宽度，单位px像素。默认取印章大小（个人印章96px，企业印章159px）。
     *
     * 不建议设置为0，会造成页面看不到印章图片。
     *
     * 不需指定宽度时，可不传width参数。
     */
    private Integer width;

    /**
     * 是否需要添加签署日期，
     *
     * 0-禁止 1-必须 2-不限制，默认0
     */
    private Integer signDateBeanType;

    /**
     * 签署日期信息
     */
    private ESignDateBeanForm signDateBean;
}
