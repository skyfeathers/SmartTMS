/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-22
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const configApi = {
  // 分页查询 by zhuoda
  queryList: (param) => {
    return postRequest('/support/config/query', param);
  },
  // 添加配置参数 by zhuoda
  addConfig: (param) => {
    return postRequest('/support/config/add', param);
  },
  // 修改配置参数 by zhuoda
  updateConfig: (param) => {
    return postRequest('/support/config/update', param);
  },
  // 查询配置详情 by zhuoda
  queryByKey: (param) => {
    return getRequest(`/support/config/queryByKey?configKey=${param}`);
  },
};
