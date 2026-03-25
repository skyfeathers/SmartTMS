/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const repairApi = {

  // 新建维修信息 @author lidoudou
  create: (param) => {
    return postRequest('/repair/save', param);
  },

  // 维修信息删除 @author lidoudou
  delete: (repairId) => {
    return getRequest(`/repair/delete/${repairId}`);
  },

  // 分页查询维修信息 @author lidoudou
  queryPage: (param) => {
    return postRequest('/repair/page/query', param);
  },

  // 编辑维修信息 @author lidoudou
  update: (param) => {
    return postRequest('/repair/update', param);
  },

};
