package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:29
 */
@Data
@TableName("t_shipper_contact")
public class ShipperContactEntity {

    @TableId(type = IdType.AUTO)
    private Long contactId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 联系人名称
     */
    @DataTracerFieldDoc("联系人名称")
    private String contactName;

    /**
     * 联系人电话
     */
    @DataTracerFieldDoc("联系人电话")
    private String contactPhone;

    /**
     * 岗位
     */
    private String position;

    private Integer provinceCode;

    private String provinceName;

    private Integer cityCode;

    private String cityName;

    private Integer districtCode;

    private String districtName;

    private String address;
    /**
     * 备注
     */
    private String remark;
    /**
     * 默认标识
     */
    private Boolean defaultFlag;
    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
