/*
 * @Description: file content
 * @Author: baijuhui
 * @Date: 2023-10-18 14:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const vehicleMaintenanceApi = {

  // 新建保养信息 @author zhaoxinyang
  create: (param) => {
    return postRequest('/maintenance/add', param);
  },

  // 保养信息删除 @author zhaoxinyang
  delete: (maintenanceId) => {
    return getRequest(`/maintenance/delete/${maintenanceId}`);
  },

  // 分页查询保养信息 @author zhaoxinyang
  queryPage: (param) => {
    return postRequest('/maintenance/page/query', param);
  },

  // 编辑保养信息 @author zhaoxinyang
  update: (param) => {
    return postRequest('/maintenance/update', param);
  },

};
