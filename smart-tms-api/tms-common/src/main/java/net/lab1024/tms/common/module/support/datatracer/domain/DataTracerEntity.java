package net.lab1024.tms.common.module.support.datatracer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
@TableName("t_data_tracer")
public class DataTracerEntity {

    @TableId(type = IdType.AUTO)
    private Long dataTracerId;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 业务类型
     * {@link DataTracerBusinessTypeEnum}
     */
    private Integer businessType;

    /**
     * 业务类型描述
     * 因是死数据，只做展示使用故直接存类型描述信息
     */
    private String businessTypeDesc;

    /**
     * 操作类型
     * {@link DataTracerOperateTypeEnum}
     */
    private Integer operateType;

    private String operateTypeDesc;

    /**
     * 操作内容
     */
    private String operateContent;

    private String diff;

    /**
     * 操作人
     */
    private Long operatorId;

    private String operatorName;

    /**
     * 操作人类型
     */
    private Integer operatorType;

    /**
     * 操作人类型描述
     */
    private String operatorTypeDesc;

    private String ip;

    private String userAgent;

    /**
     * 扩展数据
     */
    private String extraData;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
