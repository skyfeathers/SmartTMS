package net.lab1024.tms.common.module.business.mobileapp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.mobileapp.domain.entity.MobileAppEntity;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppQueryForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.vo.MobileAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MobileAppDao extends BaseMapper<MobileAppEntity> {

    /**
     * 查询最新的版本
     *
     * @param platformType
     * @param newestFlag
     * @return
     */
    MobileAppEntity selectByNewestFlag(@Param("platformType") Integer platformType, @Param("newestFlag") Boolean newestFlag);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MobileAppVO> queryPage(Page page, @Param("queryForm") MobileAppQueryForm queryForm);

    /**
     * 更新最新版本标记
     *
     * @param id
     * @param newestFlag
     */
    void updateNewestFlag(@Param("id") Long id, @Param("newestFlag") Boolean newestFlag);

}
