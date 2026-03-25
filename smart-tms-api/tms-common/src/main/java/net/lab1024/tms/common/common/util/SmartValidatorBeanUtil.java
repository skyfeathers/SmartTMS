package net.lab1024.tms.common.common.util;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 验证类工具
 *
 * @author listen
 * @date 2017/11/06 11:27
 */
public class SmartValidatorBeanUtil {

    /**
     * 手动验证对象 Model的属性
     * 该对象的属性 需要使用 Hibernate 验证框架的注解
     *
     * @return String 返回null 代表验证通过，否则 则返回错误的信息
     * @Author listen
     */
    public static String validateModel(Object obj) {
        // 创建 验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        // 获取验证结果
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(obj);
        if (CollectionUtils.isEmpty(constraintViolationSet)) {
            // 验证通过
            return null;
        }
        // 遍历结果 并返回错误信息
        List<String> stringList = constraintViolationSet.stream().map(ConstraintViolation :: getMessage).collect(Collectors.toList());
        return stringList.toString();
    }
}
