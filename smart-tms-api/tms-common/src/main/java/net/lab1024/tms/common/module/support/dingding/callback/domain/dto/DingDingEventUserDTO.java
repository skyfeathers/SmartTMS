package net.lab1024.tms.common.module.support.dingding.callback.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 通讯录事件参数
 *
 * @author lidoudou
 * @date 2023/4/22 上午11:12
 */
@Data
public class DingDingEventUserDTO extends DingDingEventBaseDTO {

    /**
     * 用户发生变更的userId列表。
     */
    private List<String> userId;
}
