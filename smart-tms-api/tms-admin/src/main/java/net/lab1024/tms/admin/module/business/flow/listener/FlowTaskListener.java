package net.lab1024.tms.admin.module.business.flow.listener;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author yandy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface FlowTaskListener {

    @AliasFor(
            annotation = Service.class
    )
    String value() default "";

    String desc() default "";
}
