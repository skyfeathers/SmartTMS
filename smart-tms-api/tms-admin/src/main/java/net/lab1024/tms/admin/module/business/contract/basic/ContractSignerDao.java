package net.lab1024.tms.admin.module.business.contract.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.contract.domain.ContractSignerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 合同签署人
 *
 * @author lihaifan
 * @date 2022/9/26 09:54
 */
@Component
@Mapper
public interface ContractSignerDao extends BaseMapper<ContractSignerEntity> {

    /**
     * 根据合同ID获取
     *
     * @param contractId
     * @return
     */
    ContractSignerEntity getByContractId(@Param("contractId") Long contractId);
}
