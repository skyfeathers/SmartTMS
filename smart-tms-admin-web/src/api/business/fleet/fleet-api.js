import {getRequest, postRequest} from "/@/lib/axios";

export const fleetApi = {
    // 批量审核 by lidoudou
    batchAuditFleet: (param) => {
        return postRequest('/fleet/audit', param);
    },
    // 车队删除 @author lidoudou
    batchDeleteFleet: (params) => {
        return postRequest('/fleet/batch/delete',params);
    },
    // 车队详情 @author lidoudou
    getFleet: (fleetId) => {
        return getRequest(`/fleet/detail/${fleetId}`);
    },
    // 分页查询车队 @author lidoudou
    queryFleet: (param) => {
      return postRequest('/fleet/page/query', param);
    },
    // 查询车队列表 @author lidoudou
    queryList: () => {
      return getRequest('/fleet/query/list');
    },
    // 新建车队 @author lidoudou
    saveFleet: (param) => {
        return postRequest('/fleet/save', param);
    },
    // 编辑车队 @author lidoudou
    updateFleet: (param) => {
        return postRequest('/fleet/update', param);
    },
    // 分页查询司机 @author lidoudou
    queryFleetDriver: (param) => {
        return postRequest('/fleet/driver/page/query', param);
    },
    // 根据车队查询司机不分页列表 @author lidoudou
    queryFleetDriverList: (fleetId) => {
      return getRequest(`/fleet/driver/query/list/${fleetId}`);
    },
    // 分页查询车辆 @author lidoudou
    queryFleetVehicle: (param) => {
        return postRequest('/fleet/vehicle/page/query', param);
    },
    // 添加关联模块 @author lidoudou
    addFleetItem: (param) => {
        return postRequest('/fleet/item/add', param);
    },
    // 删除关联信息 @author lidoudou
    deleteFleetItem: (fleetItemId) => {
        return getRequest(`/fleet/item/delete/${fleetItemId}`);
    },
    // 批量修改负责人 by lidoudou
    batchUpdateManager: (param) => {
      return postRequest('/fleet/manager/update', param);
    },
    // 银行列表 @author lidoudou
    bankList: (fleetId) => {
        return getRequest(`/fleet/bankList/${fleetId}`);
    },
    // 银行添加@author lidoudou
    bankAdd: (param) => {
        return postRequest('/fleet/bank/add', param);
    },
}
