package net.lab1024.tms.driver.module.business.oa.notice.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticePushTypeEnum;

/**
 * 通知公告 分页查询
 *
 * @author: zhuoda
 * @date: 2022/08/12 20:24
 */
@Data
public class NoticeQueryForm extends PageParam {

    @ApiModelProperty("标题、作者、来源、文号")
    private String keywords;

    @ApiModelProperty("删除标识")
    private Boolean deletedFlag;

    @ApiModelPropertyEnum(value = NoticePushTypeEnum.class, desc = "推送分类")
    private Integer noticePushType;
    
}

