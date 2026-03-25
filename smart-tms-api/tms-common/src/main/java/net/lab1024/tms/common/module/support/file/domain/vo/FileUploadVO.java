package net.lab1024.tms.common.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Data
public class FileUploadVO {

    @ApiModelProperty(value = "文件id")
    private Long fileId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "fileUrl")
    private String fileUrl;

    @ApiModelProperty(value = "fileKey")
    private String fileKey;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
