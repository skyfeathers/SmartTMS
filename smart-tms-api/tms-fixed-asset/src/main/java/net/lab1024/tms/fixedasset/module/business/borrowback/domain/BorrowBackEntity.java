package net.lab1024.tms.fixedasset.module.business.borrowback.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2023/3/20 下午5:31
 */
@Data
@TableName("t_asset_borrow_back")
public class BorrowBackEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long borrowBackId;

    /**
     * 领用编号
     */
    private String borrowBackNo;

    /**
     * 使用日期
     */
    private LocalDate useTime;

    /**
     * 归还日期
     */
    private LocalDate backTime;

    /**
     * 使用公司
     */
    private Long enterpriseId;

    /**
     * 使用部门
     */
    private Long departmentId;

    /**
     * 使用人
     */
    private Long userId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 申请说明
     */
    private String remark;

    /**
     *
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
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
