package net.lab1024.tms.common.module.support.file.domain.dto;

import lombok.Data;

/**
 * 文件下载DTO类
 *
 * @author 胡克
 * @date 2020/2/4 15:30
 */
@Data
public class FileDownloadDTO {

    /**
     * 文件字节数据
     */
    private byte[] data;

    /**
     * 文件元数据
     */
    private FileMetadataDTO metadata;


}
