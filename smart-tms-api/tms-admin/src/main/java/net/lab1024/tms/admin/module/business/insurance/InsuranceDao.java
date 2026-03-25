package net.lab1024.tms.admin.module.business.insurance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.insurance.domain.form.InsuranceQueryForm;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.common.module.business.insurance.domain.InsuranceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 保险 管理
 *
 * @author lidoudou
 * @date 2022/6/21 下午2:59
 */
@Mapper
@Component
public interface InsuranceDao extends BaseMapper<InsuranceEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<InsuranceVO> queryByPage(Page page, @Param("queryDTO") InsuranceQueryForm queryDTO);

    /**
     * 根据保险对象ID、类型、删除标识、排除ID统计条数
     *
     * @param moduleId
     * @param moduleType
     * @param deletedFlag
     * @param exclude
     * @return
     */
    Integer countByModuleIdAndTypeAndDeletedFlag(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("deletedFlag") Boolean deletedFlag, @Param("excludeId") Long exclude);

    /**
     * 根据模块ID以及模块类型删除保险 真的删除
     *
     * @param moduleId
     * @param moduleType
     */
    void deleteByModuleIdAndType(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);

    /**
     * 根据模块ID以及模块类型更新删除标识
     *
     * @param moduleIdList
     * @param moduleType
     * @param deletedFlag
     * @param requestUsrId
     */
    void updateDeletedFlagByModuleIdAndType(@Param("moduleIdList") List<Long> moduleIdList, @Param("moduleType") Integer moduleType, @Param("requestUsrId") Long requestUsrId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据类型查询保险列表
     *
     * @param moduleId
     * @param moduleType
     */
    List<InsuranceEntity> selectByModuleIdAndType(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);

    /**
     * 根据类型查询保险列表
     *
     * @param moduleIdList
     * @param moduleType
     */
    List<InsuranceEntity> selectByModuleIdListAndType(@Param("moduleIdList") List<Long> moduleIdList, @Param("moduleType") Integer moduleType);

    /**
     * 根据自有车统计金额
     *
     * @param queryForm
     * @param moduleType
     * @return
     */
    List<CarCostVehicleMonthAmountDTO> sumByModuleIdListAndType(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm, @Param("moduleType") Integer moduleType);

}
