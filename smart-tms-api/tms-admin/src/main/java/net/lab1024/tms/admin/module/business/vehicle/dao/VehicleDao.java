package net.lab1024.tms.admin.module.business.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.vehicle.domain.form.VehicleQueryForm;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleDetailVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleSimpleVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleVO;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/***
 * 车辆Dao
 *
 * @author lidoudou
 * @date 2022/6/24 下午5:47
 */
@Mapper
@Component
public interface VehicleDao extends BaseMapper<VehicleEntity> {

    /**
     * 车辆数量
     * @param enterpriseId
     * @param deletedFlag
     * @return
     */
    Integer countVehicle(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询挂车关联的车辆的企业id
     *
     * @param bracketId
     * @return
     */
    List<VehicleEntity> selectByBracketId(@Param("bracketId") Long bracketId);

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<VehicleVO> queryByPage(Page page, @Param("queryDTO") VehicleQueryForm queryDTO);

    /**
     * 车辆详情查询
     *
     * @param vehicleId
     * @return
     */
    VehicleDetailVO getDetail(@Param("vehicleId") Long vehicleId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取所有车辆
     *
     * @param deletedFlag
     * @return
     */
    List<VehicleSimpleVO> selectByDisableFlagAndAuditStatus(@Param("enterpriseId")Long enterpriseId, @Param("disableFlag") Boolean disableFlag, @Param("auditStatus") Integer auditStatus, @Param("deletedFlag") Boolean deletedFlag);


    List<VehicleSimpleVO> selectByBusinessModeAndDisableFlag(@Param("enterpriseId") Long enterpriseId,@Param("businessMode") Integer businessMode, @Param("disableFlag") Boolean disableFlag, @Param("deletedFlag") Boolean deletedFlag);
    /**
     * 更新删除标识
     *
     * @param vehicleIdList
     * @param deletedFlag
     * @return
     */
    Integer updateDeletedFlagByVehicleId(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 更新删除标识
     *
     * @param vehicleIdList
     * @param disabledFlag
     * @return
     */
    Integer batchUpdateDisabled(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 根据状态以及ID查询
     *
     * @param vehicleIdList
     * @param auditStatus
     * @param deletedFlag
     * @return
     */
    List<VehicleEntity> selectVehicleList(@Param("list") List<Long> vehicleIdList, @Param("auditStatus") Integer auditStatus, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 批量更新审核状态
     *
     * @param vehicleIdList
     * @param auditStatus
     * @param auditRemark
     * @return
     */
    Integer batchUpdateAuditStatus(@Param("list") List<Long> vehicleIdList, @Param("auditStatus") Integer auditStatus, @Param("auditRemark") String auditRemark);

    /**
     * 名称查询
     *
     * @param vehicleNumber
     * @param deletedFlag
     * @param excludeIds
     * @return
     */
    List<VehicleEntity> selectByNameExcludeIds(@Param("enterpriseId") Long enterpriseId, @Param("vehicleNumber") String vehicleNumber, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据车牌查询车辆
     *
     * @param vehicleNumberList
     * @param deletedFlag
     * @return
     */
    List<VehicleEntity> selectByVehicleNumber(@Param("enterpriseId") Long enterpriseId, @Param("vehicleNumberList") Collection<String> vehicleNumberList, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 更新挂车ID
     *
     * @param vehicleId
     * @param bracketId
     */
    void updateBracketId(@Param("vehicleId") Long vehicleId, @Param("bracketId") Long bracketId);

    /**
     * 根据经营方式查询
     *
     * @param keyword
     * @param vehicleIdList
     * @param deletedFlag
     * @return
     */
    List<VehicleEntity> selectByVehicleIdAndKeyword(@Param("keyword") String keyword,
                                                    @Param("enterpriseId") Long enterpriseId,
                                                    @Param("vehicleIdList") List<Long> vehicleIdList ,
                                             @Param("deletedFlag") Boolean deletedFlag);

}
