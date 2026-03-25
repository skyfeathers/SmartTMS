package net.lab1024.tms.admin.module.business.flow.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.flow.constant.FlowHandlerTypeEnum;
import net.lab1024.tms.admin.module.business.flow.custom.IFlowCustomTaskHandlerService;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [ 流程处理人 ]
 *
 * @author yandanyang
 * @date 2021/8/17 11:30
 */
@Slf4j
@Service
public class FlowHandlerConfigService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired(required = false)
    private Map<String, IFlowCustomTaskHandlerService> flowCustomTaskHandlerServiceMap;

    /**
     * 获取处理人
     *
     * @param initiatorId 发起人id
     * @param config      配置信息
     * @return
     */
    public List<Long> getFlowHandler(Long initiatorId, Long enterpriseId, String config, Object businessData) {
        FlowHandlerConfigBO handlerConfig = JSON.parseObject(config, FlowHandlerConfigBO.class);
        Integer handlerType = handlerConfig.getHandlerType();
        //处理人为所有人
        if (FlowHandlerTypeEnum.ALL.equalsValue(handlerType)) {
            return employeeDao.getEmployeeId(enterpriseId, false, false);
        }
        if (FlowHandlerTypeEnum.ASSIGN_USER.equalsValue(handlerType)) {
            return handlerConfig.getConfigIdList();
        }
        if (FlowHandlerTypeEnum.ASSIGN_ROLE.equalsValue(handlerType)) {
            return this.getHandlerByRole(handlerConfig.getConfigIdList());
        }
        if (FlowHandlerTypeEnum.ASSIGN_DEPARTMENT.equalsValue(handlerType)) {
            return this.getHandlerByDepartment(handlerConfig.getConfigIdList());
        }
        if (FlowHandlerTypeEnum.INITIATOR_MANAGER.equalsValue(handlerType)) {
            return this.getManagerByInitiatorId(initiatorId);
        }
        if (FlowHandlerTypeEnum.CUSTOM_SERVICE.equalsValue(handlerType)) {
            return this.getCustomHandler(initiatorId, enterpriseId, businessData, handlerConfig.getServiceName());
        }
        return Lists.newArrayList();
    }

    /**
     * 自定义任务处理人
     *
     * @param initiatorId
     * @param serviceName
     * @return
     */
    private List<Long> getCustomHandler(Long initiatorId, Long enterpriseId, Object businessData, String serviceName) {
        if (StringUtils.isBlank(serviceName)) {
            log.warn("[custom handler] service name cannot be null!");
            return Lists.newArrayList();
        }
        IFlowCustomTaskHandlerService customTaskHandlerService = applicationContext.getBean(serviceName, IFlowCustomTaskHandlerService.class);
        if (customTaskHandlerService == null) {
            log.warn("[custom handler] service name {} cannot found!", serviceName);
            return Lists.newArrayList();
        }
        return customTaskHandlerService.customHandler(initiatorId, enterpriseId, businessData);
    }

    /**
     * 获取角色下所有人
     *
     * @param configIdList
     * @return
     */
    private List<Long> getHandlerByRole(List<Long> configIdList) {
        if (CollectionUtils.isEmpty(configIdList)) {
            return Lists.newArrayList();
        }
        List<Long> employeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(configIdList);
        return employeeIdList;
    }

    /**
     * 获取部门下所有人
     *
     * @param configIdList
     * @return
     */
    private List<Long> getHandlerByDepartment(List<Long> configIdList) {
        if (CollectionUtils.isEmpty(configIdList)) {
            return Lists.newArrayList();
        }
        List<Long> employeeIdList = employeeDao.getEmployeeIdByDepartmentIdList(configIdList, false);
        return employeeIdList;
    }

    /**
     * 获取发起人部门主管
     *
     * @param initiatorId
     * @return
     */
    private List<Long> getManagerByInitiatorId(Long initiatorId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(initiatorId);
        if (employeeEntity == null) {
            return Lists.newArrayList();
        }
        DepartmentEntity departmentEntity = departmentDao.selectById(employeeEntity.getDepartmentId());
        if (departmentEntity == null) {
            return Lists.newArrayList();
        }
        if (departmentEntity.getManagerId() == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(departmentEntity.getManagerId());
    }

    /**
     * 获取处理人名称
     *
     * @param handlerIdList
     * @return
     */
    public List<String> getHandlerNameList(List<Long> handlerIdList) {
        if (CollectionUtils.isEmpty(handlerIdList)) {
            return Lists.newArrayList();
        }
        List<EmployeeVO> employeeList = employeeDao.getEmployeeByIds(handlerIdList);
        return employeeList.stream().map(EmployeeVO::getActualName).collect(Collectors.toList());
    }

    /**
     * 检查流程处理人配置
     *
     * @param flowConfig
     * @return
     */
    public ResponseDTO<String> checkFlowHandlerConfig(String flowConfig) {
        if (StrUtil.isBlank(flowConfig)) {
            return ResponseDTO.userErrorParam("审批流程配置不能为空");
        }
        ResponseDTO<FlowHandlerConfigBO> parseResp = this.parseConfig(flowConfig, FlowHandlerConfigBO.class);
        if (!parseResp.getOk()) {
            return ResponseDTO.userErrorParam("审批流程配置格式错误");
        }
        FlowHandlerConfigBO flowHandlerConfigBO = parseResp.getData();
        if (flowHandlerConfigBO == null) {
            return ResponseDTO.userErrorParam("审批流程配置不能为空");
        }
        Integer handlerType = flowHandlerConfigBO.getHandlerType();
        if (handlerType == null || !SmartBaseEnumUtil.checkEnum(handlerType, FlowHandlerTypeEnum.class)) {
            return ResponseDTO.userErrorParam("审批流程配置处理人类型错误");
        }
        List<Long> configIdList = flowHandlerConfigBO.getConfigIdList();
        if ((FlowHandlerTypeEnum.ASSIGN_USER.equalsValue(handlerType)
                || FlowHandlerTypeEnum.ASSIGN_DEPARTMENT.equalsValue(handlerType)
                || FlowHandlerTypeEnum.ASSIGN_ROLE.equalsValue(handlerType))
                && CollUtil.isEmpty(configIdList)) {
            return ResponseDTO.userErrorParam("审批流程配置处理人类型配置错误");
        }
        // 校验自定义服务
        if (FlowHandlerTypeEnum.CUSTOM_SERVICE.equalsValue(handlerType)) {
            String serviceName = flowHandlerConfigBO.getServiceName();
            if (StrUtil.isBlank(serviceName)) {
                return ResponseDTO.userErrorParam("审批流程配置处理人类型自定义服务不能为空");
            }
            if (flowCustomTaskHandlerServiceMap == null) {
                return ResponseDTO.userErrorParam("审批流程配置处理人类型自定义服务不存在");
            }
            System.out.println(flowCustomTaskHandlerServiceMap.keySet());
            IFlowCustomTaskHandlerService customTaskHandlerService = flowCustomTaskHandlerServiceMap.get(serviceName);
            if (customTaskHandlerService == null) {
                return ResponseDTO.userErrorParam("审批流程配置处理人类型自定义服务不存在");
            }
        }

        return ResponseDTO.ok();
    }

    public <T> ResponseDTO<T> parseConfig(String flowConfig, Class<T> clazz) {
        try {
            T configBO = JSON.parseObject(flowConfig, clazz);
            return ResponseDTO.ok(configBO);
        } catch (Exception e) {
            return ResponseDTO.userErrorParam();
        }
    }
}
