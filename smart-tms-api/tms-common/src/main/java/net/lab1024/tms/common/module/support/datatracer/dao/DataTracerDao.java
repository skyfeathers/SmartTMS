package net.lab1024.tms.common.module.support.datatracer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerQueryForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 21:57
 */
@Mapper
@Component
public interface DataTracerDao extends BaseMapper<DataTracerEntity> {

    /**
     * 操作记录查询
     * @param businessId
     * @param businessType
     * @return
     */
    List<DataTracerVO> selectRecord(@Param("businessId") Long businessId, @Param("businessType") Integer businessType);

    /**
     * 分页查询
     * @param page
     * @param queryForm
     * @return
     */
    List<DataTracerVO> query(Page page, @Param("query") DataTracerQueryForm queryForm);
}
