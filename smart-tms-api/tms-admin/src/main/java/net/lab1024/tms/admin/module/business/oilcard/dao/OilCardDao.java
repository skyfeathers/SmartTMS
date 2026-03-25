package net.lab1024.tms.admin.module.business.oilcard.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardQueryForm;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardSimpleVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/***
 * 油卡管理
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:33
 */
@Mapper
@Component
public interface OilCardDao extends BaseMapper<OilCardEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<OilCardVO> queryByPage(Page page, @Param("queryForm") OilCardQueryForm queryForm);

    BigDecimal querySummary(@Param("queryForm") OilCardQueryForm queryForm);

    /**
     * 更新删除标识
     *
     * @param list
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("list") List<Long> list, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询详情
     *
     * @param oilCardId
     * @return
     */
    OilCardVO getDetail(@Param("oilCardId") Long oilCardId);


    /**
     * 卡号查询
     *
     * @param oilCardNo
     * @param deletedFlag
     * @return
     */
    List<OilCardEntity> selectByNoExcludeIds(@Param("oilCardNo") String oilCardNo, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据主卡卡号更新禁用状态
     *
     * @param masterOilCardId
     * @param disabledFlag
     * @param deletedFlag
     */
    void updateDisabledFlagByMasterOilId(@Param("masterOilCardId") Long masterOilCardId, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据主卡查询副卡
     *
     * @param masterOilCardId
     * @param deletedFlag
     * @return
     */
    List<OilCardEntity> selectByMasterOilCardId(@Param("masterOilCardId") Long masterOilCardId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 修改预分配金额
     *
     * @param changeAmount
     * @param oilCardId
     */
    void updatePreDistributionBalance(@Param("changeAmount") BigDecimal changeAmount, @Param("oilCardId") Long oilCardId);


    List<OilCardEntity> selectByTypeAndEnterprise(@Param("type") Integer type, @Param("excludeEnterpriseIdList") List<Long> excludeEnterpriseIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据卡号查询
     *
     * @param oilCardNoList
     * @param deletedFlag
     * @param masterOilCardId 主卡ID。可为空，注意️：为空可能导致查出主卡、副卡卡号一样的两条数据
     * @param excludeMasterOilCardId 不包含的主卡ID，可为空。注意️：为空可能导致查出主卡、副卡卡号一样的两条数据
     * @return
     */
    List<OilCardEntity> selectByOilCardNo(@Param("oilCardNoList") Collection<String> oilCardNoList, @Param("deletedFlag") Boolean deletedFlag, @Param("masterOilCardId") Long masterOilCardId, @Param("excludeMasterOilCardId") Long excludeMasterOilCardId);

    /**
     * 根据车辆ID查询绑定油卡
     *
     * @param vehicleId
     * @param deletedFlag
     * @return
     */
    List<OilCardSimpleVO> queryListByVehicleId(@Param("vehicleId") Long vehicleId, @Param("type") Integer type, @Param("fixedPointFlag") Boolean fixedPointFlag, @Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 修改油卡余额
     *
     * @param moduleId
     * @param changeAmount
     */
    void updateBalance(@Param("moduleId") Long moduleId, @Param("changeAmount") BigDecimal changeAmount);

    /**
     * 获取油卡燃料类型
     *
     * @param oilCardId
     * @return
     */
    Integer selectFuelType(@Param("oilCardId") Long oilCardId);
}