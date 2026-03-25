package net.lab1024.tms.admin.module.business.oa.invoice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceCreateForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceQueryForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceUpdateForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.oa.invoice.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * OA发票信息
 *
 * @author lihaifan
 * @date 2022/6/23 17:31
 */
@Service
@Slf4j
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private EnterpriseDao enterpriseDao;

    /**
     * 分页查询发票信息
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(InvoiceQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<InvoiceVO> invoiceVOS = invoiceDao.queryPage(page, queryDTO);
        PageResult<InvoiceVO> pageResult = SmartPageUtil.convert2PageResult(page, invoiceVOS);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<InvoiceVO>> queryList(Long enterpriseId) {
        InvoiceQueryForm queryForm = new InvoiceQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setDisabledFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<InvoiceVO> invoiceList = invoiceDao.queryPage(null, queryForm);
        return ResponseDTO.ok(invoiceList);
    }

    /**
     * 查询发票信息详情
     *
     * @param invoiceId
     * @return
     */
    public ResponseDTO<InvoiceVO> getDetail(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceVO invoiceVO = invoiceDao.getDetail(invoiceId, Boolean.FALSE);
        if (Objects.isNull(invoiceVO)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发票信息不存在");
        }
        return ResponseDTO.ok(invoiceVO);
    }

    /**
     * 新建发票信息
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createInvoice(InvoiceCreateForm createVO) {
        Long enterpriseId = createVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceDao.queryByAccountNumber(enterpriseId, createVO.getAccountNumber(), null, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发票信息账号重复");
        }
        // 数据插入
        InvoiceEntity insertInvoice = SmartBeanUtil.copy(createVO, InvoiceEntity.class);
        invoiceDao.insert(insertInvoice);
        return ResponseDTO.ok();
    }

    /**
     * 编辑发票信息
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateInvoice(InvoiceUpdateForm updateVO) {
        Long enterpriseId = updateVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "企业不存在");
        }
        Long invoiceId = updateVO.getInvoiceId();
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceDao.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发票信息不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceDao.queryByAccountNumber(updateVO.getEnterpriseId(), updateVO.getAccountNumber(), invoiceId, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发票信息账号重复");
        }
        // 数据编辑
        InvoiceEntity updateInvoice = SmartBeanUtil.copy(updateVO, InvoiceEntity.class);
        invoiceDao.updateById(updateInvoice);
        return ResponseDTO.ok();
    }


    /**
     * 删除发票信息
     *
     * @param invoiceId
     * @return
     */
    public ResponseDTO<String> deleteInvoice(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceDao.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "发票信息不存在");
        }
        invoiceDao.deleteInvoice(invoiceId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
