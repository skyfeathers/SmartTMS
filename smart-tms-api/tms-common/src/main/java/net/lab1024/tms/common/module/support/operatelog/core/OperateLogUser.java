package net.lab1024.tms.common.module.support.operatelog.core;

import lombok.Data;

/**
* @Description:    用户信息
* @Author:         zhuoda
*/
@Data
public class OperateLogUser {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展信息
     */
    private String extData;

}
