package net.lab1024.tms.common.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.support.file.constant.FileFolderTypeEnum;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Data
public class FileVO {

    @ApiModelProperty("主键")
    private Long fileId;

    @ApiModelProperty("存储文件夹类型")
    @ApiModelPropertyEnum(FileFolderTypeEnum.class)
    private Integer folderType;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件路径")
    private String fileKey;

    @ApiModelProperty("上传人")
    private Long creatorId;

    @ApiModelProperty("上传人")
    private String creatorName;

    @ApiModelProperty("文件展示url")
    private String fileUrl;
}
