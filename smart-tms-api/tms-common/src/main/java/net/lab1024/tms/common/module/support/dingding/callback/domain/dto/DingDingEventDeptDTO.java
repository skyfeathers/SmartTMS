package net.lab1024.tms.common.module.support.dingding.callback.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 部门事件参数
 *
 * @author lidoudou
 * @date 2023/4/26 下午4:01
 */
@Data
public class DingDingEventDeptDTO extends DingDingEventBaseDTO {

    /**
     * 用户发生变更的userId列表。
     */
    private List<Long> deptId;
}
