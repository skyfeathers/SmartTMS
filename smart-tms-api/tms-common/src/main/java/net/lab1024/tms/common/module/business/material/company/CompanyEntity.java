package net.lab1024.tms.common.module.business.material.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-公司管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
@TableName("t_material_company")
public class CompanyEntity {

    /**
     * 公司ID
     */
    @TableId(type = IdType.AUTO)
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司编号
     */
    private String companyCode;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
