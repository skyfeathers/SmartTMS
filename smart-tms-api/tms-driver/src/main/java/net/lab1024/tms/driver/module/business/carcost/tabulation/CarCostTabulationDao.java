package net.lab1024.tms.driver.module.business.carcost.tabulation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.domain.CarCostTabulationQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostTabulationDao extends BaseMapper<CarCostTabulationEntity> {

    /**
     * 更新金额
     *
     * @param moduleId
     * @param moduleType
     * @param amount
     */
    void updateAmount(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("amount") BigDecimal amount);

    /**
     * 更新金额和分类ID
     *
     * @param moduleId
     * @param moduleType
     * @param amount
     */
    void updateAmountAndCategory(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("amount") BigDecimal amount, @Param("categoryId") Long categoryId);

    /**
     * 获取费用列表ID
     *
     * @param moduleId
     * @param moduleType
     */
    Long selectTabulationIdByModule(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);

    /**
     * 自有车费用列表-简单列表
     *
     * @param driverId
     * @param limitNum
     * @param userType
     * @return
     */
    List<CarCostTabulationSimpleVO> selectByDriverId(@Param("driverId") Long driverId, @Param("userType") Integer userType, @Param("limitNum") Integer limitNum);

    /**
     * 自有车费用列表-列表
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CarCostTabulationVO> selectByPage(Page page, @Param("queryForm") CarCostTabulationQueryForm queryForm);

}
