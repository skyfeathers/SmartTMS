package net.lab1024.tms.common.module.business.oilcard.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.driver.dao.CommonDriverDao;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.dao.CommonVehicleDao;
import net.lab1024.tms.common.module.support.datatracer.annoation.*;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 油卡管理
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:33
 */
@Data
@TableName("t_oil_card")
public class OilCardEntity {

    /**
     * 油卡ID
     */
    @TableId(type = IdType.AUTO)
    private Long oilCardId;

    private Long enterpriseId;


    @DataTracerFieldDoc("油卡卡号")
    private String oilCardNo;

    /**
     * 类型
     *
     * @see OilCardTypeEnum
     */
    @DataTracerFieldDoc("类型")
    @DataTracerFieldEnum(enumClass = OilCardTypeEnum.class)
    private Integer type;

    /**
     * 燃料类型
     *
     * @see OilCardFuelTypeEnum
     */
    @DataTracerFieldDoc("燃料类型")
    @DataTracerFieldEnum(enumClass = OilCardFuelTypeEnum.class)
    private Integer fuelType;

    /**
     * 关联的主卡ID
     */
    private Long masterOilCardId;

    @DataTracerFieldDoc("品牌")
    @DataTracerFieldDict
    private String brand;

    @DataTracerFieldDoc("用途")
    @DataTracerFieldDict
    private String purpose;

    @DataTracerFieldDoc("领取人")
    @DataTracerFieldSql(relateColumn = "employeeId", relateDisplayColumn = "actualName", relateMapper = CommonEmployeeDao.class)
    private Long receiveUserId;

    @DataTracerFieldDoc("持卡司机")
    @DataTracerFieldSql(relateColumn = "driverId", relateDisplayColumn = "driverName", relateMapper = CommonDriverDao.class)
    private Long useDriverId;

    @DataTracerFieldDoc("持卡车辆")
    @DataTracerFieldSql(relateColumn = "vehicleId", relateDisplayColumn = "vehicleNumber", relateMapper = CommonVehicleDao.class)
    private Long useVehicleId;

    /**
     * 持卡时间
     */
    private LocalDateTime useTime;


    @DataTracerFieldDoc("期初余额")
    private BigDecimal beginBalance;

    /**
     * 当前余额
     */
    @DataTracerFieldDoc("当前余额")
    private BigDecimal balance;

    /**
     * 预分配金额
     * 油卡核算审批通过后增加此值，管理岗审批通过后减此值
     */
    private BigDecimal preDistributionBalance;

    /**
     * 密码
     */
    private String password;

    /**
     * 计划充值金额
     */
    private BigDecimal preRechargeAmount;

    /**
     * 备注
     */
    private String remark;

    @DataTracerFieldDoc(value = "是否定点加油")
    @DataTracerFieldBoolean(value = {"是_true", "否_false"})
    private Boolean fixedPointFlag;

    @DataTracerFieldDoc(value = "禁用状态")
    @DataTracerFieldBoolean(value = {"禁用_true", "启用_false"})
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
