package net.lab1024.tms.common.module.business.driver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/***
 * 司机的银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/21 上午09:58
 */
@Data
@TableName("t_driver_bank")
public class DriverBankEntity {

    @TableId(type = IdType.AUTO)
    private Long bankId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 银行类型 数据字典
     */
    private String bankType;

    /**
     * 银行名称
     */
    @DataTracerFieldDoc("银行名称")
    private String bankName;

    /**
     * 支行名称
     */
    @DataTracerFieldDoc("支行名称")
    private String bankBranchName;

    /**
     * 开户名
     */
    @DataTracerFieldDoc("开户名")
    private String accountName;

    /**
     * 银行账号
     */
    @DataTracerFieldDoc("银行账号")
    private String bankAccount;

    /**
     * 银行卡正面
     */
    private String bankCardFrontAttachment;

    /**
     * 是否为纳税人信息
     */
    private Boolean taxpayerFlag;

    /**
     * 是否默认
     */
    private Boolean defaultFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建人类型
     */
    private Integer createUserType;

    /**
     * 创建人类型名称
     */
    private String createUserTypeDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
