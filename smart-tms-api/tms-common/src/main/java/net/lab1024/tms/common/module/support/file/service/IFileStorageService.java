package net.lab1024.tms.common.module.support.file.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.file.domain.dto.FileDownloadDTO;
import net.lab1024.tms.common.module.support.file.domain.vo.FileUploadVO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
public interface IFileStorageService {

    /**
     * 文件上传
     *
     * @param file
     * @param path
     * @return
     */
    ResponseDTO<FileUploadVO> fileUpload(MultipartFile file, String path);

    /**
     * 获取文件url
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> getFileUrl(String fileKey);

    /**
     * 流式下载（名称为原文件）
     *
     * @param key
     * @return
     */
    ResponseDTO<FileDownloadDTO> fileDownload(String key);

    /**
     * 单个删除文件
     * 根据文件key删除
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> delete(String fileKey);


    /**
     * 缓存过期秒数
     *
     * @return
     */
    default Long cacheExpireSecond() {
        return 3600L;
    }

    /**
     * 生成文件名字
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param originalFileName
     * @return String
     */
    default String generateFileName(String originalFileName) {
        return generateFileNameByType(FilenameUtils.getExtension(originalFileName));
    }

    /**
     * 根据文件类型 生成文件名
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param fileType
     * @return
     */
    default String generateFileNameByType(String fileType) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTimeUtil.formatNormal(LocalDateTime.now()).replaceAll("-","").replaceAll(" ","").replaceAll(":","");
        return uuid + time + "." + fileType;
    }

    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("pdf".equalsIgnoreCase(fileExt)) {
            return "application/pdf";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

    /**
     * 获取文件格式 根据 content-type
     *
     * @param contentType
     * @return
     */
    default String getFileTypeByContentType(String contentType) {
        // 文件的后缀名
        if ("image/bmp".equalsIgnoreCase(contentType)) {
            return "bmp";
        }
        if ("image/gif".equalsIgnoreCase(contentType)) {
            return "gif";
        }
        if ("image/jpeg".equalsIgnoreCase(contentType) || "image/jpg".equalsIgnoreCase(contentType)) {
            return "jpg";
        }
        if ("image/png".equalsIgnoreCase(contentType)) {
            return "png";
        }
        if ("text/html".equalsIgnoreCase(contentType)) {
            return "html";
        }
        if ("text/plain".equalsIgnoreCase(contentType)) {
            return "txt";
        }
        if ("application/vnd.visio".equalsIgnoreCase(contentType)) {
            return "vsd";
        }
        if ("application/vnd.ms-powerpoint".equalsIgnoreCase(contentType)) {
            return "pptx";
        }
        if ("application/msword".equalsIgnoreCase(contentType)) {
            return "docx";
        }
        if ("application/pdf".equalsIgnoreCase(contentType)) {
            return "pdf";
        }
        if ("text/xml".equalsIgnoreCase(contentType)) {
            return "xml";
        }
        return "";
    }

    /**
     * 根据不同的浏览器 返回对应编码的文件名称
     *
     * @param fileName
     * @param userAgent
     * @return
     */
    default String getDownloadFileNameByUA(String fileName, String userAgent) {
        try {
            userAgent = userAgent.toUpperCase();
            if (userAgent.indexOf("MSIE") > 0) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            } else if (userAgent.indexOf("EDGE") > 0) {
                // WIN10浏览器
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            } else {
                // 其他
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return fileName;
    }
}
