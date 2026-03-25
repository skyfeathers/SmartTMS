package net.lab1024.tms.common.module.support.datatracer.domain;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/22 10:55 下午
 */
@Data
public class DataTracerDetailVO <T> extends DataTracerVO{


    private T originObject;

    private T newObject;
}