package net.lab1024.tms.common.module.support.datatracer.service;

import cn.hutool.http.useragent.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.dao.DataTracerDao;
import net.lab1024.tms.common.module.support.datatracer.domain.*;
import net.lab1024.tms.common.module.support.datatracer.manager.DataTracerManger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class DataTracerService {

    @Autowired
    private DataTracerDao dataTracerDao;
    @Autowired
    private DataTracerManger dataTracerManger;

    /**
     * 保存操作日志
     *
     * @param dataTracerForm
     * @param operatorId    操作人id
     * @param operatorName  操作人名称
     * @param operatorType 操作人类型
     */
    public void saveOperateRecord(DataTracerForm dataTracerForm, Long operatorId, String operatorName, Integer operatorType) {
        DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerForm, operatorId, operatorName, operatorType);
        dataTracerDao.insert(dataTracerEntity);
    }

    /**
     * 批量保存
     *
     * @param dataTracerList
     * @param operatorId
     * @param operatorName
     * @param operatorType
     */
    public void saveBatchOperateRecord(List<DataTracerForm> dataTracerList, Long operatorId, String operatorName, Integer operatorType) {
        if (CollectionUtils.isEmpty(dataTracerList)) {
            return;
        }
        List<DataTracerEntity> recordEntityList = Lists.newArrayList();
        for (DataTracerForm dataTracerForm : dataTracerList) {
            DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerForm, operatorId, operatorName, operatorType);
            recordEntityList.add(dataTracerEntity);
        }
        dataTracerManger.saveBatch(recordEntityList);
    }

    /**
     * dto 转实体对象
     *
     * @param dataTracerForm
     * @param operatorId
     * @param operatorName
     * @return
     */
    private DataTracerEntity convertEntity(DataTracerForm dataTracerForm, Long operatorId, String operatorName, Integer operatorType) {
        DataTracerEntity recordEntity = new DataTracerEntity();
        recordEntity.setBusinessId(dataTracerForm.getBusinessId());
        recordEntity.setBusinessType(dataTracerForm.getBusinessType().getValue());
        recordEntity.setBusinessTypeDesc(dataTracerForm.getBusinessType().getDesc());
        recordEntity.setOperateType((Integer) dataTracerForm.getOperateType().getValue());
        recordEntity.setOperateTypeDesc(dataTracerForm.getOperateType().getDesc());
        recordEntity.setOperateContent(dataTracerForm.getOperateContent());
        recordEntity.setIp(dataTracerForm.getIp());
        recordEntity.setUserAgent(dataTracerForm.getUserAgent());
        recordEntity.setOperatorId(operatorId);
        recordEntity.setOperatorName(operatorName);
        recordEntity.setOperatorType(operatorType);
        recordEntity.setOperatorTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(operatorType, UserTypeEnum.class));
        if (dataTracerForm.getExtraData() != null) {
            recordEntity.setExtraData(JSON.toJSONString(dataTracerForm.getExtraData()));
        }
        if (dataTracerForm.getDiff() != null) {
            recordEntity.setDiff(JSON.toJSONString(dataTracerForm.getDiff()));
        }
        recordEntity.setCreateTime(dataTracerForm.getOperateTime());
        return recordEntity;
    }


    /**
     * 查询某个业务的所有的日志信息
     *
     * @param businessId
     * @param orderType
     * @return
     */
    public List<DataTracerVO> selectRecord(Long businessId, DataTracerBusinessTypeEnum orderType) {
        return dataTracerDao.selectRecord(businessId, orderType.getValue());
    }

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DataTracerVO>> query(DataTracerQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DataTracerVO> list = dataTracerDao.query(page, queryForm);
        list.forEach(e->this.buildDataTracer(e));
        PageResult<DataTracerVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    private void buildDataTracer(DataTracerVO dataTracerVO) {
        String userAgentStr = dataTracerVO.getUserAgent();
        if (StringUtils.isBlank(userAgentStr)) {
            return;
        }
        // userAgent parse
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        OS os = userAgent.getOs();
        if (os != null) {
            dataTracerVO.setOs(os.getName());
        }
        Browser browser = userAgent.getBrowser();
        if (browser != null) {
            dataTracerVO.setBrowser(browser.getName());
        }
        Platform platform = userAgent.getPlatform();
        if (platform != null) {
            dataTracerVO.setPlatform(platform.getName());
        }
    }



    public ResponseDTO<DataTracerDetailVO> extData(Long dataTracerId) {
        DataTracerEntity dataTracerEntity = dataTracerDao.selectById(dataTracerId);
        if(dataTracerEntity == null){
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if(StringUtils.isBlank(dataTracerEntity.getExtraData())){
            return ResponseDTO.ok();
        }
        DataTracerDetailVO dataTracerDetailVO = SmartBeanUtil.copy(dataTracerEntity,DataTracerDetailVO.class);
        this.buildDataTracer(dataTracerDetailVO);
        try {
            JSONObject jsonObject = JSON.parseObject(dataTracerEntity.getExtraData());
            Class clazz = Class.forName(jsonObject.getString("objectClass"));
            String originJson = jsonObject.getString("originObject");
            String newJson = jsonObject.getString("newObject");

            dataTracerDetailVO.setOriginObject(JSONObject.parseObject(originJson, clazz));
            dataTracerDetailVO.setNewObject(JSONObject.parseObject(newJson, clazz));
        }catch (Exception e){

        }
        return ResponseDTO.ok(dataTracerDetailVO);

    }
}
