package net.lab1024.tms.common.module.business.esign.domain.file;

import lombok.Data;

/**
 * 查询文件上传状态
 *
 * @author lihaifan
 * @date 2022/7/18 17:41
 */
@Data
public class ESignFileStatusVO {

    /**
     * 文件状态
     *
     * 0 -文件未上传；
     *
     * 1 -文件上传中 ；
     *
     * 2 -文件上传已完成；
     *
     * 3 -文件上传失败 ；
     *
     * 4 -文件等待转pdf ；
     *
     * 5 -文件已转换pdf ；
     *
     * 6 -加水印中；
     *
     * 7 -加水印完毕；
     *
     * 8 -文件转换中；
     *
     * 9 -文件转换失败
     */
    private int status;
}
