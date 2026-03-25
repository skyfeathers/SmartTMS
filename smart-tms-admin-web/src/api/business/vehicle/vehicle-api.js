/*
 * @Description:车辆api
 * @Author: zhuoda
 * @Date: 2022-07-08
 * @LastEditTime: 2022-07-11
 * @LastEditors: zhuoda
 */
import { download, getRequest, postRequest } from '/@/lib/axios';

export const vehicleApi = {
  // 查询所有有效车辆 @author zhuoda
  getAll: (driverId) => {
    let params = '';
    if (driverId) {
      params = `?driverId=${driverId}`;
    }
    return getRequest(`/vehicle/getAll${params}`);
  },
  // 查询所有有效车辆 @author zhuoda
  getCarCostVehicleList: () => {
    return getRequest(`/vehicle/carCostVehicleList`);
  },
  // 查询NFT的车辆下拉框 by lidoudou
  queryNftVehicleList: () => {
    return getRequest('/vehicle/nft/select');
  },
  // 分页查询车辆 @author lidoudou
  query: (param) => {
    return postRequest('/vehicle/page/query', param);
  },
  // 车辆详情 @author lidoudou
  getDetail: (vehicleId) => {
    return getRequest(`/vehicle/detail/${vehicleId}`);
  },
  // 新建车辆 @author lidoudou
  save: (param) => {
    return postRequest('/vehicle/save', param);
  },
  // 编辑车辆 @author lidoudou
  update: (param) => {
    return postRequest('/vehicle/update', param);
  },
  // 车辆删除 @author lidoudou
  batchDelete: (param) => {
    return postRequest('/vehicle/batchDelete', param);
  },
  // 车辆启用 @author lidoudou
  batchEnabled: (param) => {
    return postRequest('/vehicle/batchEnabled', param);
  },
  // 车辆禁用 @author lidoudou
  batchDisabled: (param) => {
    return postRequest('/vehicle/batchDisabled', param);
  },
  // 批量审核 @author lidoudou
  batchAudit: (param) => {
    return postRequest('/vehicle/batch/audit', param);
  },
  // 批量修改负责人 by lidoudou
  batchUpdateManager: (param) => {
    return postRequest('/vehicle/manager/update', param);
  },
  businessModeUpdate: (param) => {
    return postRequest('/vehicle/businessMode/update', param);
  },
  // 快速创建司机、车辆、挂车
  quickCreate: (params) => {
    return postRequest('/vehicle/quick/save', params);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    download(fileName, '/vehicle/export', params);
  },
};
