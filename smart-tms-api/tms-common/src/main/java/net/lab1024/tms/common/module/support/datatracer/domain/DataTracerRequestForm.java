package net.lab1024.tms.common.module.support.datatracer.domain;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/7/29 4:07 下午
 */
@Data
public class DataTracerRequestForm {

    @ApiModelProperty(value = "enterpriseId",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "ip",hidden = true)
    private String ip;

    @ApiModelProperty(value = "userAgent",hidden = true)
    private String userAgent;

    @ApiModelProperty(value = "operatorId",hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "operatorName",hidden = true)
    private String operatorName;

    @ApiModelProperty(value = "operatorType",hidden = true)
    private Integer operatorType;

    @ApiModelProperty(value = "operatorTypeDesc",hidden = true)
    private String operatorTypeDesc;

    private LocalDateTime operateTime;


    public DataTracerRequestForm(HttpServletRequest request){
        this.enterpriseId = SmartRequestEnterpriseUtil.getRequestEnterpriseId();

        this.ip = ServletUtil.getClientIP(request);
        this.userAgent = ServletUtil.getHeaderIgnoreCase(request,"user-agent");

        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        this.operatorId = requestUser.getUserId();
        this.operatorName = requestUser.getUserName();

        this.operateTime = LocalDateTime.now();
        this.operatorType = requestUser.getUserType().getValue();
        this.operatorTypeDesc = requestUser.getUserType().getDesc();
    }

    public DataTracerRequestForm(Long enterpriseId, Long userId, String userName, UserTypeEnum userTypeEnum, HttpServletRequest request){
        this.enterpriseId = enterpriseId;

        this.ip = ServletUtil.getClientIP(request);
        this.userAgent = ServletUtil.getHeaderIgnoreCase(request,"user-agent");

        this.operatorId = userId;
        this.operatorType = userTypeEnum.getValue();
        this.operatorTypeDesc = userTypeEnum.getDesc();
        this.operatorName = userName;
        this.operateTime = LocalDateTime.now();
    }
}