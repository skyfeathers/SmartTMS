package net.lab1024.tms.admin.module.business.material.customsbroker;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerQueryForm;
import net.lab1024.tms.admin.module.business.material.customsbroker.domain.CustomsBrokerVO;
import net.lab1024.tms.common.module.business.material.customsbroker.CustomsBrokerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务资料-报关行
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Mapper
@Component
public interface CustomsBrokerDao extends BaseMapper<CustomsBrokerEntity> {

    /**
     * 根据业务代码查询
     *
     * @param customsBrokerCode
     * @param excludeCustomsBrokerId
     * @param deletedFlag
     * @return
     */
    CustomsBrokerEntity queryByCustomsBrokerCode(@Param("customsBrokerCode") String customsBrokerCode, @Param("excludeCustomsBrokerId") Long excludeCustomsBrokerId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除报关行
     *
     * @param customsBrokerId
     * @param deletedFlag
     */
    void deleteCustomsBroker(@Param("customsBrokerId") Long customsBrokerId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 报关行分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CustomsBrokerVO> queryPage(Page page, @Param("queryForm") CustomsBrokerQueryForm queryForm);

    /**
     * 查询报关行详情
     *
     * @param customsBrokerId
     * @return
     */
    CustomsBrokerVO getDetail(@Param("customsBrokerId") Long customsBrokerId, @Param("deletedFlag") Boolean deletedFlag);
}
