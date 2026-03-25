package net.lab1024.tms.common.module.business.material.contracttype;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
@TableName("t_contract_type")
public class ContractTypeEntity {

    @TableId(type = IdType.AUTO)
    private Long contractTypeId;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer seq;

    private Long templateId;

    private Integer templateType;

    /**
     * 备注
     */
    private String remark;

    private Integer deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
     */
    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
