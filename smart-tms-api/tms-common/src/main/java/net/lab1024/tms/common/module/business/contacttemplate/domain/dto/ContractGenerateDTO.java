package net.lab1024.tms.common.module.business.contacttemplate.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/28 16:43
 */
@Data
public class ContractGenerateDTO {

    /**
     * 模板内容
     */
    private String templateContent;


    /**
     * 字段值
     */
    private Map<String, String> fieldValueMap;

    /**
     * 合同名称
     */
    private String contractName;
}
