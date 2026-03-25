package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperAddQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.ShipperCountMonthVO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperBasicDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperQueryForm;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperDetailVO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperListVO;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:47
 */
@Mapper
@Component
public interface ShipperDao extends BaseMapper<ShipperEntity> {

    /**
     * 货主详情
     *
     * @param shipperId
     * @return
     */
    ShipperDetailVO detailById(@Param("shipperId") Long shipperId);

    /**
     * 获取基本信息
     *
     * @param shipperId
     * @return
     */
    ShipperBasicDTO selectBasicById(@Param("shipperId") Long shipperId);

    /**
     * 获取一批货主的基本信息
     *
     * @param shipperIdList
     * @return
     */
    List<ShipperBasicDTO> selectBasicByIdList(@Param("shipperIdList") Collection<Long> shipperIdList);
    /**
     * 名称查询
     *
     * @param consignor
     * @param deletedFlag
     * @return
     */
    List<ShipperBasicDTO> selectByName(@Param("consignor") String consignor, @Param("deletedFlag") Boolean deletedFlag, @Param("enterpriseId") Long enterpriseId);

    /**
     * 查询货主基本信息
     *
     * @param shipperId
     * @return
     */
    ShipperBasicDTO selectSimpleById(@Param("shipperId") Long shipperId);

    /**
     * 名称查询
     *
     * @param consignor
     * @param deletedFlag
     * @return
     */
    List<ShipperBasicDTO> selectByNameExcludeIds(@Param("consignor") String consignor, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag, @Param("enterpriseId") Long enterpriseId);

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<ShipperListVO> queryByPage(Page page, @Param("queryDTO") ShipperQueryForm queryDTO);

    /**
     * 查询所有货主基本信息
     *
     * @param deletedFlag
     * @return
     */
    List<ShipperBasicDTO> queryList(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param shipperIdList
     * @param deletedFlag
     */
    void updateDeletedFlagById(@Param("shipperIdList") List<Long> shipperIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 更新货主名称
     *
     * @param id
     * @param name
     */
    void updateNameById(@Param("shipperId") Long id, @Param("name") String name);

    /**
     * 修改负责人
     *
     * @param shipperId
     * @param managerId
     */
    void updateManager(@Param("shipperId") Long shipperId, @Param("managerId") Long managerId);

    /**
     * 根据状态以及ID查询
     *
     * @param shipperIdList
     * @param auditStatus
     * @param deletedFlag
     * @return
     */
    List<ShipperEntity> selectShipperList(@Param("list") List<Long> shipperIdList, @Param("auditStatus") Integer auditStatus, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 批量更新审核状态
     *
     * @param shipperIdList
     * @param auditStatus
     * @param auditRemark
     * @return
     */
    Integer batchUpdateAuditStatus(@Param("list") List<Long> shipperIdList, @Param("auditStatus") Integer auditStatus, @Param("auditRemark") String auditRemark);


    /**
     * 根据月份统计
     *
     * @param addQueryForm
     * @param deletedFlag
     * @return
     */
    List<ShipperCountMonthVO> countByMonth(@Param("queryForm") ShipperAddQueryForm addQueryForm, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据简称查询货主信息
     *
     * @param shipperNameList
     * @return
     */
    List<ShipperEntity> selectByConsignorListAndEnterpriseId(@Param("shipperNameList")Set<String> shipperNameList, @Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);

}
