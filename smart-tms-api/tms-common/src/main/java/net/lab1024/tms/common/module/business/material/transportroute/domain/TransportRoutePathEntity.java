package net.lab1024.tms.common.module.business.material.transportroute.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;

import java.time.LocalDateTime;

/**
 * 运输路线的各种地点
 *
 * @author lidoudou
 * @date 2022/7/12 下午2:44
 */
@Data
@TableName("t_transport_route_path")
public class TransportRoutePathEntity {

    @TableId(type = IdType.AUTO)
    private Long transportRoutePathId;

    /**
     * 运输路线ID
     */
    private Long transportRouteId;

    /**
     * 类型
     *
     * @see PathTypeEnum
     */
    private Integer type;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市
     */
    private Integer city;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区县
     */
    private Integer district;

    /**
     * 区县名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
