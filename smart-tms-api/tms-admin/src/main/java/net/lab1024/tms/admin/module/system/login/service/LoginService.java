package net.lab1024.tms.admin.module.system.login.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.service.EmployeePermissionService;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.admin.module.system.login.domain.LoginEmployeeDetail;
import net.lab1024.tms.admin.module.system.login.domain.LoginEnterpriseVO;
import net.lab1024.tms.admin.module.system.login.domain.LoginForm;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.tms.common.common.constant.RequestHeaderConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.RequestEnterprise;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartIpUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.captcha.CaptchaService;
import net.lab1024.tms.common.module.support.captcha.domain.CaptchaVO;
import net.lab1024.tms.common.module.support.dingding.auth.DingDingAuthService;
import net.lab1024.tms.common.module.support.loginlog.LoginLogResultEnum;
import net.lab1024.tms.common.module.support.loginlog.LoginLogService;
import net.lab1024.tms.common.module.support.loginlog.domain.LoginLogEntity;
import net.lab1024.tms.common.module.support.loginlog.domain.LoginLogVO;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigVO;
import net.lab1024.tms.common.module.support.token.LoginDeviceEnum;
import net.lab1024.tms.common.module.support.token.TokenService;
import net.lab1024.tms.common.module.support.token.UserTerminalEnum;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhuoda
 * @date 2021-11-1
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmployeePermissionService employeePermissionService;

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private DingDingAuthService dingDingAuthService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private EmployeeDao employeeDao;

    private ConcurrentMap<Long, LoginEmployeeDetail> loginUserDetailCache = new ConcurrentLinkedHashMap.Builder<Long, LoginEmployeeDetail>().maximumWeightedCapacity(1000).build();

    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

    /**
     * 员工登陆
     *
     * @param loginForm
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginEmployeeDetail> login(LoginForm loginForm, String ip, String userAgent, RequestEnterprise requestEnterprise) {
        LoginDeviceEnum loginDeviceEnum = SmartBaseEnumUtil.getEnumByValue(loginForm.getLoginDevice(), LoginDeviceEnum.class);
        if (loginDeviceEnum == null) {
            return ResponseDTO.userErrorParam("登录设备暂不支持！");
        }
        // 校验 图形验证码
        ResponseDTO<String> checkCaptcha = captchaService.checkCaptcha(loginForm);
        if (!checkCaptcha.getOk()) {
            return ResponseDTO.error(checkCaptcha);
        }

        /**
         * 验证账号和账号状态
         */
        String uniqueCode = loginForm.getUniqueCode();
        EmployeeEntity employeeEntity = employeeService.getByLoginName(requestEnterprise.getEnterpriseId(), loginForm.getLoginName());
        if (null == employeeEntity) {
            return ResponseDTO.userErrorParam("登录名不存在！");
        }

        if (employeeEntity.getLeaveFlag()) {
            saveLoginLog(employeeEntity, ip, userAgent, uniqueCode, "账号所属人员已离职", LoginLogResultEnum.LOGIN_FAIL);
            return ResponseDTO.userErrorParam("您已离职");
        }

        if (employeeEntity.getDisabledFlag()) {
            saveLoginLog(employeeEntity, ip, userAgent, uniqueCode, "账号已禁用", LoginLogResultEnum.LOGIN_FAIL);
            return ResponseDTO.userErrorParam("您的账号已被禁用,请联系工作人员！");
        }
        /**
         * 验证密码：
         * 1、万能密码
         * 2、真实密码
         */
        String superPassword = EmployeeService.getEncryptPwd(systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD));
        String requestPassword = EmployeeService.getEncryptPwd(loginForm.getPassword());
        if (!(superPassword.equals(requestPassword) || employeeEntity.getLoginPwd().equals(requestPassword))) {
            // 记录登录失败
            saveLoginLog(employeeEntity, ip, userAgent, uniqueCode, "密码错误", LoginLogResultEnum.LOGIN_FAIL);
            return ResponseDTO.userErrorParam("登录名或密码错误！");
        }

        // 删除已存在token 生成登录token，保存token
        tokenService.deleteLoginToken(employeeEntity.getEmployeeId(), UserTypeEnum.ADMIN);
        Boolean superPasswordFlag = superPassword.equals(requestPassword);
        String token = tokenService.generateToken(employeeEntity.getEmployeeId(), employeeEntity.getActualName(), UserTypeEnum.ADMIN, loginDeviceEnum, UserTerminalEnum.ADMIN, superPasswordFlag);

        LoginEmployeeDetail loginEmployeeDetail = loadLoginInfo(employeeEntity);
        loginEmployeeDetail.setToken(token);
        // 放入缓存
        loginUserDetailCache.put(employeeEntity.getEmployeeId(), loginEmployeeDetail);
        //保存登录记录
        saveLoginLog(employeeEntity, ip, userAgent, uniqueCode, superPasswordFlag ? "万能密码登录" : loginDeviceEnum.getDesc(), LoginLogResultEnum.LOGIN_SUCCESS);

        return ResponseDTO.ok(loginEmployeeDetail);
    }

    /**
     * 校验唯一码
     *
     * @param employeeId
     * @param uniqueCode
     * @return
     */
    private Boolean checkLoginException(Long employeeId, String uniqueCode) {
        LoginLogVO loginLogVO = loginLogService.queryLastByUserId(employeeId, UserTypeEnum.ADMIN, LoginLogResultEnum.LOGIN_SUCCESS);
        if (null == loginLogVO || StrUtil.isBlank(loginLogVO.getUniqueCode())) {
            return false;
        }

        return !loginLogVO.getUniqueCode().equals(uniqueCode);
    }

    /**
     * 保存登录日志
     */
    public void saveLoginLog(EmployeeEntity employeeEntity, String ip, String userAgent, String uniqueCode, String remark, LoginLogResultEnum result) {
        Boolean loginExceptionFlag = this.checkLoginException(employeeEntity.getEmployeeId(), uniqueCode);
        LoginLogEntity loginEntity = LoginLogEntity.builder()
                .userId(employeeEntity.getEmployeeId())
                .enterpriseId(employeeEntity.getEnterpriseId())
                .userType(UserTypeEnum.ADMIN.getValue())
                .userName(employeeEntity.getActualName())
                .userAgent(userAgent)
                .loginIp(ip)
                .loginIpRegion(SmartIpUtil.getRegion(ip))
                .uniqueCode(uniqueCode)
                .loginExceptionFlag(loginExceptionFlag)
                .remark(remark)
                .loginResult(result.getValue())
                .createTime(LocalDateTime.now())
                .build();
        loginLogService.log(loginEntity);
    }

    /**
     * 校验上次修改密码日期
     *
     * @param employeeEntity
     * @return
     */
    private Boolean checkEditPwd(EmployeeEntity employeeEntity) {
        if (employeeEntity.getAdministratorFlag()) {
            return false;
        }

        SystemConfigVO config = systemConfigService.getConfig(SystemConfigKeyEnum.FORCE_EDIT_PWD_FLAG);
        if (config == null) {
            log.error("强制密码修改系统参数配置错误，请及时修改");
            return false;
        }
        String configValue = config.getConfigValue();
        if (!configValue.equals("true") && !configValue.equals("false")) {
            log.error("强制密码修改系统参数配置错误，请及时修改");
            return false;
        }
        if (!Boolean.parseBoolean(configValue)) {
            return false;
        }

        config = systemConfigService.getConfig(SystemConfigKeyEnum.FORCE_EDIT_PWD_DAYS);
        if (config == null || !NumberUtil.isNumber(config.getConfigValue())) {
            log.error("强制密码修改系统参数配置错误，请及时修改");
            return false;
        }

        LocalDate lastEditPwdDate = employeeEntity.getLastEditPwdDate();
        if (lastEditPwdDate.plusDays(Integer.parseInt(config.getConfigValue())).isBefore(LocalDate.now())) {
            return true;
        }
        return false;
    }


    /**
     * 获取登录的用户信息
     *
     * @return
     */
    private LoginEmployeeDetail loadLoginInfo(EmployeeEntity employeeEntity) {
        LoginEmployeeDetail loginEmployeeDetail = SmartBeanUtil.copy(employeeEntity, LoginEmployeeDetail.class);
        loginEmployeeDetail.setUserType(UserTypeEnum.ADMIN);

        //部门信息
        DepartmentVO department = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        loginEmployeeDetail.setDepartmentName(null == department ? StringConst.EMPTY : department.getName());

        // 强制修改密码标识
        Boolean forceEditPwdFlag = this.checkEditPwd(employeeEntity);
        loginEmployeeDetail.setForceEditPwdFlag(forceEditPwdFlag);

        // 企业信息
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(employeeEntity.getEnterpriseId());

        /**
         * 获取前端菜单和后端权限
         * 1、从数据库获取所有的权限
         * 2、拼凑成菜单和后端权限
         */
        List<MenuVO> menuAndPointsList = employeePermissionService.getEmployeeMenuAndPointsList(enterpriseEntity, employeeEntity);
        //前端菜单
        loginEmployeeDetail.setMenuList(menuAndPointsList);
        //后端权限
        loginEmployeeDetail.setAuthorities(employeePermissionService.buildAuthorities(menuAndPointsList));
        return loginEmployeeDetail;
    }

    /**
     * 根据登陆token 获取员请求工信息
     *
     * @param
     * @return
     */
    public LoginEmployeeDetail getLoginUserDetail(String token, HttpServletRequest request) {
        Long requestUserId = tokenService.parseUserIdByToken(token);
        if (requestUserId == null) {
            return null;
        }
        // 查询用户信息
        LoginEmployeeDetail loginEmployeeDetail = loginUserDetailCache.get(requestUserId);
        if (loginEmployeeDetail == null) {
            // 员工基本信息
            EmployeeEntity employeeEntity = employeeService.getById(requestUserId);
            if (employeeEntity == null) {
                return null;
            }

            loginEmployeeDetail = this.loadLoginInfo(employeeEntity);
            loginEmployeeDetail.setToken(token);
            loginUserDetailCache.put(requestUserId, loginEmployeeDetail);
        }
        //更新请求ip和user agent
        loginEmployeeDetail.setUserAgent(ServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT));
        loginEmployeeDetail.setIp(ServletUtil.getClientIP(request));

        return loginEmployeeDetail;
    }


    /**
     * 退出登陆，清除token缓存
     *
     * @param token
     * @param requestUserId
     * @return
     */
    public ResponseDTO<String> logout(String token, Long requestUserId) {
        loginUserDetailCache.remove(requestUserId);
        tokenService.removeToken(token);
        return ResponseDTO.ok();
    }

    /**
     * 移除权限缓存
     *
     * @param requestUserId
     */
    public void removeLoginUserDetailCache(Long requestUserId) {
        loginUserDetailCache.remove(requestUserId);
    }

    public ResponseDTO<LoginEmployeeDetail> dingDingLogin(String authCode, RequestEnterprise requestEnterprise) {
        String mobile = dingDingAuthService.getMobileByCode(authCode);
        if (SmartStringUtil.isBlank(mobile)) {
            return ResponseDTO.userErrorParam("登录用户不存在");
        }
        EmployeeEntity employeeEntity = employeeDao.getByPhone(requestEnterprise.getEnterpriseId(), mobile, null);
        if (null == employeeEntity) {
            return ResponseDTO.userErrorParam("登录用户不存在");
        }
        if (employeeEntity.getLeaveFlag()) {
            return ResponseDTO.userErrorParam("您已离职");
        }
        if (employeeEntity.getDisabledFlag()) {
            return ResponseDTO.userErrorParam("您的账号已被禁用,请联系工作人员！");
        }
        String token = tokenService.generateToken(employeeEntity.getEmployeeId(), employeeEntity.getActualName(), UserTypeEnum.ADMIN, LoginDeviceEnum.PC, UserTerminalEnum.ADMIN, Boolean.FALSE);
        LoginEmployeeDetail loginEmployeeDetail = loadLoginInfo(employeeEntity);
        loginEmployeeDetail.setToken(token);
        // 放入缓存
        loginUserDetailCache.put(employeeEntity.getEmployeeId(), loginEmployeeDetail);

        return ResponseDTO.ok(loginEmployeeDetail);
    }

    /**
     * 获取登录企业信息
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<LoginEnterpriseVO> getLoginEnterprise(Long enterpriseId) {
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);

        LoginEnterpriseVO loginEnterpriseVO = new LoginEnterpriseVO();
        loginEnterpriseVO.setCompanyName(enterpriseEntity.getEnterpriseName());
        loginEnterpriseVO.setFaviconUrl(enterpriseEntity.getEnterpriseLogo());
        loginEnterpriseVO.setLogoUrl(enterpriseEntity.getEnterpriseLogo());
        loginEnterpriseVO.setWebsiteName(enterpriseEntity.getWebsiteName());
        loginEnterpriseVO.setWebsiteDesc(enterpriseEntity.getWebsiteDesc());
        loginEnterpriseVO.setBeiAnNo(enterpriseEntity.getBeiAnNo());
        return ResponseDTO.ok(loginEnterpriseVO);
    }
}
