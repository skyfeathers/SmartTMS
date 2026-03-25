package net.lab1024.tms.driver.module.business.bracket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Component
public interface BracketDao extends BaseMapper<BracketEntity> {

    /**
     * 挂车分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<BracketEntity> queryByPage(Page page, @Param("queryForm") BracketQueryForm queryForm);

    /**
     * 根据挂车车牌号查询挂车，排除部分ID
     *
     * @param bracketNo
     * @param excludeIds
     * @param deletedFlag
     * @return
     */
    BracketEntity selectByNoExcludeIds(@Param("bracketNo") String bracketNo, @Param("excludeIds") ArrayList<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

}
