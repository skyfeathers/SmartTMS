package net.lab1024.tms.admin.module.business.oilcard.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.oilcard.dao.BalanceRecordDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceRecordQueryForm;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardBalanceRecordExportVO;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.OilCardBalanceRecordVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 交易记录操作
 *
 * @author lidoudou
 * @date 2022/6/30 下午2:47
 */
@Service
public class OilCardBalanceRecordService {

    @Autowired
    private OilCardDao oilCardDao;

    @Autowired
    private BalanceRecordDao balanceRecordDao;
    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<OilCardBalanceRecordVO>> queryByPage(OilCardBalanceRecordQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OilCardBalanceRecordVO> list = balanceRecordDao.queryByPage(page, queryForm);
        this.buildRelatedRecord(list);
        PageResult<OilCardBalanceRecordVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResultDTO);
    }

    private void buildRelatedRecord(List<OilCardBalanceRecordVO> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        List<Long> relatedRecordIdList = list.stream().filter(e->e.getRelatedRecordId() != null).map(OilCardBalanceRecordVO::getRelatedRecordId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(relatedRecordIdList)){
            return;
        }
        List<OilCardBalanceRecordVO> balanceRecordList = balanceRecordDao.selectByRecordIdList(relatedRecordIdList);
        if(CollectionUtils.isEmpty(balanceRecordList)){
            return;
        }
        Map<Long,OilCardBalanceRecordVO> balanceRecordMap = balanceRecordList.stream().collect(Collectors.toMap(OilCardBalanceRecordVO::getBalanceRecordId, Function.identity()));
        for (OilCardBalanceRecordVO oilCardBalanceRecordVO : list) {
            Long relatedRecordId = oilCardBalanceRecordVO.getRelatedRecordId();
            if(relatedRecordId == null){
                continue;
            }
            OilCardBalanceRecordVO relatedRecord = balanceRecordMap.get(relatedRecordId);
            if(relatedRecord == null){
                continue;
            }
            oilCardBalanceRecordVO.setRelatedOilCardNo(relatedRecord.getOilCardNo());
        }
    }

    /**
     * 导出
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<OilCardBalanceRecordExportVO>> queryByExport(OilCardBalanceRecordQueryForm queryForm) {
        List<OilCardBalanceRecordVO> searchList = balanceRecordDao.queryByPage(null, queryForm);
        if (CollectionUtils.isEmpty(searchList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        //关联记录信息
        Map<Long, OilCardBalanceRecordVO> balanceRecordMap = Maps.newHashMap();
        List<Long> relatedRecordIdList = searchList.stream().filter(e -> e.getRelatedRecordId() != null).map(OilCardBalanceRecordVO::getRelatedRecordId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(relatedRecordIdList)) {
            List<OilCardBalanceRecordVO> balanceRecordList = balanceRecordDao.selectByRecordIdList(relatedRecordIdList);
            balanceRecordMap = balanceRecordList.stream().collect(Collectors.toMap(OilCardBalanceRecordVO::getBalanceRecordId, Function.identity()));

        }
        //当前是否查询的主卡记录
        Boolean masterFlag = Boolean.TRUE;
        //导出列表
        List<OilCardBalanceRecordExportVO> exportVOList = Lists.newArrayList();
        for (OilCardBalanceRecordVO recordVO : searchList) {
            OilCardBalanceRecordExportVO exportVO = new OilCardBalanceRecordExportVO();
            exportVO.setCreateTime(recordVO.getCreateTime());
            exportVO.setBalanceRecordNo(recordVO.getBalanceRecordNo());
            exportVO.setRecordTypeDesc(recordVO.getRecordTypeDesc());
            exportVO.setDealTime(recordVO.getTransactionTime());
            exportVO.setRemark(recordVO.getRemark());
            if (masterFlag) {
                exportVO.setOilCardNo(recordVO.getOilCardNo());
                exportVO.setChangeBalance(recordVO.getChangeBalance());
                exportVO.setAfterBalance(recordVO.getAfterBalance());
            } else {
                exportVO.setSlaveOilCardNo(recordVO.getOilCardNo());
                exportVO.setSlaveChangeBalance(recordVO.getChangeBalance());
                exportVO.setSlaveAfterBalance(recordVO.getAfterBalance());
            }
            Long relatedRecordId = recordVO.getRelatedRecordId();
            if (relatedRecordId == null) {
                exportVOList.add(exportVO);
                continue;
            }
            //关联信息
            OilCardBalanceRecordVO relatedRecord = balanceRecordMap.get(relatedRecordId);
            if (relatedRecord == null) {
                continue;
            }
            if (masterFlag) {
                exportVO.setSlaveOilCardNo(relatedRecord.getOilCardNo());
                exportVO.setSlaveChangeBalance(relatedRecord.getChangeBalance());
                exportVO.setSlaveAfterBalance(relatedRecord.getAfterBalance());
            } else {
                exportVO.setOilCardNo(relatedRecord.getOilCardNo());
                exportVO.setChangeBalance(relatedRecord.getChangeBalance());
                exportVO.setAfterBalance(relatedRecord.getAfterBalance());
            }
            exportVOList.add(exportVO);
        }
        return ResponseDTO.ok(exportVOList);
    }
}
