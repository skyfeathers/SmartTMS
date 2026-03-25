/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const repairPlantApi = {

// 维修厂家-新增 @listen
  create: (param) => {
    return postRequest('/information/repairPlant/add', param);
  },

  // 维修厂家-删除 @listen
  delete: (repairPlantId) => {
    return getRequest(`/information/repairPlant/del/${repairPlantId}`);
  },


  // 维修厂家-分页查询 @listen
  pageQuery: (param) => {
    return postRequest('/information/repairPlant/query', param);
  },

  // 维修厂家-更新 @listen
  update: (param) => {
    return postRequest('/information/repairPlant/update', param);
  },

  // 维修厂家-查询全部 @listen
  queryAll: (param) => {
    return getRequest('/information/repairPlant/query/all', param);
  },

};
