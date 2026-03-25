package net.lab1024.tms.common.module.support.datatracer.domain;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataTracerExtraData {

    /**
     * 对象所属类
     */
    private Class objectClass;

    /**
     * 原对象
     */
    private Object originObject;

    /**
     * 新对象
     */
    private Object newObject;

    public DataTracerExtraData(Class objectClass, Object originObject, Object newObject) {
        this.objectClass = objectClass;
        this.originObject = originObject;
        this.newObject = newObject;
    }
}
