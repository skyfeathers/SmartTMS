package net.lab1024.tms.common.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhuoda
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${file.storage.local.path}")
    private String localPath;

    @Autowired(required = false)
    private List<HandlerInterceptor> interceptorList;

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        if (CollectionUtils.isEmpty(interceptorList)) {
            return;
        }
        interceptorList.forEach(e->{
            registry.addInterceptor(e).addPathPatterns("/**");
        });
    }

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/preview/**")
                .addResourceLocations("file:" + localPath);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:swagger-ui.html");
    }
}
