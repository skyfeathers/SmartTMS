package net.lab1024.tms.common.module.support.dingding.sync.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Mapper
@Component
public interface DingDingEmployeeDao extends BaseMapper<DingDingEmployeeEntity> {


    /**
     * 根据所属公司以及员工查询
     *
     * @param enterpriseId
     * @param userId
     * @return
     */
    DingDingEmployeeEntity selectByEnterpriseUserId(@Param("enterpriseId") Long enterpriseId, @Param("userId") String userId);

    /**
     *
     * @param userIdList
     * @return
     */
    List<DingDingEmployeeEntity> selectByUserIdList(@Param("userIdList") Collection<String> userIdList);
}
