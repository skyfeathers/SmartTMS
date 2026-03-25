package net.lab1024.tms.common.common.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统环境枚举类
 *
 * @author zhuoda
 */
@AllArgsConstructor
@Getter
public enum SystemEnvironmentEnum implements BaseEnum {
    /**
     * dev
     */
    DEV(SystemEnvironmentNameConst.DEV, "开发环境"),

    /**
     * sit
     */
    SIT(SystemEnvironmentNameConst.SIT, "测试环境"),

    /**
     * pre
     */
    PRE(SystemEnvironmentNameConst.PRE, "预发布环境"),

    /**
     * prod
     */
    PROD(SystemEnvironmentNameConst.PROD, "生产环境"),
    /**
     * 货车联 prod
     */
    HCL_PROD(SystemEnvironmentNameConst.HCL_PROD, "HCL生产环境"),

    /**
     * SASS prod
     */
    SASS(SystemEnvironmentNameConst.SASS, "SASS生产环境");

    private final String value;

    private final String desc;

    public static final class SystemEnvironmentNameConst {
        public static final String DEV = "dev";
        public static final String SIT = "sit";
        public static final String PRE = "pre";
        public static final String PROD = "prod";
        public static final String HCL_PROD = "hcl-prod";
        public static final String SASS = "sass";
    }

    public static boolean isProd(String systemEnvironment){
        if(StringUtils.isBlank(systemEnvironment)){
            return false;
        }
        return SystemEnvironmentNameConst.PROD.equals(systemEnvironment) || SystemEnvironmentNameConst.HCL_PROD.equals(systemEnvironment) || SystemEnvironmentNameConst.SASS.equals(systemEnvironment);
    }

}
