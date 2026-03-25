import { getRequest, postRequest, request } from '/@/lib/axios';

export const employeeBusinessApi = {
  // 员工业绩目标-查询 by lidoudou
  queryEmployeeSalesTargetList (params) {
    return postRequest('/employee/salesTarget/query', params);
  },
  // 员工业绩目标-更新 by lidoudou
  updateEmployeeSalesTarget (params) {
    return postRequest('/employee/salesTarget/update', params);
  },
  // 查询销售情况 by lidoudou
  querySalesList: (params) => {
    return postRequest('/employee/sale/query', params);
  }
};
