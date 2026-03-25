package net.lab1024.tms.job.module.contract;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ContractDao extends BaseMapper<ContractEntity> {

    /**
     * 查询过期时间大于当前天的
     *
     * @return
     */
    List<ContractEntity> selectByExpireDate();
}
