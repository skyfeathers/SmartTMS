/*
 * @Description: 业务资料-维修地点
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-14 10:25:50
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-19 08:42:04
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const repairPlantApi = {

  // 维修厂家-分页查询 @listen
  queryRepairPlantList (params) {
    return postRequest('/information/repairPlant/query', params)
  },

  // 维修厂家-新增 @listen
  addRepairPlant (params) {
    return postRequest('/information/repairPlant/add', params)
  },

  // 维修厂家-更新 @listen
  updateRepairPlant (params) {
    return postRequest('/information/repairPlant/update', params)
  },

  // 维修厂家-删除 @listen
  delRepairPlant (repairPlantId) {
    return getRequest(`/information/repairPlant/del/${repairPlantId}`)
  } ,

  // 维修厂家-维修记录
  queryRepairPlant (params) {
    return postRequest(`/repair/page/query`,params)
  },
  // 维修厂家-维修金额总计
  getRepairPlantAmountSum (repairPlantId) {
    return getRequest(`/repair/amount/sum/${repairPlantId}`)
  }
}