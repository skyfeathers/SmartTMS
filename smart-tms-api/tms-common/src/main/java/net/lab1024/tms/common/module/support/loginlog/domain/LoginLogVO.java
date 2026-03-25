package net.lab1024.tms.common.module.support.loginlog.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.support.loginlog.LoginLogResultEnum;

import java.time.LocalDateTime;

/**
 * 登录日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class LoginLogVO {

    private Long loginLogId;

    @ApiModelProperty( "用户id")
    private Long userId;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "用户类型")
    private Integer userType;

    @ApiModelProperty( "用户名")
    private String userName;

    @ApiModelProperty( "登录ip")
    private String loginIp;

    @ApiModelProperty( "登录ip地区")
    private String loginIpRegion;

    @ApiModelProperty( "user-agent")
    private String userAgent;

    @ApiModelProperty("唯一码")
    private String uniqueCode;

    @ApiModelProperty( "是否异常登录")
    private Boolean loginExceptionFlag;

    @ApiModelProperty( "remark")
    private String remark;

    @ApiModelPropertyEnum(LoginLogResultEnum.class)
    private Integer loginResult;

    private LocalDateTime createTime;

}
