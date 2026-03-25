package net.lab1024.tms.admin.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverNoticeForm;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverQueryForm;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverDetailVO;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverSelectVO;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverVO;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/***
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:00
 */
@Mapper
@Component
public interface DriverDao extends BaseMapper<DriverEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<DriverVO> queryByPage(Page page, @Param("queryForm") DriverQueryForm queryForm);

    /**
     * 司机详情
     *
     * @param driverId
     * @return
     */
    DriverDetailVO getDetail(@Param("driverId") Long driverId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 司机数量
     * @param deletedFlag
     * @return
     */
    Integer countDriver(@Param("enterpriseId") Long enterpriseId, @Param("deletedFlag") Boolean deletedFlag);
    /**
     * 更新删除标识
     *
     * @param driverIdList
     * @param deletedFlag
     * @param updateUserId
     * @return
     */
    Integer updateDeletedFlagByDriverId(@Param("driverIdList") List<Long> driverIdList, @Param("deletedFlag") Boolean deletedFlag, @Param("updateUserId") Long updateUserId);

    /**
     * 根据状态以及ID查询
     *
     * @param driverIdList
     * @param auditStatus
     * @param status
     * @param deletedFlag
     * @return
     */
    List<DriverEntity> selectList(@Param("list") List<Long> driverIdList, @Param("auditStatus") Integer auditStatus, @Param("status") Integer status, @Param("deletedFlag") Boolean deletedFlag);


    List<DriverNoticeForm> queryExpireList(@Param("columnName") String test);

    /**
     * 司机下拉框查询
     *
     * @param auditStatus 可为空
     * @param status
     * @param deletedFlag
     * @return
     */
    List<DriverSelectVO> queryDriverSelect(@Param("enterpriseId") Long enterpriseId, @Param("auditStatus") Integer auditStatus, @Param("status") Integer status, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据手机号查询司机
     *
     * @param phone
     * @return
     */
    List<DriverEntity> selectByPhoneExcludeIds(@Param("enterpriseId") Long enterpriseId, @Param("phone") String phone, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

    List<DriverEntity> selectByName(@Param("enterpriseId") Long enterpriseId, @Param("driverName") String driverName, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 根据手机号查询司机
     *
     * @param phoneList
     * @param deletedFlag
     * @return
     */
    List<DriverEntity> selectByPhone(@Param("enterpriseId") Long enterpriseId, @Param("phoneList") Collection<String> phoneList, @Param("deletedFlag") Boolean deletedFlag);

}
