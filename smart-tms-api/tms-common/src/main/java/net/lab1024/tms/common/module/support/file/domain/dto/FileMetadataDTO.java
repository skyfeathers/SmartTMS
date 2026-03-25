package net.lab1024.tms.common.module.support.file.domain.dto;

import lombok.Data;

/**
 * 文件元数据 DTO 类
 *
 * @author 胡克
 * @date 2020/1/16 17:35
 */
@Data
public class FileMetadataDTO {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小/字节
     */
    private Long fileSize;

    /**
     * 文件格式
     */
    private String fileFormat;
}
