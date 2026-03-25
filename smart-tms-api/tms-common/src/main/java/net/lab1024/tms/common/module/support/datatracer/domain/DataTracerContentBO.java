package net.lab1024.tms.common.module.support.datatracer.domain;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataTracerContentBO {

    private Field field;

    private Object fieldValue;

    private String fieldDesc;

    private String fieldContent;

}
