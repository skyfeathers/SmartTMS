package net.lab1024.tms.admin.module.business.oilcard.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardEnterpriseVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Mapper
@Component
public interface OilCardEnterpriseDao extends BaseMapper<OilCardEnterpriseEntity> {

    /**
     * 根据油卡删除
     *
     * @param oilCardId
     */
    void deleteByOilCardId(@Param("oilCardId") Long oilCardId);

    /**
     * 根据油卡ID批量删除
     *
     * @param oilCardIdList
     */
    void deleteByOilCardIdList(@Param("oilCardIdList") List<Long> oilCardIdList);

    /**
     * 根据油卡查询
     *
     * @param oilCardId
     * @return
     */
    List<EnterpriseVO> selectByOilCardId(@Param("oilCardId") Long oilCardId);

    /**
     * 根据油卡查询
     *
     * @param oilCardIdList
     * @return
     */
    List<OilCardEnterpriseVO> selectByOilCardIdList(@Param("oilCardIdList") Collection<Long> oilCardIdList);
}