package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车-基本信息
 *
 * @author zhaoxinyang
 * @date 2023/10/23 15:47
 */
@Data
@TableName("t_car_cost_basic_info")
public class CarCostBasicInfoEntity {

    /**
     * 自有车基本信息ID
     */
    @TableId(type = IdType.AUTO)
    private Long basicInfoId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 高速里程
     */
    @DataTracerFieldDoc("高速里程")
    @DataTracerFieldBigDecimal
    private BigDecimal highSpeedMileage;

    /**
     * 低速里程
     */
    @DataTracerFieldDoc("低速里程")
    @DataTracerFieldBigDecimal
    private BigDecimal lowSpeedMileage;

    /**
     * GPS里程
     */
    @DataTracerFieldDoc("GPS里程")
    @DataTracerFieldBigDecimal
    private BigDecimal gpsMileage;

    /**
     * 用油量-升数
     */
    @DataTracerFieldDoc("升数")
    @DataTracerFieldBigDecimal
    private BigDecimal oilConsumption;

    @DataTracerFieldDoc("基本工资")
    @DataTracerFieldBigDecimal
    private BigDecimal basicSalary;

    @DataTracerFieldDoc("补助")
    @DataTracerFieldBigDecimal
    private BigDecimal allowance;


    /**
     * 工资板块-提成工资
     */
    @DataTracerFieldDoc("提成工资")
    @DataTracerFieldBigDecimal
    private BigDecimal commissionSalary;

    /**
     * 合计
     */
    private BigDecimal salaryTotal;

    /**
     * 工资板块-出勤天数
     */
    @DataTracerFieldDoc("出勤天数")
    private Integer attendanceDays;

    /**
     * 确认状态
     */
    private Boolean confirmFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}