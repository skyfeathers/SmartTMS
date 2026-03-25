package net.lab1024.tms.admin;

import net.lab1024.tms.common.constant.SystemConst;
import net.lab1024.tms.common.listener.Ip2RegionListener;
import net.lab1024.tms.common.listener.LogVariableListener;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

/**
 * SmartAdmin 项目启动类
 *
 * @author 卓大
 */
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan(value = SystemConst.COMPONENT_SCAN, annotationClass = Mapper.class)
@SpringBootApplication(scanBasePackages = {SystemConst.COMPONENT_SCAN})
public class TmsAdminApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringApplication application = new SpringApplication(TmsAdminApplication.class);
        // 添加 日志监听器，使 log4j2-spring.xml 可以间接读取到配置文件的属性
        application.addListeners(new LogVariableListener(), new Ip2RegionListener());
        application.run(args);
        stopWatch.stop();
        System.out.printf("########################## TMS Admin Service 发射升空 [%s seconds] ############################ \n", stopWatch.getTotalTimeMillis() / 1000f);
    }
}
