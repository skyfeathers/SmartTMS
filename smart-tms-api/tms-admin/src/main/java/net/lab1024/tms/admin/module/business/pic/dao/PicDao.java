package net.lab1024.tms.admin.module.business.pic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicQueryForm;
import net.lab1024.tms.common.module.business.pic.domain.entity.PicEntity;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */

@Mapper
@Component
public interface PicDao extends BaseMapper<PicEntity> {

    /**
     * 更新排序
     *
     * @param picId
     * @param seq
     */
    void updateSeq(@Param("picId") Long picId, @Param("seq") Integer seq);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<PicVO> query(Page page, @Param("queryForm") PicQueryForm queryForm);

}
