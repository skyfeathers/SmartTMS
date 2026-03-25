package net.lab1024.tms.admin.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 货主关联的公司信息
 *
 * @author lidoudou
 * @date 2022/8/18 下午4:57
 */
@Data
@TableName("t_shipper_enterprise")
@Deprecated
public class ShipperEnterpriseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 货主ID
     */
    private Long shipperId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
