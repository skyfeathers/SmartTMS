package net.lab1024.tms.admin.module.business.flow.bussiness.domain;

import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

/**
 * @author yandy
 * @description:
 * @date 2022/1/10 2:10 下午
 */
@Data
public class FlowBusinessEndBO {

    private Long businessId;

    private String businessCode;

    private FlowAuditStatusEnum auditStatusEnum;

}