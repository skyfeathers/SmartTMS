package net.lab1024.tms.common.module.business.fleet.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/***
 * 银行信息
 *
 * @author lidoudou
 * @date 2022/6/27 下午3:23
 */
@Data
@TableName("t_fleet_bank")
public class FleetBankEntity {

    @TableId(type = IdType.AUTO)
    private Long bankId;

    /**
     * 车队ID
     */
    private Long fleetId;

    /**
     * 银行类型 数据字典
     */
    private String bankType;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 支行名称
     */
    private String bankBranchName;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 附件信息
     */
    private String attachment;

    /**
     * 是否默认
     */
    private Boolean defaultFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
