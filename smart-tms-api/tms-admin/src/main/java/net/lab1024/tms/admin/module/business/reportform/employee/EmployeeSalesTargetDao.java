package net.lab1024.tms.admin.module.business.reportform.employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeSalesTargetAmountBO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.entity.EmployeeSalesTargetEntity;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.EmployeeSalesTargetQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.SalesDayStatisticQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 员工 业绩目标 dao
 *
 * @author Turbolisten
 * @date 2021/8/14 11:26
 */
@Mapper
@Component
public interface EmployeeSalesTargetDao extends BaseMapper<EmployeeSalesTargetEntity> {

    /**
     * 根据 员工 年份 查询数据
     *
     * @param employeeId
     * @param year
     * @return
     */
    List<EmployeeSalesTargetEntity> listByYear(@Param("employeeId") Long employeeId,
                                               @Param("year") Integer year);

    /**
     * 查找某人某月
     * @param employeeId
     * @param year
     * @param month
     * @return
     */
    EmployeeSalesTargetEntity selectByYearMonth(@Param("employeeId") Long employeeId,
                                                @Param("year") Integer year,
                                                @Param("month") Integer month);

    /**
     * 查询 员工业绩目标
     *
     * @param queryForm
     * @return
     */
    List<EmployeeSalesTargetEntity> query(@Param("query") EmployeeSalesTargetQueryForm queryForm);


    /**
     * 查找某些人某月到某月的业绩目标
     * @param employeeIdList
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    List<EmployeeSalesTargetAmountBO> queryBetweenYearMonth(@Param("employeeIdList") List<Long> employeeIdList,
                                                            @Param("startYear") Integer startYear,
                                                            @Param("startMonth") Integer startMonth,
                                                            @Param("endYear") Integer endYear,
                                                            @Param("endMonth") Integer endMonth);

    /**
     * 查询月指标
     *
     * @param queryForm
     * @return
     */
    List<EmployeeSalesTargetEntity> querySalesTarget(@Param("queryForm") SalesDayStatisticQueryForm queryForm);

}
