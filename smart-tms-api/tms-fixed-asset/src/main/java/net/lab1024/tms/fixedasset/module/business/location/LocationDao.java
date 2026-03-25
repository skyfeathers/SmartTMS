package net.lab1024.tms.fixedasset.module.business.location;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationQueryForm;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 存放位置管理
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:22
 */
@Mapper
@Component
public interface LocationDao extends BaseMapper<LocationEntity> {

    /**
     * 根据位置名称查询
     *
     * @param locationName
     * @param excludeLocationId
     * @param deletedFlag
     * @return
     */
    LocationEntity queryByLocationName(@Param("locationName") String locationName, @Param("enterpriseId") Long enterpriseId, @Param("excludeLocationId") Long excludeLocationId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除位置
     *
     * @param locationId
     * @param deletedFlag
     */
    void deleteLocation(@Param("locationId") Long locationId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 位置分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<LocationVO> queryPage(Page page, @Param("queryForm") LocationQueryForm queryForm);

    /**
     * 业务类型查询
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<LocationVO> queryList(@Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag, @Param("enterpriseId") Long enterpriseId);

    /**
     * 查询位置详情
     *
     * @param locationId
     * @return
     */
    LocationVO getDetail(@Param("locationId") Long locationId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据ID列表查询位置
     *
     * @param locationIdList
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<LocationEntity> selectByLocationIdList(@Param("locationIdList") Collection<Long> locationIdList, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
