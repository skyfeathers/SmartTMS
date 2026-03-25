package net.lab1024.tms.common.module.business.material.customsbroker;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-报关行
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
@TableName("t_material_customs_broker")
public class CustomsBrokerEntity {

    /**
     * 报关行ID
     */
    @TableId(type = IdType.AUTO)
    private Long customsBrokerId;

    /**
     * 报关行简称
     */
    private String customsBrokerShortName;

    /**
     * 报关行编号
     */
    private String customsBrokerCode;

    /**
     * 报关行名称
     */
    private String customsBrokerName;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市
     */
    private Integer city;

    /**
     * 城市名称
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
    private String contact;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 禁用状态
     */
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
     * 创建人ID
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
