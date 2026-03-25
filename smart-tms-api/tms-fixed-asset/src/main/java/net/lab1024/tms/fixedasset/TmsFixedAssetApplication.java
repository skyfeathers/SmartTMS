package net.lab1024.tms.fixedasset;

import net.lab1024.tms.common.constant.SystemConst;
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
public class TmsFixedAssetApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringApplication.run(TmsFixedAssetApplication.class, args);
        stopWatch.stop();
        System.out.printf("########################## TMS FixedAsset Service 发射升空 [%s seconds] ############################ \n", stopWatch.getTotalTimeMillis() / 1000f);
    }
}
