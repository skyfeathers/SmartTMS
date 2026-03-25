package net.lab1024.tms.admin.module.business.material.cabinet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetQueryForm;
import net.lab1024.tms.admin.module.business.material.cabinet.domain.CabinetVO;
import net.lab1024.tms.common.module.business.material.cabinet.CabinetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component
public interface CabinetDao extends BaseMapper<CabinetEntity> {

    /**
     * 根据柜型名称查询
     *
     * @param cabinetName
     * @param excludeCabinetId
     * @param deletedFlag
     * @return
     */
    CabinetEntity queryByCabinetName(@Param("enterpriseId") Long enterpriseId, @Param("cabinetName") String cabinetName, @Param("excludeCabinetId") Long excludeCabinetId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除柜型
     *
     * @param cabinetId
     * @param deletedFlag
     */
    void deleteCabinet(@Param("cabinetId") Long cabinetId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 柜型分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CabinetVO> queryPage(Page page, @Param("queryForm") CabinetQueryForm queryForm);

    /**
     * 业务类型查询
     *
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<CabinetVO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 查询柜型详情
     *
     * @param cabinetId
     * @return
     */
    CabinetVO getDetail(@Param("cabinetId") Long cabinetId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询默认柜型
     *
     * @param defaultFlag
     * @param deletedFlag
     * @param excludeCabinetId
     * @return
     */
    List<CabinetEntity> queryByDefault(@Param("enterpriseId") Long enterpriseId, @Param("defaultFlag") Boolean defaultFlag, @Param("deletedFlag") Boolean deletedFlag, @Param("excludeCabinetId") Long excludeCabinetId);

    /**
     * 根据名称查询柜型
     *
     * @param cabinetNameList
     * @param deletedFlag
     * @param disabledFlag
     * @return
     */
    List<CabinetEntity> selectByCabinetNameList(@Param("enterpriseId") Long enterpriseId, @Param("cabinetNameList") Collection<String> cabinetNameList, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);
}
