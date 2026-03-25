package net.lab1024.tms.driver.constant;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 司机通用配置
 *
 * @author lihaifan
 * @date 2022/11/15 19:13
 */
public class DriverCommonConst {

    /**
     * 自有注册 默认创建人
     */
    public static final Long DEFAULT_CREATE_ID = NumberUtils.LONG_ZERO;

    /**
     * 自由注册 默认创建人
     */
    public static final String DEFAULT_CREATE_NAME = "司机注册";

    /**
     * 随机昵称后的 数字长度
     */
    public static final int NICKNAME_RANDOM_NUM_LEN = 6;
}
