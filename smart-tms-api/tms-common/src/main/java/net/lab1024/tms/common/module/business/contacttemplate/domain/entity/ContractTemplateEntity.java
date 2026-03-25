package net.lab1024.tms.common.module.business.contacttemplate.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/12 11:25
 */
@Data
@TableName("t_contract_template")
public class ContractTemplateEntity {

    @TableId(type = IdType.AUTO)
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 禁用标识
     */
    private Boolean disableFlag;


    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
