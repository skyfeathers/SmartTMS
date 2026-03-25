package net.lab1024.tms.common.module.support.repeatsubmit.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 需要防止重复提交 的注解
 *
 * @author 胡克
 * @date 2020年11月25日 20:56:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatSubmit {

    /**
     * 重复提交间隔时间/毫秒
     *
     * @return
     */
    int value() default 600;

    /**
     * 最长间隔30s
     */
    int MAX_INTERVAL = 30000;
}
