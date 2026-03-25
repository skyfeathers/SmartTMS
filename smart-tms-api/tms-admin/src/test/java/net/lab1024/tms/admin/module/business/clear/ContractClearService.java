package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.contract.basic.ContractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/30 9:03 上午
 */
@Service
public class ContractClearService {

    @Autowired
    private ContractDao contractDao;

    public void contractClear(List<Long> enterpriseIdList){
        QueryWrapper searchQw = new QueryWrapper();
        searchQw.notIn("enterprise_id", enterpriseIdList);
        contractDao.delete(searchQw);
    }
}