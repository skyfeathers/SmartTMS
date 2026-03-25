package net.lab1024.tms.admin.config;

import net.lab1024.tms.common.common.security.SecurityMethodSource;
import net.lab1024.tms.common.common.security.SecurityPermissionCheckService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 1、以类名加方法名为权限字符串的校验模式
 * 2、启用此类 将优化security配置，只需在方法上加上@saAuth注解，方法上就会存在权限（权限字符串为类名加方法名），而无需另外手动设置，减轻后端开发成本
 * 3、security将不再依据权限字符串进行权限控制，
 * 4、security将依据对应权限字符串下的接口权限进行控制
 * 5、采用此配置原@PreAuthorize依然有效
 * 6、如若无需此配置，需将@EnableGlobalMethodSecurity注解添加至SecurityConfig类上
 * @author yandanyang
 * @date 2021-08-31 0:01
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityMethodConfig extends GlobalMethodSecurityConfiguration {

    public static final String BEAN_NAME = "saAuth";

    @Bean(BEAN_NAME)
    public SecurityPermissionCheckService securityPermissionCheckService() {
        return new SecurityPermissionCheckService();
    }

    @Override
    public MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(this.getExpressionHandler());
        return new SecurityMethodSource(attributeFactory, BEAN_NAME);
    }
}
