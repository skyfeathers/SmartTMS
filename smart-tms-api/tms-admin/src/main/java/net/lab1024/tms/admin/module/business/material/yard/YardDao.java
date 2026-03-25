package net.lab1024.tms.admin.module.business.material.yard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardQueryForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardVO;
import net.lab1024.tms.common.module.business.material.yard.YardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务资料-堆场管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component
public interface YardDao extends BaseMapper<YardEntity> {

    /**
     * 根据堆场编号查询
     *
     * @param yardCode
     * @param excludeYardId
     * @param deletedFlag
     * @return
     */
    YardEntity queryByYardCode(@Param("enterpriseId") Long enterpriseId, @Param("yardCode") String yardCode, @Param("excludeYardId") Long excludeYardId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除堆场
     *
     * @param yardId
     * @param deletedFlag
     */
    void deleteYard(@Param("yardId") Long yardId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 堆场分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<YardVO> queryPage(Page page, @Param("queryForm") YardQueryForm queryForm);

    /**
     * 查询堆场详情
     *
     * @param yardId
     * @return
     */
    YardVO getDetail(@Param("yardId") Long yardId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 列表查询
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<YardVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
