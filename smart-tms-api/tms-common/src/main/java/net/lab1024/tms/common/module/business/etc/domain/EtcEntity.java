package net.lab1024.tms.common.module.business.etc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * etc管理
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:33
 */
@Data
@TableName("t_etc")
public class EtcEntity {

    /**
     * ETC ID
     */
    @TableId(type = IdType.AUTO)
    private Long etcId;

    /**
     * ETC 卡号
     */
    @DataTracerFieldDoc("卡号")
    private String etcNo;

    /**
     * 所属公司ID
     */
    private Long enterpriseId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 持卡人
     */
    private Long userId;

    /**
     * 持卡人姓名
     */
    private String userName;

    /**
     * 持卡车
     */
    private Long useVehicleId;

    /**
     * 持卡时间
     */
    private LocalDateTime useTime;

    /**
     * 密码
     */
    private String password;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
