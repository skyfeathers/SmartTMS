package net.lab1024.tms.common.module.support.file.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.file.constant.FileFolderTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * @Description: 文件信息查询
 * @Author: zhuoda
 */
@Data
public class FileQueryForm extends PageParam {

    @ApiModelProperty(value = "文件名称")
    @Length(max = 50, message = "文件名称搜索最多50字符")
    private String fileName;

    @ApiModelPropertyEnum(value = FileFolderTypeEnum.class, desc = "业务类型")
    @CheckEnum(value = FileFolderTypeEnum.class, message = "业务类型错误")
    private Integer folderType;

}
