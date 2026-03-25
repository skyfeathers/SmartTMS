package net.lab1024.tms.common.module.support.conpanygroup;

import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.conpanygroup.annoation.CompanyGroupParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 公司分组切面
 *
 * @author zhaoxinyang
 * @date 2025/1/4 9:48
 */
//@Aspect
//@Component
public class CompanyGroupAspect {

    /**
     * 定义切入点
     *
     * @param point
     * @return
     * @throws Throwable
     */
//    @Around("@annotation(net.lab1024.tms.common.module.support.conpanygroup.annoation.CompanyGroup)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
//        Long companyId = SmartRequestUtil.getCompanyId();
//        if (companyId == null) {
//            return point.proceed();
//        }
//        Object[] args = point.getArgs();
//        Integer selectArgIndex = this.selectArgIndex(args);
//        if (selectArgIndex == null) {
//            return point.proceed();
//        }
//
//        Object argProxy = CompanyGroupProxy.withCompanyId(args[selectArgIndex], companyId);
//        args[selectArgIndex] = argProxy;
//        return point.proceed(args);
        return point.proceed();
    }

    /**
     * 选择参数位置
     *
     * @param args
     * @return
     */
    private Integer selectArgIndex(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            CompanyGroupParam annotation = args[i].getClass().getAnnotation(CompanyGroupParam.class);
            if (annotation != null) {
                return i;
            }
        }
        return null;
    }

}
