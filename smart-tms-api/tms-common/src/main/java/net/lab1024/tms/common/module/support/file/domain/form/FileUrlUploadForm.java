package net.lab1024.tms.common.module.support.file.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.support.file.constant.FileFolderTypeEnum;

import javax.validation.constraints.NotBlank;

/**
 * url上传文件 DTO 类
 *
 * @author 胡克
 * @date 2020年3月16日 10:14:54
 */
@Data
public class FileUrlUploadForm {

    @ApiModelPropertyEnum(value = FileFolderTypeEnum.class, desc = "业务类型")
    @CheckEnum(value = FileFolderTypeEnum.class, required = true, message = "业务类型错误")
    private Integer folder;

    @ApiModelProperty("文件url")
    @NotBlank(message = "文件url不能为空")
    private String fileUrl;

    @ApiModelProperty("用户id|可选")
    private Long userId;

    @ApiModelProperty("用户姓名|可选")
    private String userName;

}
