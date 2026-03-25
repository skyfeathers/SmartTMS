import { postRequest } from '/@/lib/axios';

export const departmentBusinessApi = {
  // 部门业绩目标-查询 by listen
  queryDeptSalesTargetList (param) {
    return postRequest('dept/salesTarget/query', param);
  },
  // 部门业绩目标-更新 by listen
  updateDeptSalesTarget (param) {
    return postRequest('/dept/salesTarget/update', param);
  },
};
