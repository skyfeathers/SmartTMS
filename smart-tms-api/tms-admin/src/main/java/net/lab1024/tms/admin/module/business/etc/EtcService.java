package net.lab1024.tms.admin.module.business.etc;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.etc.dao.EtcDao;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcAddForm;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcExcelImportForm;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcQueryForm;
import net.lab1024.tms.admin.module.business.etc.domain.form.EtcUpdateForm;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.etc.domain.EtcEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * ETC管理
 *
 * @author lidoudou
 * @date 2022/6/30 下午3:30
 */
@Service
public class EtcService {

    @Autowired
    private EtcDao etcDao;
    @Autowired
    private EtcDataTracerService etcDataTracerService;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EtcManager etcManager;


    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<EtcVO>> queryByPage(EtcQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<EtcVO> etcList = etcDao.queryByPage(page, queryForm);
        buildList(etcList);
        PageResult<EtcVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, etcList);
        return ResponseDTO.ok(pageResultDTO);
    }

    private void buildList(List<EtcVO> etcList){
        if(CollectionUtils.isEmpty(etcList)){
            return;
        }
        Set<Long> driverIdList = etcList.stream().filter(e -> e.getUseVehicleId() != null).map(EtcVO::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        List<DriverEntity> driverList = driverDao.selectBatchIds(driverIdList);
        Map<Long, String> driverNameMap = driverList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));
        etcList.forEach(item -> {
            item.setUserName(driverNameMap.getOrDefault(item.getUserId(), StringConst.EMPTY));
        });
    }

    /**
     * 查询详情
     *
     * @param etcId
     * @return
     */
    public EtcVO getDetail(Long etcId) {
        EtcVO etcVO = etcDao.getDetail(etcId);
        if (null == etcVO) {
            return null;
        }
        if (null != etcVO.getUserId()) {
            DriverEntity driverEntity = driverDao.selectById(etcVO.getUserId());
            etcVO.setUserName(null != driverEntity ? driverEntity.getDriverName() : StringConst.EMPTY);
            etcVO.setUserPhone(null != driverEntity ? driverEntity.getTelephone() : StringConst.EMPTY);
        }
        return etcVO;
    }

    /**
     * 新建
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> save(EtcAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        EtcEntity existEntity = etcDao.selectByEtcNo(addForm.getEnterpriseId(), addForm.getEtcNo(), false);
        if (existEntity != null) {
            return ResponseDTO.userErrorParam("ETC卡号重复");
        }
        EtcEntity etcEntity = SmartBeanUtil.copy(addForm, EtcEntity.class);
        etcEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        etcDao.insert(etcEntity);
        etcDataTracerService.saveLog(etcEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新etc
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> update(EtcUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long etcId = updateForm.getEtcId();
        EtcEntity dbEtcEntity = etcDao.selectById(etcId);
        if (null == dbEtcEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EtcEntity existEntity = etcDao.selectByEtcNo(updateForm.getEnterpriseId(), updateForm.getEtcNo(), false);
        if (existEntity != null && !existEntity.getEtcId().equals(etcId)) {
            return ResponseDTO.userErrorParam("ETC卡号重复");
        }
        EtcVO beforeData = getDetail(etcId);
        EtcEntity etcEntity = SmartBeanUtil.copy(updateForm, EtcEntity.class);
        etcDao.updateById(etcEntity);
        EtcVO afterData = getDetail(etcId);
        etcDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     *
     * @param etcIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateDeletedFlag(List<Long> etcIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(etcIdList)) {
            return ResponseDTO.ok();
        }
        List<EtcEntity> etcEntityList = etcDao.selectBatchIds(etcIdList);
        if (CollectionUtils.isEmpty(etcEntityList)) {
            return ResponseDTO.ok();
        }
        etcDao.updateDeletedFlag(etcIdList, Boolean.TRUE);
        etcDataTracerService.batchDeleteLog(etcIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * Excel导出
     * @param uploadFile
     * @return
     * @throws Exception
     */
    public ResponseDTO<String> excelImport(Long enterpriseId, MultipartFile uploadFile, DataTracerRequestForm dataTracerRequestForm) throws Exception{
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        List<EtcExcelImportForm> excelImportFormList = ExcelImportUtil.importExcel(uploadFile.getInputStream(), EtcExcelImportForm.class, params);
        if (CollectionUtils.isEmpty(excelImportFormList)) {
            return ResponseDTO.userErrorParam("没有需要导入的数据");
        }
        Optional<EtcExcelImportForm> importFormOptional = excelImportFormList.stream().filter(e-> StringUtils.isBlank(e.getEtcNo()) || StringUtils.isBlank(e.getPassword())).findFirst();
        if (importFormOptional.isPresent()) {
            return ResponseDTO.userErrorParam("ETC卡号、所属公司、密码不能为空");
        }
        //清理空格
        this.clearExcelDataBlank(excelImportFormList);
        //剔除重复ETC
        Set<String> etcNoList = excelImportFormList.stream().map(EtcExcelImportForm::getEtcNo).collect(Collectors.toSet());
        if (etcNoList.size() != excelImportFormList.size()) {
            return ResponseDTO.userErrorParam("EXCEL中存在重复的ETC卡号");
        }
        List<EtcEntity> existEntityList = etcDao.selectByEtcNoList(enterpriseId, etcNoList, false);
        if (CollectionUtils.isNotEmpty(existEntityList)) {
            Set<String> existList = existEntityList.stream().map(EtcEntity::getEtcNo).collect(Collectors.toSet());
            return ResponseDTO.userErrorParam("ETC卡号【" + StringUtils.join(existList, ",") + "】已存在");
        }
        // 司机信息
        ResponseDTO<Map<String, Long>> driverResp = this.driverMap(enterpriseId, excelImportFormList);
        if (!driverResp.getOk()) {
            return ResponseDTO.userErrorParam(driverResp.getMsg());
        }
        // 车辆信息
        ResponseDTO<Map<String, Long>> vehicleResp = this.vehicleMap(enterpriseId, excelImportFormList);
        if (!vehicleResp.getOk()) {
            return ResponseDTO.userErrorParam(vehicleResp.getMsg());
        }
        List<EtcEntity> etcEntityList = this.buildImportEntityList(enterpriseId, excelImportFormList, driverResp.getData(), vehicleResp.getData(), dataTracerRequestForm);
        etcManager.saveBatch(etcEntityList);
        etcDataTracerService.batchImportLog(etcEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private List<EtcEntity> buildImportEntityList(Long enterpriseId, List<EtcExcelImportForm> excelImportFormList, Map<String, Long> driverMap, Map<String, Long> vehicleMap,DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(excelImportFormList)) {
            return Lists.newArrayList();
        }
        List<EtcEntity> etcEntityList = Lists.newArrayList();
        for (EtcExcelImportForm importForm : excelImportFormList) {
            EtcEntity etcEntity = new EtcEntity();
            etcEntity.setEtcNo(importForm.getEtcNo());
            etcEntity.setEnterpriseId(enterpriseId);
            if (StringUtils.isNotBlank(importForm.getDriverTelephone())) {
                etcEntity.setUserId(driverMap.get(importForm.getDriverTelephone()));
                etcEntity.setUserName(importForm.getDriverName());
            }
            if (StringUtils.isNotBlank(importForm.getVehicleNumber())) {
                etcEntity.setUseVehicleId(vehicleMap.get(importForm.getVehicleNumber()));
            }
            etcEntity.setPassword(importForm.getPassword());
            etcEntity.setRemark(importForm.getRemark());
            etcEntity.setDisabledFlag(false);
            if (StringUtils.isNotBlank(importForm.getDisabledFlag()) && !"启用".equals(importForm.getDisabledFlag())){
                etcEntity.setDisabledFlag(true);
            }
            etcEntity.setDeletedFlag(false);
            etcEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
            etcEntityList.add(etcEntity);
        }
        return etcEntityList;
    }

    /**
     * 司机数据
     * @param excelImportFormList
     * @return
     */
    private ResponseDTO<Map<String, Long>> driverMap(Long enterpriseId, List<EtcExcelImportForm> excelImportFormList) {
        Set<String> driverTelephoneList = excelImportFormList.stream().filter(e->StringUtils.isNotBlank(e.getDriverTelephone())).map(EtcExcelImportForm::getDriverTelephone).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(driverTelephoneList)) {
            return ResponseDTO.ok(Maps.newHashMap());
        }
        List<DriverEntity> driverEntityList = driverDao.selectByPhone(enterpriseId, driverTelephoneList, false);
        List<String> searchList = driverEntityList.stream().map(DriverEntity::getTelephone).collect(Collectors.toList());
        if (searchList.size() != driverTelephoneList.size()){
            List<String> notExistList = driverTelephoneList.stream().filter(e->!searchList.contains(e)).collect(Collectors.toList());
            return ResponseDTO.userErrorParam("手机号【" + StringUtils.join(notExistList, ",") + "】对应的司机信息暂未找到");
        }
        Map<String,Long> resultMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getTelephone, DriverEntity::getDriverId));
        return ResponseDTO.ok(resultMap);
    }

    /**
     * 车辆数据
     * @param excelImportFormList
     * @return
     */
    private ResponseDTO<Map<String, Long>> vehicleMap(Long enterpriseId, List<EtcExcelImportForm> excelImportFormList) {
        Set<String> vehicleNumberList = excelImportFormList.stream().filter(e->StringUtils.isNotBlank(e.getVehicleNumber())).map(EtcExcelImportForm::getVehicleNumber).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(vehicleNumberList)) {
            return ResponseDTO.ok(Maps.newHashMap());
        }
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectByVehicleNumber(enterpriseId, vehicleNumberList, false);
        List<String> searchList = vehicleEntityList.stream().map(VehicleEntity::getVehicleNumber).collect(Collectors.toList());
        if (searchList.size() != vehicleNumberList.size()){
            List<String> notExistList = vehicleNumberList.stream().filter(e->!searchList.contains(e)).collect(Collectors.toList());
            return ResponseDTO.userErrorParam("车牌号【" + StringUtils.join(notExistList, ",") + "】对应的车辆信息暂未找到");
        }
        Map<String,Long> resultMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleNumber, VehicleEntity::getVehicleId));
        return ResponseDTO.ok(resultMap);
    }


    private void clearExcelDataBlank(List<EtcExcelImportForm> excelImportFormList) {
        for (EtcExcelImportForm importForm : excelImportFormList) {
            importForm.setEtcNo(importForm.getEtcNo().trim());
            importForm.setPassword(importForm.getPassword().trim());
            if (StringUtils.isNotBlank(importForm.getDriverName())){
                importForm.setDriverName(importForm.getDriverName().trim());
            }
            if (StringUtils.isNotBlank(importForm.getDriverTelephone())){
                importForm.setDriverTelephone(importForm.getDriverTelephone().trim());
            }
            if (StringUtils.isNotBlank(importForm.getVehicleNumber())){
                importForm.setVehicleNumber(importForm.getVehicleNumber().trim());
            }
            if (StringUtils.isNotBlank(importForm.getRemark())){
                importForm.setRemark(importForm.getRemark().trim());
            }
        }
    }
}
