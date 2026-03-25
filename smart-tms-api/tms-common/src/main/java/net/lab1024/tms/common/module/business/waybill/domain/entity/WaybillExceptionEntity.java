package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillExceptionTypeEnum;

import java.time.LocalDateTime;

/**
 * 运单异常
 *
 * @author lihaifan
 * @date 2022/12/3 14:13
 */
@Data
@TableName("t_waybill_exception")
public class WaybillExceptionEntity {

    /**
     * 异常ID
     */
    @TableId(type = IdType.AUTO)
    private Long exceptionId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 异常类型
     * @see WaybillExceptionTypeEnum
     */
    private Integer exceptionType;

    /**
     * 异常描述
     */
    private String exceptionMessage;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人类型
     * @see UserTypeEnum
     */
    private Integer createUserType;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名称
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
