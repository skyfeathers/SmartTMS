/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const yardApi = {

  // 新建堆场 @author lihaifan
  create: (param) => {
    return postRequest('/yard/create', param);
  },

  // 删除堆场 @author lihaifan
  delete: (yardId) => {
    return getRequest(`/yard/delete/${yardId}`);
  },

  // 查询堆场详情 @author lihaifan
  detail: (yardId) => {
    return getRequest(`/yard/get/${yardId}`);
  },

  // 分页查询堆场模块 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/yard/page/query', param);
  },

  // 编辑堆场 @author lihaifan
  update: (param) => {
    return postRequest('/yard/update', param);
  },

  // 分页查询堆场模块 @author lihaifan
  list: () => {
    return getRequest('/yard/list');
  },

};
