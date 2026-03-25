package net.lab1024.tms.admin.module.business.material.transportroute;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.form.TransportRouteQueryForm;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.vo.TransportRouteVO;
import net.lab1024.tms.common.module.business.material.transportroute.domain.TransportRouteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 业务资料-运输路线管理
 *
 * @author lidoudou
 * @date 2022/7/12 下午3:09
 */
@Mapper
@Component
public interface TransportRouteDao extends BaseMapper<TransportRouteEntity> {

    /**
     * 删除运输路线
     *
     * @param transportRouteId
     * @param deletedFlag
     */
    void deleteTransportRoute(@Param("transportRouteId") Long transportRouteId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 运输路线分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<TransportRouteVO> queryPage(Page page, @Param("queryForm") TransportRouteQueryForm queryForm);

    /**
     * 查询运输路线详情
     *
     * @param transportRouteId
     * @return
     */
    TransportRouteVO getDetail(@Param("transportRouteId") Long transportRouteId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询不分页的列表
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<TransportRouteVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 根据名称查询柜型
     *
     * @param transportRouteNameList
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<TransportRouteEntity> selectByTransportNameList(@Param("enterpriseId") Long enterpriseId, @Param("transportRouteNameList") Collection<String> transportRouteNameList, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);

}
