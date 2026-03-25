package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.time.LocalDateTime;

/**
 * [ 货主类型 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:28
 */
@Data
@TableName("t_shipper_type")
public class ShipperTypeEntity {

    @TableId(type = IdType.AUTO)
    private Long typeId;
    /**
     * 机构id
     */
    private Long shipperId;
    /**
     * 机构类型
     */
    @DataTracerFieldDoc("业务关系")
    @DataTracerFieldEnum(enumClass = ShipperTypeEnum.class)
    private Integer shipperType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
