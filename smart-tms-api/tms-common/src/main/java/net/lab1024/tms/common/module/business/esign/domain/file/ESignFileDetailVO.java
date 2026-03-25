package net.lab1024.tms.common.module.business.esign.domain.file;

import lombok.Data;

/**
 * 查询PDF文件详情
 *
 * @author lihaifan
 * @date 2022/7/18 20:30
 */
@Data
public class ESignFileDetailVO {

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小（预留字段，暂时不会返回任何值，开发者可忽略）
     */
    private Integer size;

    /**
     * 文件上传状态
     *
     * 0-文件未上传；
     *
     * 1-文件上传中 ；
     *
     * 2-文件上传已完成；
     *
     * 3-文件上传失败 ；
     *
     * 4-文件等待转pdf ；
     *
     * 5-文件已转换pdf ；
     *
     * 6-加水印中；
     *
     * 7-加水印完毕；
     *
     * 8-文件转换中；
     *
     * 9-文件转换失败
     */
    private Integer status;

    /**
     * 下载地址，一般有效期为1个小时
     */
    private String downloadUrl;

    /**
     * pdf文件总页数,仅当文件类型为pdf时有值
     */
    private Integer pdfTotalPages;
}
