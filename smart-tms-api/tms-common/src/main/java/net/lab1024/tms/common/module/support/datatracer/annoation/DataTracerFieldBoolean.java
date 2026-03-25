package net.lab1024.tms.common.module.support.datatracer.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataTracerFieldBoolean {
    /**
     * 本属性的注释信息{a_id,b_id}
     *
     * @return
     */
    String[] value() default {"禁用_true", "启用_false"};

}
