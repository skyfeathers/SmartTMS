package net.lab1024.tms.common.module.support.serialnumber.service;

import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */
public interface SerialNumberService {

    /**
     * 生成
     *
     * @param serialNumberIdEnum
     * @return
     */
    String generate(SerialNumberIdEnum serialNumberIdEnum);


    /**
     * 生成n个
     *
     * @param serialNumberIdEnum
     * @param count
     * @return
     */
    List<String> generate(SerialNumberIdEnum serialNumberIdEnum, int count);

}
