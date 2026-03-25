package net.lab1024.tms.common.module.support.dingding.constants;

/**
 * 钉钉接口URL定义
 *
 * @author lidoudou
 * @date 2023/4/20 下午3:49
 */
public class DingDingUrlConst {

    /**
     * 根据userid获取用户详情
     */
    public static final String USER_GET = "https://oapi.dingtalk.com/topapi/v2/user/get";

    /**
     * 获取下一级部门基础信息
     * 本接口只支持获取当前部门的下一级部门基础信息，不支持获取当前部门下所有层级子部门。
     */
    public static final String DEPARTMENT_LIST_SUB = "https://oapi.dingtalk.com/topapi/v2/department/listsub";

    /**
     * 调用本接口获取指定部门中的用户详细信息。
     * 本接口只支持获取指定部门下的员工详情信息，子部门员工信息获取不到。
     */
    public static final String USER_LIST_BY_DEPT_ID = "https://oapi.dingtalk.com/topapi/v2/user/list";

    /**
     * 根据部门ID获取指定部门详情。
     */
    public static final String GET_DEPARTMENT = "https://oapi.dingtalk.com/topapi/v2/department/get";
}
