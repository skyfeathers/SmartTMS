/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-18
 * @LastEditTime: 2022-07-18
 * @LastEditors: zhuoda
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const costItemApi = {
  // 查询全部 @author zhuoda
  query: (param) => {
    return postRequest('/costItem/query', param);
  },
  // 删除 @author zhuoda
  delete: (costItemId) => {
    return getRequest(`/costItem/delete/${costItemId}`);
  },
  // 保存 @author zhuoda
  save: (param) => {
    return postRequest('/costItem/save', param);
  },

  // 保存 @author zhuoda
  update: (param) => {
    return postRequest('/costItem/update', param);
  },
};
