package net.lab1024.tms.common.module.support.file.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.SystemErrorCode;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.RedisKeyConst;
import net.lab1024.tms.common.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.tms.common.module.support.file.dao.FileDao;
import net.lab1024.tms.common.module.support.file.domain.dto.FileDownloadDTO;
import net.lab1024.tms.common.module.support.file.domain.dto.FileMetadataDTO;
import net.lab1024.tms.common.module.support.file.domain.entity.FileEntity;
import net.lab1024.tms.common.module.support.file.domain.form.FileQueryForm;
import net.lab1024.tms.common.module.support.file.domain.form.FileUrlUploadForm;
import net.lab1024.tms.common.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;
import net.lab1024.tms.common.module.support.redis.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Service
public class FileService {

    /**
     * 文件名最大长度
     */
    private static final int FILE_NAME_MAX_LENGTH = 100;

    @Resource
    private IFileStorageService fileStorageService;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private RedisService redisService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * 文件上传服务：通过 url 上传
     *
     * @param urlUploadForm
     * @return
     */
    public ResponseDTO<FileUploadVO> fileUpload(FileUrlUploadForm urlUploadForm) {
        try {
            URL url = new URL(urlUploadForm.getFileUrl());
            URLConnection urlConnection = url.openConnection();
            // 获取文件格式
            String contentType = urlConnection.getContentType();
            String fileType = fileStorageService.getFileTypeByContentType(contentType);
            // 生成文件key
            String fileKey = fileStorageService.generateFileNameByType(fileType);
            MockMultipartFile file = new MockMultipartFile(fileKey, fileKey, contentType, urlConnection.getInputStream());
            return this.fileUpload(file, urlUploadForm.getFolder(), urlUploadForm.getUserId(), urlUploadForm.getUserName());
        } catch (IOException e) {
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "上传失败");
        }
    }

    /**
     * 文件上传服务
     *
     * @param file
     * @param folderType 文件夹类型
     * @return
     */
    public ResponseDTO<FileUploadVO> fileUpload(MultipartFile file, Integer folderType, Long userId, String userName) {
        FileFolderTypeEnum folderTypeEnum = SmartBaseEnumUtil.getEnumByValue(folderType, FileFolderTypeEnum.class);
        if (null == folderTypeEnum) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "文件夹错误");
        }
        if (null == file || file.getSize() == 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "上传文件不能为空");
        }
        // 校验文件名称
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename) || originalFilename.length() > FILE_NAME_MAX_LENGTH) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "上传文件名称不能为空");
        }
        // 校验文件大小
        String maxSizeStr = maxFileSize.toLowerCase().replace("mb", "");
        long maxSize = Integer.parseInt(maxSizeStr) * 1024 * 1024L;
        if (file.getSize() > maxSize) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "上传文件最大:" + maxSize);
        }
        // 获取文件服务
        ResponseDTO<FileUploadVO> response = fileStorageService.fileUpload(file, folderTypeEnum.getFolder());
        if (!response.getOk()) {
            return response;
        }

        // 上传成功 保存记录数据库
        FileUploadVO uploadVO = response.getData();

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderType(folderTypeEnum.getValue());
        fileEntity.setFileName(originalFilename);
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileKey(uploadVO.getFileKey());
        fileEntity.setFileType(uploadVO.getFileType());
        fileEntity.setCreatorId(userId);
        fileEntity.setCreatorName(userName);
        fileDao.insert(fileEntity);
        uploadVO.setFileId(fileEntity.getFileId());
        // 添加缓存
        String redisKey = redisService.generateRedisKey(RedisKeyConst.Support.FILE_URL, uploadVO.getFileKey());
        redisService.set(redisKey, uploadVO.getFileUrl(), fileStorageService.cacheExpireSecond());

        String fileRedisKey = redisService.generateRedisKey(RedisKeyConst.Support.FILE_VO, uploadVO.getFileKey());
        FileVO fileVO = SmartBeanUtil.copy(fileEntity, FileVO.class);
        redisService.set(fileRedisKey, fileVO, fileStorageService.cacheExpireSecond());
        return response;
    }

    public List<FileVO> getFileList(List<String> fileKeyList) {
        if (CollectionUtils.isEmpty(fileKeyList)) {
            return Lists.newArrayList();
        }
        return fileKeyList.stream().map(this::getCacheFileVO).filter(e->e != null).collect(Collectors.toList());
    }

    public FileVO getCacheFileVO(String fileKey) {
        String redisKey = redisService.generateRedisKey(RedisKeyConst.Support.FILE_VO, fileKey);
        FileVO fileVO = redisService.getObject(redisKey, FileVO.class);
        if (fileVO == null) {
            fileVO = fileDao.getByFileKey(fileKey);
            if (fileVO == null) {
                return null;
            }
            redisService.set(redisKey, fileVO, fileStorageService.cacheExpireSecond());
        }
        fileVO.setFileUrl(this.getCacheUrl(fileKey));
        return fileVO;
    }

    /**
     * 根据文件绝对路径 获取文件URL
     * 支持单个 key 逗号分隔的形式
     *
     * @param fileKey
     * @return
     */
    public ResponseDTO<String> getFileUrl(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        // 处理逗号分隔的字符串
        String keyList = StrUtil.split(fileKey, StringConst.SEPARATOR)
                                        .stream().map(this::getCacheUrl)
                                        .collect(Collectors.joining(StringConst.SEPARATOR));
        return ResponseDTO.ok(keyList);
    }


    public String getCacheUrl(String fileKey) {
        String redisKey = redisService.generateRedisKey(RedisKeyConst.Support.FILE_URL, fileKey);
        String fileUrl = redisService.get(redisKey);
        if (null != fileUrl) {
            return fileUrl;
        }
        ResponseDTO<String> responseDTO = fileStorageService.getFileUrl(fileKey);
        if (!responseDTO.getOk()) {
            return null;
        }
        fileUrl = responseDTO.getData();
        redisService.set(redisKey, fileUrl, fileStorageService.cacheExpireSecond());
        return fileUrl;
    }

    /**
     * 分页查询文件列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<FileVO>> queryListByPage(FileQueryForm queryDTO) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<FileVO> fileList = fileDao.queryListByPage(page, queryDTO);
        if (CollectionUtils.isNotEmpty(fileList)) {
            fileList.forEach(e -> {
                // 根据文件服务类 获取对应文件服务 查询 url
                e.setFileUrl(fileStorageService.getFileUrl(e.getFileKey()).getData());
            });
        }
        PageResult<FileVO> pageResult = SmartPageUtil.convert2PageResult(page, fileList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 根据文件服务类型 和 FileKey 下载文件
     *
     * @param fileKey
     * @return
     * @throws IOException
     */
    public ResponseEntity<Object> downloadByFileKey(String fileKey, String userAgent) {
        // 根据文件服务类 获取对应文件服务 查询 url
        ResponseDTO<FileDownloadDTO> responseDTO = fileStorageService.fileDownload(fileKey);
        if (!responseDTO.getOk()) {
            HttpHeaders heads = new HttpHeaders();
            heads.add(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
            return new ResponseEntity<>(responseDTO.getMsg() + "：" + fileKey, heads, HttpStatus.OK);
        }
        // 设置下载头
        HttpHeaders heads = new HttpHeaders();
        heads.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream; charset=utf-8");
        // 设置对应浏览器的文件名称编码
        FileDownloadDTO fileDownloadDTO = responseDTO.getData();
        FileMetadataDTO metadata = fileDownloadDTO.getMetadata();
        String fileName = null != metadata ? metadata.getFileName() : fileKey.substring(fileKey.lastIndexOf("/"));
        fileName = fileStorageService.getDownloadFileNameByUA(fileName, userAgent);
        heads.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(fileDownloadDTO.getData(), heads, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 根据文件key 删除
     *
     * @param fileKey
     * @return
     */
    public ResponseDTO<String> deleteByFileKey(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileKey(fileKey);
        fileEntity = fileDao.selectOne(new QueryWrapper<>(fileEntity));
        if (null == fileEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 根据文件服务类 获取对应文件服务 删除文件
        return fileStorageService.delete(fileKey);
    }
}
