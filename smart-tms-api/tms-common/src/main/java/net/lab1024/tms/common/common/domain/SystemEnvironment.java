package net.lab1024.tms.common.common.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.SystemEnvironmentEnum;

/**
 * 系统环境
 *
 * @author zhuoda
 * @Date 2021/8/13
 */
@AllArgsConstructor
@Getter
public class SystemEnvironment {

    /**
     * 是否位生产环境
     */
    private boolean isProd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * redis 项目名称
     */
    private String redisProjectName;

    /**
     * 当前环境
     */
    private SystemEnvironmentEnum currentEnvironment;
}
