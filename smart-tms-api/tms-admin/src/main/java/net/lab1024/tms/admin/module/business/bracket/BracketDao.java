package net.lab1024.tms.admin.module.business.bracket;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.bracket.domain.form.BracketQueryForm;
import net.lab1024.tms.admin.module.business.bracket.domain.vo.BracketListVO;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
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
public interface BracketDao extends BaseMapper<BracketEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<BracketListVO> queryByPage(Page page, @Param("queryForm") BracketQueryForm queryForm);

    /**
     * 更新删除标识
     *
     * @param bracketIdList
     * @param deletedFlag
     * @param updateUserId
     * @return
     */
    Integer updateDeletedFlagById(@Param("bracketIdList") List<Long> bracketIdList, @Param("deletedFlag") Boolean deletedFlag, @Param("updateUserId") Long updateUserId);

    /**
     * 根据状态以及ID查询
     *
     * @param bracketIdList
     * @param auditStatus
     * @param deletedFlag
     * @return
     */
    List<BracketEntity> selectList(@Param("list") List<Long> bracketIdList, @Param("auditStatus") Integer auditStatus, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 批量更新审核状态
     *
     * @param bracketIdList
     * @param auditStatus
     * @param auditRemark
     * @return
     */
    Integer batchUpdateAuditStatus(@Param("list") List<Long> bracketIdList, @Param("auditStatus") Integer auditStatus, @Param("auditRemark") String auditRemark);

    /**
     * 名称查询
     *
     * @param bracketNo
     * @param deletedFlag
     * @return
     */
    List<BracketEntity> selectByNoExcludeIds(@Param("enterpriseId")Long enterpriseId, @Param("bracketNo") String bracketNo, @Param("excludeIds") Collection<Long> excludeIds, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 批量修改负责人
     * @param bracketIdList
     * @param managerId
     */
    void batchUpdateManager(@Param("list")List<Long> bracketIdList, @Param("managerId") Long managerId);
}
