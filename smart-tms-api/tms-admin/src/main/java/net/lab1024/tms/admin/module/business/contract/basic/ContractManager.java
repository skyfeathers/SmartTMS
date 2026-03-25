package net.lab1024.tms.admin.module.business.contract.basic;

import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.contract.domain.ContractOrgEntity;
import net.lab1024.tms.common.module.business.contract.domain.ContractSignerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 合同管理
 *
 * @author lihaifan
 * @date 2022/9/25 17:58
 */
@Service
public class ContractManager {

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractSignerDao contractSignerDao;
    @Autowired
    private ContractOrgDao contractOrgDao;


    /**
     * 新建合同
     * @param createEntity
     * @param contractSignerEntity
     * @param contractOrgEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void insertContract(ContractEntity createEntity, ContractSignerEntity contractSignerEntity, ContractOrgEntity contractOrgEntity){
        contractDao.insert(createEntity);
        Integer contractId = createEntity.getContractId();
        if(contractSignerEntity != null){
            contractSignerEntity.setContractId(contractId);
            contractSignerDao.insert(contractSignerEntity);
        }
        if(contractOrgEntity != null){
            contractOrgEntity.setContractId(contractId);
            contractOrgDao.insert(contractOrgEntity);
        }
    }
}
