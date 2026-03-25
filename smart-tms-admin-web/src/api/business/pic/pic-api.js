import { postRequest, getRequest } from '/@/lib/axios';

export const picApi = {
  // 添加
  add: (param) => {
    return postRequest('/pic/add', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/pic/update', param);
  },
  // 查询
  query: (param) => {
    return postRequest(`/pic/query`,param);
  },
  // 启用/禁用 @author zhuo
  picEnable: (picId) => {
    return getRequest(`/pic/enable/${picId}`);
  },

};
