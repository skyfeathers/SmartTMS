package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;

import java.time.LocalDateTime;

/***
 * 货主的负责人
 *
 * @author lidoudou
 * @date 2022/6/24 上午10:05
 */
@Data
@TableName("t_shipper_principal")
public class ShipperPrincipalEntity {

    @TableId(type = IdType.AUTO)
    private Long principalId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 负责人类型
     *
     * @see PrincipalTypeEnum
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
