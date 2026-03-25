package net.lab1024.tms.common.module.business.oa.news.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 资讯 推送类型
 *
 * @author zhaoxinyang
 * @date 2023/08/28 15:56
 */
@Getter
@AllArgsConstructor
public enum NoticePushTypeEnum implements BaseEnum {
    
    PLATFORM_PUSH(1, "平台推送"),
    
    DRIVER_PUSH(2, "司机推送"),
    
    ;
    
    private final Integer value;
    
    private final String desc;
    
}
