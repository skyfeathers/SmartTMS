package net.lab1024.tms.common.common.util;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.RequestEnterprise;

/**
 * @author 罗伊
 */
@Slf4j
public class SmartRequestEnterpriseUtil {

    private static final ThreadLocal<RequestEnterprise> requestThreadLocal = new ThreadLocal<>();


    public static void setRequestEnterprise(RequestEnterprise requestEnterprise) {
        requestThreadLocal.set(requestEnterprise);
    }

    public static RequestEnterprise getRequestEnterprise() {
        return requestThreadLocal.get();
    }

    public static Long getRequestEnterpriseId() {
        RequestEnterprise requestEnterprise = getRequestEnterprise();
        return null == requestEnterprise ? null : requestEnterprise.getEnterpriseId();
    }

    public static Boolean getRequestEnterprisePlatformFlag() {
        RequestEnterprise requestEnterprise = getRequestEnterprise();
        return null == requestEnterprise ? false : requestEnterprise.getPlatformFlag();
    }


    public static void remove() {
        requestThreadLocal.remove();
    }


}
