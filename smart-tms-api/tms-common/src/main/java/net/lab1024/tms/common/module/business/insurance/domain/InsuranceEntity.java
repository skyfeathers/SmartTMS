package net.lab1024.tms.common.module.business.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDict;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 保险表
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:11
 */
@Data
@TableName("t_insurance")
public class InsuranceEntity {

    /**
     * 保险ID
     */
    @TableId(type = IdType.AUTO)
    private Long insuranceId;

    /**
     * 所属企业ID
     */
    private Long enterpriseId;

    /**
     * 保单号
     */
    @DataTracerFieldDoc("保单号")
    private String policyNumber;

    /**
     * 保险公司代码
     * 数据字典 insuranceCompanyCode
     */
    @DataTracerFieldDoc("保险公司")
    @DataTracerFieldDict
    private String insuranceCompanyCode;

    /**
     * 保险类型
     * @see InsuranceTypeEnum
     */
    @DataTracerFieldDoc("保险类型")
    @DataTracerFieldEnum(enumClass = InsuranceTypeEnum.class)
    private Integer insuranceType;

    /**
     * 保险对象ID
     */
    private Long moduleId;

    /**
     * 保险对象类型 1车辆 2挂车
     * @see InsuranceModuleTypeEnum
     */
    private Integer moduleType;

    /**
     * 保险金额
     */
    @DataTracerFieldDoc("保险金额")
    @DataTracerFieldBigDecimal
    private BigDecimal insuranceAmount;

    /**
     * 保险单费用
     */
    @DataTracerFieldDoc("保险单费用")
    @DataTracerFieldBigDecimal
    private BigDecimal policyInsuranceAmount;

    /**
     * 投保时间
     */
    @DataTracerFieldDoc("投保时间")
    private LocalDate effectDate;
    /**
     * 有效期至
     */
    @DataTracerFieldDoc("有效期至")
    private LocalDate expireDate;

    /**
     * 附件
     */
    @DataTracerFieldDoc("附件")
    private String attachment;

    /**
     * 登记日期
     */
    private LocalDate enrollDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
