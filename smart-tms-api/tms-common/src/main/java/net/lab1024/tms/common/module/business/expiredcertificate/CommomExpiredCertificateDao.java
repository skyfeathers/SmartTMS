package net.lab1024.tms.common.module.business.expiredcertificate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 到期证件 dao
 *
 * @author Turbolisten
 * @date 2022/7/19 11:03
 */
@Mapper
@Component
public interface CommomExpiredCertificateDao extends BaseMapper<ExpiredCertificateEntity> {

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
}
