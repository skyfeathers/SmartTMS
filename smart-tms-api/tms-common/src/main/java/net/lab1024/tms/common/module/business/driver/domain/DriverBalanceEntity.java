package net.lab1024.tms.common.module.business.driver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/***
 * 司机企业余额
 *
 * @author lidoudou
 * @date 2022/6/21 上午09:58
 */
@Data
@TableName("t_driver_balance")
public class DriverBalanceEntity {

    @TableId(type = IdType.AUTO)
    private Long driverBalanceId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 版本号
     */
    private Integer updateVersion;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
