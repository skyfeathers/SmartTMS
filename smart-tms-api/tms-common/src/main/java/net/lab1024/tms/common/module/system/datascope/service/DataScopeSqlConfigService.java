package net.lab1024.tms.common.module.system.datascope.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.constant.SystemConst;
import net.lab1024.tms.common.module.system.datascope.DataScope;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeConstant;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import net.lab1024.tms.common.module.system.datascope.domain.DataScopeSqlConfig;
import net.lab1024.tms.common.module.system.datascope.strategy.AbstractDataScopeStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sql配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class DataScopeSqlConfigService {



    private final ConcurrentHashMap<String, DataScopeSqlConfig> dataScopeMethodMap = new ConcurrentHashMap<>();

    @Resource
    private DataScopeViewService dataScopeViewService;

    @Resource
    private ApplicationContext applicationContext;


    @PostConstruct
    private void initDataScopeMethodMap() {
        this.refreshDataScopeMethodMap();
    }

    /**
     * 刷新 所有添加数据范围注解的接口方法配置<class.method,DataScopeSqlConfigDTO></>
     */
    private Map<String, DataScopeSqlConfig> refreshDataScopeMethodMap() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(SystemConst.COMPONENT_SCAN)).setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(DataScope.class);
        for (Method method : methods) {
            DataScope dataScopeAnnotation = method.getAnnotation(DataScope.class);
            if (dataScopeAnnotation != null) {
                DataScopeSqlConfig configDTO = new DataScopeSqlConfig();
                configDTO.setDataScopeType(dataScopeAnnotation.dataScopeType());
                configDTO.setJoinSql(dataScopeAnnotation.joinSql());
                configDTO.setWhereIndex(dataScopeAnnotation.whereIndex());
                configDTO.setDataScopeWhereInType(dataScopeAnnotation.whereInType());
                configDTO.setParamName(dataScopeAnnotation.paramName());
                configDTO.setJoinSqlImplClazz(dataScopeAnnotation.joinSqlImplClazz());
                dataScopeMethodMap.put(method.getDeclaringClass().getSimpleName() + "." + method.getName(), configDTO);
            }
        }
        return dataScopeMethodMap;
    }

    /**
     * 根据调用的方法获取，此方法的配置信息
     *
     */
    public DataScopeSqlConfig getSqlConfig(String method) {
        return this.dataScopeMethodMap.get(method);
    }

    /**
     * 组装需要拼接的sql
     */
    public String getJoinSql(Map<String, Object> paramMap, DataScopeSqlConfig sqlConfigDTO) {
        DataScopeTypeEnum dataScopeTypeEnum = sqlConfigDTO.getDataScopeType();
        String joinSql = sqlConfigDTO.getJoinSql();
        Long employeeId = SmartRequestUtil.getRequestUserId();
        Long enterpriseId = SmartRequestEnterpriseUtil.getRequestEnterpriseId();
        if (employeeId == null) {
            return "";
        }
        if (DataScopeWhereInTypeEnum.CUSTOM_STRATEGY == sqlConfigDTO.getDataScopeWhereInType()) {
            Class strategyClass = sqlConfigDTO.getJoinSqlImplClazz();
            if (strategyClass == null) {
                log.warn("data scope custom strategy class is null");
                return "";
            }
            AbstractDataScopeStrategy powerStrategy = (AbstractDataScopeStrategy) applicationContext.getBean(sqlConfigDTO.getJoinSqlImplClazz());
            if (powerStrategy == null) {
                log.warn("data scope custom strategy class：{} ,bean is null", sqlConfigDTO.getJoinSqlImplClazz());
                return "";
            }
            return powerStrategy.getCondition(enterpriseId, employeeId, paramMap, sqlConfigDTO);
        }
        if (DataScopeWhereInTypeEnum.EMPLOYEE == sqlConfigDTO.getDataScopeWhereInType()) {
            List<Long> canViewEmployeeIds = dataScopeViewService.getCanViewEmployeeId(dataScopeTypeEnum, employeeId);
            if (CollectionUtils.isEmpty(canViewEmployeeIds)) {
                return "";
            }
            String employeeIds = StringUtils.join(canViewEmployeeIds, ",");
            String sql = joinSql.replaceAll(DataScopeConstant.EMPLOYEE_PARAM, employeeIds);
            return sql;
        }
        if (DataScopeWhereInTypeEnum.DEPARTMENT == sqlConfigDTO.getDataScopeWhereInType()) {
            List<Long> canViewDepartmentIds = dataScopeViewService.getCanViewDepartmentId(dataScopeTypeEnum, employeeId);
            if (CollectionUtils.isEmpty(canViewDepartmentIds)) {
                return "";
            }
            String departmentIds = StringUtils.join(canViewDepartmentIds, ",");
            String sql = joinSql.replaceAll(DataScopeConstant.DEPARTMENT_PARAM, departmentIds);
            return sql;
        }
        return "";
    }
}
