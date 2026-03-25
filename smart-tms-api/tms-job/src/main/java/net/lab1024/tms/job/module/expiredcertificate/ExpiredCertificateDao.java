package net.lab1024.tms.job.module.expiredcertificate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    /**
     * 查询即将过期的证件
     *
     * @return
     */
    List<ExpiredCertificateEntity> queryNeedUpdateStatus(@Param("status0") Integer status0,
                                                         @Param("status3") Integer status3,
                                                         @Param("status7") Integer status7,
                                                         @Param("status15") Integer status15,
                                                         @Param("status30") Integer status30);

    /**
     * 货主负责人、创建人
     * @param shipperId
     * @return
     */
    ExpiredCertificationModuleEmployeeBO selectShipperEmployeeId(@Param("shipperId") Long shipperId);

    /**
     * 货主负责人、创建人
     * @param driverId
     * @return
     */
    ExpiredCertificationModuleEmployeeBO selectDriverEmployeeId(@Param("driverId") Long driverId);

    /**
     * 货主负责人、创建人
     * @param vehicleId
     * @return
     */
    ExpiredCertificationModuleEmployeeBO selectVehicleEmployeeId(@Param("vehicleId") Long vehicleId);

    /**
     * 货主负责人、创建人
     * @param bracketId
     * @return
     */
    ExpiredCertificationModuleEmployeeBO selectBracketEmployeeId(@Param("bracketId") Long bracketId);
}
