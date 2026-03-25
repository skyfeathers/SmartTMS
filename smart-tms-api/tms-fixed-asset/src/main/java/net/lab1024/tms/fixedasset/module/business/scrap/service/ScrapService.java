package net.lab1024.tms.fixedasset.module.business.scrap.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Collections;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.employee.CommonEmployeeDao;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.scrap.constant.AssetScrapStatusEnum;
import net.lab1024.tms.fixedasset.module.business.scrap.dao.ScrapDao;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.entity.ScrapEntity;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapAddForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapQueryForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapAssetVO;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 固定资产-报废 Service
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Service
public class ScrapService {

    @Autowired
    private ScrapDao scrapDao;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private CommonEmployeeDao commonEmployeeDao;

    @Autowired
    private AssetService assetService;
    @Autowired
    private ScrapDataTracerService scrapDataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ScrapVO> queryPage(ScrapQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrapVO> list = scrapDao.queryPage(page, queryForm);

        if (list.size() > 0) {
            List<Long> scrapIdList = list.stream().map(ScrapVO::getScrapId).collect(Collectors.toList());
            List<ScrapAssetVO> assetList = scrapDao.selectRelateAssetList(scrapIdList);
            Map<Long, List<ScrapAssetVO>> scrapMap = assetList.stream().collect(Collectors.groupingBy(ScrapAssetVO::getScrapId));

            Set<Long> enterpriseIdSet = list.stream().map(ScrapVO::getEnterpriseId).collect(Collectors.toSet());
            List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
            Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
            for (ScrapVO scrapVO : list) {
                scrapVO.setEnterpriseName(enterpriseNameMap.getOrDefault(scrapVO.getEnterpriseId(), StringConst.EMPTY));
                List<ScrapAssetVO> assetVOS = scrapMap.get(scrapVO.getScrapId());
                if (scrapMap == null) {
                    scrapVO.setAssetList(Lists.newArrayList());
                } else {
                    scrapVO.setAssetList(assetVOS);
                }
            }
        }

        PageResult<ScrapVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    /**
     * 添加
     */

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(ScrapAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        ScrapEntity scrapEntity = SmartBeanUtil.copy(addForm, ScrapEntity.class);
        scrapEntity.setStatus(AssetScrapStatusEnum.AUDIT.getValue());
        scrapEntity.setApplyUserId(dataTracerRequestForm.getOperatorId());

        String scrapCode = serialNumberService.generate(SerialNumberIdEnum.ASSET_SCRAP);
        scrapEntity.setScrapCode(scrapCode);

        scrapDao.insert(scrapEntity);

        if (!Collections.isEmpty(addForm.getAssetIdList())) {
            Long scrapId = scrapEntity.getScrapId();
            scrapDao.batchInsertScrapAsset(scrapId, addForm.getAssetIdList());
        }
        ScrapVO scrapVO = getDetail(scrapEntity.getScrapId());
        scrapDataTracerService.saveLog(addForm, scrapVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 批量审核
     *
     * @return
     */
    public ResponseDTO<String> batchAudit(List<Long> scrapIdList, AssetScrapStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<ScrapEntity> scrapEntityList = scrapDao.selectBatchIds(scrapIdList);
        List<Long> filterIdList = scrapEntityList.stream()
                .filter(e -> AssetScrapStatusEnum.AUDIT.getValue().equals(e.getStatus()))
                .map(ScrapEntity::getScrapId)
                .collect(Collectors.toList());

        if (filterIdList.size() > 0) {
            scrapDao.batchUpdateStatus(filterIdList, status.getValue());
            scrapDataTracerService.batchAuditLog(scrapIdList, status, dataTracerRequestForm);
            if (AssetScrapStatusEnum.PASS.equalsValue(status.getValue())) {
                List<ScrapAssetVO> assetList = scrapDao.selectRelateAssetList(filterIdList);
                assetService.batchUpdateCancel(assetList, scrapEntityList, dataTracerRequestForm);
            }
        }
        return ResponseDTO.ok();
    }


    /**
     * 获取详情
     *
     * @param scrapId
     * @return
     */
    public ScrapVO getDetail(Long scrapId) {
        ScrapEntity scrapEntity = scrapDao.selectById(scrapId);
        ScrapVO detailVO = SmartBeanUtil.copy(scrapEntity, ScrapVO.class);
        EmployeeEntity employeeEntity = commonEmployeeDao.selectById(detailVO.getApplyUserId());
        detailVO.setApplyUserName(employeeEntity != null ? employeeEntity.getActualName() : "");
        detailVO.setAssetList(scrapDao.selectRelateAssetList(Arrays.asList(scrapId)));
        EnterpriseEntity enterpriseEntity = commonEnterpriseDao.selectById(scrapEntity.getEnterpriseId());
        detailVO.setEnterpriseName(null != enterpriseEntity ? enterpriseEntity.getEnterpriseName() : StringConst.EMPTY);
        return detailVO;
    }
}
