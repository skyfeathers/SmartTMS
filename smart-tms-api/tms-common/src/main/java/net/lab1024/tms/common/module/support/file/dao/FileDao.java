package net.lab1024.tms.common.module.support.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.support.file.domain.entity.FileEntity;
import net.lab1024.tms.common.module.support.file.domain.form.FileQueryForm;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 胡克
 * @date 2018-01-05 上午 9:49
 */
@Mapper
@Component
public interface FileDao extends BaseMapper<FileEntity> {

    /**
     * 文件key单个查询
     *
     * @param fileKey
     * @return
     */
    FileVO getByFileKey(@Param("fileKey") String fileKey);

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    List<FileVO> queryListByPage(Page page, @Param("query") FileQueryForm query);

}
