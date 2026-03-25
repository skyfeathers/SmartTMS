package net.lab1024.tms.admin.module.business.expiredcertificate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateQueryForm;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateVO;
import net.lab1024.tms.admin.module.business.reportform.home.domain.HomeExpireCertVO;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 到期证件 dao
 *
 * @author Turbolisten
 * @date 2022/7/19 11:03
 */
@Mapper
@Component
public interface ExpiredCertificateDao extends BaseMapper<ExpiredCertificateEntity> {

    List<HomeExpireCertVO> moduleStatusNum(@Param("enterpriseId") Long enterpriseId);

    /**
     * 根据模块类型查询
     *
     * @param moduleType
     * @param moduleId
     * @param type
     * @return
     */
    ExpiredCertificateEntity selectByModule(@Param("moduleType") Integer moduleType,
                                            @Param("moduleId") Long moduleId,
                                            @Param("type") Integer type);

    /**
     * 分页查询到期证件列表
     *
     * @param page
     * @param query
     * @return
     */
    List<ExpiredCertificateVO> query(Page page, @Param("query") ExpiredCertificateQueryForm query);


    /**
     * 删除某个模块所有的
     * @param moduleType
     * @param moduleId
     */
    void deleteByModuleId(@Param("moduleType") Integer moduleType,
                          @Param("moduleId") Long moduleId);
}
