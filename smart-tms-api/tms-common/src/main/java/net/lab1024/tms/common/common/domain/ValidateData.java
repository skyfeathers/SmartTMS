package net.lab1024.tms.common.common.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: 胡克
 * @create: 2020/10/16
 */
@Data
public class ValidateData<T> {

    @NotNull(message = "数据不能为空哦")
    private T data;
}