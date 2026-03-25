package net.lab1024.tms.admin.module.business.etc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcQueryForm;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcVO;
import net.lab1024.tms.common.module.business.etc.domain.EtcEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/***
 * etc管理
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:33
 */
@Mapper
@Component
public interface EtcDao extends BaseMapper<EtcEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EtcVO> queryByPage(Page page, @Param("queryForm") EtcQueryForm queryForm);

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
     * @param etcId
     * @return
     */
    EtcVO getDetail(@Param("etcId") Long etcId);

    /**
     * 批量根据etc号查询
     * @param etcNoList
     * @param deletedFlag
     * @return
     */
    List<EtcEntity> selectByEtcNoList(@Param("enterpriseId") Long enterpriseId, @Param("etcNoList") Collection<String> etcNoList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据etc号查询
     * @param etcNo
     * @param deletedFlag
     * @return
     */
    EtcEntity selectByEtcNo(@Param("enterpriseId") Long enterpriseId, @Param("etcNo") String etcNo, @Param("deletedFlag") Boolean deletedFlag);

}