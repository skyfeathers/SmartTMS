/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const cabinetApi = {

  // 新建柜型 @author lihaifan
  create: (param) => {
    return postRequest('/cabinet/create', param);
  },

  // 删除柜型 @author lihaifan
  delete: (yardId) => {
    return getRequest(`/cabinet/delete/${yardId}`);
  },

  // 查询柜型详情 @author lihaifan
  detail: (yardId) => {
    return getRequest(`/cabinet/get/${yardId}`);
  },

  // 分页查询柜型模块 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/cabinet/page/query', param);
  },

  // 编辑柜型 @author lihaifan
  update: (param) => {
    return postRequest('/cabinet/update', param);
  },

  // 查询列表 @author yandy
  queryList: () => {
    return getRequest('/cabinet/query/list');
  }
};
