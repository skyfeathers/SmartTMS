package net.lab1024.tms.admin.module.business.etc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.etc.dao.EtcBalanceRecordDao;
import net.lab1024.tms.admin.module.business.etc.dao.EtcDao;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcBalanceRecordQueryForm;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcRechargeForm;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcBalanceRecordVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.etc.domain.EtcBalanceRecordEntity;
import net.lab1024.tms.common.module.business.etc.domain.EtcEntity;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易记录操作
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:47
 */
@Service
public class EtcBalanceRecordService {

    @Autowired
    private EtcDao etcDao;

    @Autowired
    private EtcBalanceRecordDao etcBalanceRecordDao;

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<EtcBalanceRecordVO>> queryByPage(EtcBalanceRecordQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<EtcBalanceRecordVO> insuranceList = etcBalanceRecordDao.queryByPage(page, queryForm);
        PageResult<EtcBalanceRecordVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, insuranceList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * etc充值
     *
     * @param rechargeForm
     * @param requestUserId
     * @return
     */
    public ResponseDTO<String> recharge(EtcRechargeForm rechargeForm, Long requestUserId) {
        Long etcId = rechargeForm.getEtcId();
        EtcEntity dbEtcEntity = etcDao.selectById(etcId);
        if (null == dbEtcEntity || dbEtcEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "ETC不存在，请刷新后再试～");
        }
        String balanceRecordNo = serialNumberService.generate(SerialNumberIdEnum.ETC);
        EtcBalanceRecordEntity etcBalanceRecordEntity = new EtcBalanceRecordEntity();
        etcBalanceRecordEntity.setBalanceRecordNo(balanceRecordNo);
        etcBalanceRecordEntity.setEtcId(etcId);
        etcBalanceRecordEntity.setIncomeFlag(Boolean.TRUE);
        etcBalanceRecordEntity.setChangeBalance(rechargeForm.getRechargeAmount());
        etcBalanceRecordEntity.setRechargeTime(rechargeForm.getRechargeTime());
        etcBalanceRecordEntity.setRemark(rechargeForm.getRemark());
        etcBalanceRecordEntity.setCreateUserId(requestUserId);
        etcBalanceRecordDao.insert(etcBalanceRecordEntity);
        return ResponseDTO.ok();
    }
}
