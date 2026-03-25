package net.lab1024.tms.admin.module.system.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.enumeration.GenderEnum;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 员工登陆
 *
 * @author 李善逸
 * @date 2021/8/4 21:15
 */
@Data
public class LoginEmployeeDetail implements UserDetails, RequestUser {

    @ApiModelProperty("token")
    private String token;

    private UserTypeEnum userType;

    private String ip;

    private String userAgent;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("企业id")
    private Long enterpriseId;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("是否为超管")
    private Boolean administratorFlag;

    @ApiModelProperty("是否需要修改密码")
    private Boolean forceEditPwdFlag;

    @ApiModelProperty("菜单列表")
    private List<MenuVO> menuList;

    @JsonIgnore
    private String loginPassword;

    /**
     * security 权限串
     */
    private Set<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.loginPassword;
    }

    @Override
    public String getUsername() {
        return this.getLoginName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getUserId() {
        return employeeId;
    }

    @Override
    public String getUserName() {
        return actualName;
    }

    @Override
    public UserTypeEnum getUserType() {
        return userType;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }
}
