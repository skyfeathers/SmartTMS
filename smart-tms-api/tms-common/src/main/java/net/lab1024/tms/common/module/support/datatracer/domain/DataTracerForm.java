package net.lab1024.tms.common.module.support.datatracer.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.tms.common.common.enumeration.BaseEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
@NoArgsConstructor
public class DataTracerForm {

    /**
     * 业务id
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private DataTracerBusinessTypeEnum businessType;

    /**
     * 操作类型
     */
    private BaseEnum operateType;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 对比
     */
    private DataTracerDiffBO diff;


    private String ip;

    private String userAgent;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 扩展信息
     */
    private DataTracerExtraData extraData;

    public DataTracerForm(DataTracerRequestForm dataTracerRequestForm) {
        this.ip = dataTracerRequestForm.getIp();
        this.userAgent = dataTracerRequestForm.getUserAgent();
        this.operateTime = dataTracerRequestForm.getOperateTime();
    }
}
