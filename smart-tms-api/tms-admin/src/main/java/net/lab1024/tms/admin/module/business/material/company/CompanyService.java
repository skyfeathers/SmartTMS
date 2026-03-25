package net.lab1024.tms.admin.module.business.material.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyCreateForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyQueryForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyUpdateForm;
import net.lab1024.tms.admin.module.business.material.company.domain.CompanyVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.company.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 业务资料-公司管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    /**
     * 分页查询公司模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<CompanyVO>> queryByPage(CompanyQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<CompanyVO> companyVOS = companyDao.queryPage(page, queryDTO);
        PageResult<CompanyVO> pageResult = SmartPageUtil.convert2PageResult(page, companyVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询公司详情
     *
     * @param companyId
     * @return
     */
    public ResponseDTO<CompanyVO> getDetail(Long companyId) {
        // 校验公司是否存在
        CompanyVO companyDetail = companyDao.getDetail(companyId, Boolean.FALSE);
        if (Objects.isNull(companyDetail)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "公司不存在");
        }
        return ResponseDTO.ok(companyDetail);
    }

    /**
     * 新建公司
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createCompany(CompanyCreateForm createVO) {
        // 验证公司编号是否重复
        CompanyEntity validateCompany = companyDao.queryByCompanyCode(createVO.getCompanyCode(), null, Boolean.FALSE);
        if (Objects.nonNull(validateCompany)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "公司编号重复");
        }
        // 数据插入
        CompanyEntity insertCompany = SmartBeanUtil.copy(createVO, CompanyEntity.class);
        companyDao.insert(insertCompany);
        return ResponseDTO.ok();
    }

    /**
     * 编辑公司
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateCompany(CompanyUpdateForm updateVO) {
        Long companyId = updateVO.getCompanyId();
        // 校验公司是否存在
        CompanyEntity companyDetail = companyDao.selectById(companyId);
        if (Objects.isNull(companyDetail) || companyDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "公司不存在");
        }
        // 验证公司编号是否重复
        CompanyEntity validateCompany = companyDao.queryByCompanyCode(updateVO.getCompanyCode(), companyId, Boolean.FALSE);
        if (Objects.nonNull(validateCompany)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "公司编号重复");
        }
        // 数据编辑
        CompanyEntity updateCompany = SmartBeanUtil.copy(updateVO, CompanyEntity.class);
        companyDao.updateById(updateCompany);
        return ResponseDTO.ok();
    }


    /**
     * 删除公司
     *
     * @param companyId
     * @return
     */
    public ResponseDTO<String> deleteCompany(Long companyId) {
        // 校验公司是否存在
        CompanyEntity companyDetail = companyDao.selectById(companyId);
        if (Objects.isNull(companyDetail) || companyDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "公司不存在");
        }
        companyDao.deleteCompany(companyId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
