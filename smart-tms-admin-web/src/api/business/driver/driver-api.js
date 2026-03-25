import { download, getRequest, postRequest } from '/@/lib/axios';

export const driverApi = {
  // 审核 by lidoudou
  auditDriver: (param) => {
    return postRequest('/driver/audit', param);
  },
  // 批量审核 by lidoudou
  batchAuditDriver: (param) => {
    return postRequest('/driver/batch/audit', param);
  },
  // 司机删除 @author lidoudou
  batchDeleteDriver: (params) => {
    return postRequest('/driver/batch/delete', params);
  },
  // 司机详情 @author lidoudou
  getDriver: (driverId) => {
    return getRequest(`/driver/detail/${driverId}`);
  },
  // 分页查询司机 @author lidoudou
  queryDriver: (param) => {
    return postRequest('/driver/page/query', param);
  },
  // 新建司机 @author lidoudou
  saveDriver: (param) => {
    return postRequest('/driver/save', param);
  },
  // 编辑司机 @author lidoudou
  updateDriver: (param) => {
    return postRequest('/driver/update', param);
  },
  // 更新状态 @author lidoudou
  updateDisableFlag: (driverId) => {
    return getRequest(`/driver/updateDisableFlag/${driverId}`);
  },
  // 批量更新状态 @author lihaifan
  batchUpdateDisableFlag: (param) => {
    return postRequest('/driver/batchUpdateDisableFlag', param);
  },
  // 司机下拉框查询 @author lihaifan
  queryDriverSelect: (vehicleId) => {
    let params = '';
    if (vehicleId) {
      params = `?vehicleId=${vehicleId}`;
    }
    return getRequest(`/driver/select${params}`);
  },
  // 查询NFT的司机下拉框 by lidoudou
  queryNftDriverSelect: () => {
    return getRequest('/driver/nft/select');
  },
  // 批量修改负责人 by lidoudou
  batchUpdateManager: (param) => {
    return postRequest('/driver/manager/update', param);
  },
  businessModeUpdate: (param) => {
    return postRequest('/driver/businessMode/update', param);
  },
  // 银行列表 @author lidoudou
  bankList: (driverId) => {
    return getRequest(`/driver/bankList/${driverId}`);
  },
  // 银行添加@author lidoudou
  bankAdd: (param) => {
    return postRequest('/driver/bank/add', param);
  },
  // 查询是否有重名的司机
  queryByName: (driverName) => {
    return getRequest(`/driver/query/name?driverName=${driverName}`);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    download(fileName, '/driver/export', params);
  },
};
