package net.lab1024.tms.common.module.support.fieldencrypt;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yandy
 * @description:
 * @date 2023/9/14 10:14 上午
 */
public class FieldEncryptUtil {

    /**
     * 类 加注解字段缓存
     */
    private static ConcurrentHashMap<Class, List<Field>> fieldMap = new ConcurrentHashMap<>();

    /**
     * 批量设置设置对象 字符串属性值
     *
     * @param objectList
     * @param <T>
     */
    public static <T> void injectList(List<T> objectList) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        objectList.forEach(e -> {
            fieldInject(e);
        });
    }


    public static <T> void fieldInject(T object) {
        Class tClass = object.getClass();
        List<Field> fieldList = getField(object);
        for (Field field : fieldList) {
            field.setAccessible(true);
            String fieldValue = "";
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
                Method getMethod = pd.getReadMethod();
                Object value = getMethod.invoke(object);
                if (value != null) {
                    fieldValue = value.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(fieldValue)) {
                continue;
            }
            int valueLength = fieldValue.length();
            int startHideIndex = getHideStartIndex(valueLength);
            int endHideIndex = getHideEndIndex(valueLength);
            try {
                field.set(object, StrUtil.hide(fieldValue, startHideIndex, endHideIndex));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int getHideStartIndex(int totalLength) {
        if (totalLength <= 4) {
            return 1;
        } else if (totalLength >= 5 && totalLength <= 6) {
            return 1;
        } else if (totalLength >= 7 && totalLength <= 10) {
            return 2;
        } else if (totalLength >= 11 && totalLength <= 18) {
            return 3;
        } else if (totalLength >= 19 && totalLength <= 27) {
            return 5;
        } else if (totalLength >= 28 && totalLength <= 34) {
            return 7;
        } else if (totalLength >= 35 && totalLength <= 41) {
            return 9;
        } else {
            return 15;
        }
    }

    private static int getHideEndIndex(int totalLength) {
        if (totalLength <= 4) {
            return totalLength - 1;
        } else if (totalLength >= 5 && totalLength <= 6) {
            return totalLength - 2;
        } else if (totalLength >= 7 && totalLength <= 10) {
            return totalLength - 2;
        } else if (totalLength >= 11 && totalLength <= 18) {
            return totalLength - 4;
        } else if (totalLength >= 19 && totalLength <= 27) {
            return totalLength - 6;
        } else if (totalLength >= 28 && totalLength <= 34) {
            return totalLength - 8;
        } else if (totalLength >= 35 && totalLength <= 41) {
            return totalLength - 10;
        } else {
            return totalLength - 16;
        }
    }


    public static List<Field> getField(Object object) {
        // 从缓存中查询
        Class tClass = object.getClass();
        List<Field> fieldList = fieldMap.get(tClass);
        if (null != fieldList) {
            return fieldList;
        }

        // 这一段递归代码 是为了 从父类中获取属性
        Class tempClass = tClass;
        fieldList = new ArrayList<>();
        while (tempClass != null) {
            Field[] declaredFields = tempClass.getDeclaredFields();
            for (Field field : declaredFields) {
                Boolean stringField = false;
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
                    Method getMethod = pd.getReadMethod();
                    Type returnType = getMethod.getGenericReturnType();
                    stringField = "java.lang.String".equals(returnType.getTypeName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (field.isAnnotationPresent(FieldEncrypt.class) && stringField) {
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            }
            tempClass = tempClass.getSuperclass();
        }
        fieldMap.put(tClass, fieldList);
        return fieldList;
    }


}