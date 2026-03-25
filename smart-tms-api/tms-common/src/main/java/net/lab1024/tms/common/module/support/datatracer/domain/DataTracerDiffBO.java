package net.lab1024.tms.common.module.support.datatracer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/28 10:43 下午
 */
@Data
@AllArgsConstructor
public class DataTracerDiffBO {

    private String originContent;

    private String newContent;
}