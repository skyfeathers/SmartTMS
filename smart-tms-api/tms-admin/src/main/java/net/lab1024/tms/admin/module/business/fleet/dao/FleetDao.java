package net.lab1024.tms.admin.module.business.fleet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.fleet.domain.form.FleetQueryForm;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetDetailVO;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetVO;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/***
 * 车队
 *
 * @author lidoudou
 * @date 2022/6/27 下午3:26
 */
@Mapper
@Component
public interface FleetDao extends BaseMapper<FleetEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<FleetVO> queryByPage(Page page, @Param("queryForm") FleetQueryForm queryDTO);

    /**
     * 查询车队详情
     *
     * @param fleetId
     * @return
     */
    FleetDetailVO getDetail(@Param("fleetId") Long fleetId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 更新删除标识
     *
     * @param fleetIdList
     * @param deletedFlag
     * @param updateUserId
     * @return
     */
    Integer updateDeletedFlagByFleetId(@Param("fleetIdList") List<Long> fleetIdList, @Param("deletedFlag") Boolean deletedFlag, @Param("updateUserId") Long updateUserId);

    /**
     * 根据状态以及ID查询
     *
     * @param fleetIdList
     * @param auditStatus
     * @return
     */
    List<FleetEntity> selectList(@Param("list") List<Long> fleetIdList, @Param("auditStatus") Integer auditStatus);

    /**
     * 批量更新审核状态
     *
     * @param fleetIdList
     * @param auditStatus
     * @param auditRemark
     * @return
     */
    Integer batchUpdateAuditStatus(@Param("list") List<Long> fleetIdList, @Param("auditStatus") Integer auditStatus, @Param("auditRemark") String auditRemark);

    /**
     * 名称查询
     *
     * @param fleetName
     * @param deletedFlag
     * @param excludeIds
     * @return
     */
    List<FleetEntity> selectByNameExcludeIds(@Param("enterpriseId") Long enterpriseId, @Param("fleetName") String fleetName, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);
}
