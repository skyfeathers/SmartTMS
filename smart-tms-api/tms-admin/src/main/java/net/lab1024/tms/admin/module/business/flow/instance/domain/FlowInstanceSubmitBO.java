package net.lab1024.tms.admin.module.business.flow.instance.domain;

import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;

/**
 * [ 提交流程实例信息 ]
 *
 * @author yandanyang
 * @date 2021/8/17 17:21
 */
@Data
public class FlowInstanceSubmitBO {

    /**
     * 流程类型
     */
    private FlowTypeEnum flowTypeEnum;

    /**
     * 业务单据id
     */
    private Long businessId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 业务单据编号
     */
    private String businessCode;

    /**
     * 发起人id
     */
    private Long initiatorId;

    /**
     * 备注
     */
    private String remark;
}
