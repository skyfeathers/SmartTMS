package net.lab1024.tms.admin.module.business.material.contracttype;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.material.contracttype.domain.*;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.contracttype.ContractTypeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Service
public class ContractTypeService {

    @Autowired
    private ContractTypeDao contractTypeDao;

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<ContractTypeVO>> query(ContractTypeQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<ContractTypeVO> contractTypeVOList = contractTypeDao.queryPage(page, queryDTO);
        PageResult<ContractTypeVO> pageResult = SmartPageUtil.convert2PageResult(page, contractTypeVOList);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 删除
     *
     * @param contractTypeId
     * @return
     */
    public ResponseDTO<String> delete(Long contractTypeId) {
        contractTypeDao.updateDeleteFlag(contractTypeId, Boolean.TRUE);
        return ResponseDTO.ok();
    }

    /**
     * 保存或更新
     * @param form
     * @return
     */
    public ResponseDTO<String> save(ContractTypeForm form) {

        List<ContractTypeEntity> contractTypeEntityList = contractTypeDao.selectByNameAndExcludeIds(form.getName(), null, false);
        if(CollectionUtils.isNotEmpty(contractTypeEntityList)){
            return ResponseDTO.userErrorParam(String.format("【%s】名称已经存在", form.getName()));
        }
        ContractTypeEntity contractTypeEntity = SmartBeanUtil.copy(form, ContractTypeEntity.class);
        contractTypeDao.insert(contractTypeEntity);
        return ResponseDTO.ok();
    }

    /**
     * 保存或更新
     * @param form
     * @return
     */
    public ResponseDTO<String> update(ContractTypeUpdateForm form) {

        // 名称重复校验
        List<ContractTypeEntity> contractTypeEntityList = contractTypeDao.selectByNameAndExcludeIds(form.getName(), Lists.newArrayList(form.getContractTypeId()), false);
        if(CollectionUtils.isNotEmpty(contractTypeEntityList)){
            return ResponseDTO.userErrorParam(String.format("【%s】名称已经存在", form.getName()));
        }
        ContractTypeEntity contractTypeEntity = SmartBeanUtil.copy(form, ContractTypeEntity.class);
        contractTypeDao.updateById(contractTypeEntity);
        return ResponseDTO.ok();
    }


    public ResponseDTO<List<ContractListVO>> queryAll() {
        List<ContractListVO> contractTypeVOList = contractTypeDao.queryAll(false);
        return ResponseDTO.ok(contractTypeVOList);
    }
}
