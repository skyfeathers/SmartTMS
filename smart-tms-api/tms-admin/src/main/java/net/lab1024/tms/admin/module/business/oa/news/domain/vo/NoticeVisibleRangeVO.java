package net.lab1024.tms.admin.module.business.oa.news.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticeVisibleRangeDataTypeEnum;

/**
 * 新闻、公告 可见范围数据 VO
 *
 * @author zhuoda
 * @date 2022/08/12 21:34
 */
@Data
public class NoticeVisibleRangeVO {

    @ApiModelPropertyEnum(NoticeVisibleRangeDataTypeEnum.class)
    private Integer dataType;

    @ApiModelProperty("员工/部门id")
    private Long dataId;

    @ApiModelProperty("员工/部门 名称")
    private String dataName;

}
