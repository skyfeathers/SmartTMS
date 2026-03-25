package net.lab1024.tms.common.module.business.pic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.pic.domain.entity.PicEntity;
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
public interface CommonPicDao extends BaseMapper<PicEntity> {

    /**
     * 根据投放类型以及位置查询
     *
     * @param type
     * @param enableFlag
     * @return
     */
    List<PicEntity> selectByType(@Param("enterpriseId") Long enterpriseId, @Param("type") Integer type, @Param("enableFlag") Boolean enableFlag);


}
