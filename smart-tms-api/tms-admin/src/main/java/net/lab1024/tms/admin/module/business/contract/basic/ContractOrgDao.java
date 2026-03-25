package net.lab1024.tms.admin.module.business.contract.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.contract.domain.ContractOrgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 合同签署企业
 *
 * @author lihaifan
 * @date 2022/9/26 09:55
 */
@Component
@Mapper
public interface ContractOrgDao extends BaseMapper<ContractOrgEntity> {

    /**
     * 根据合同ID获取
     * @param contractId
     * @return
     */
    ContractOrgEntity getByContractId(@Param("contractId") Long contractId);
}
