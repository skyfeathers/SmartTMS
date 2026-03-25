/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const businessTypeApi = {

  // 新建业务类型 @author lihaifan
  create: (param) => {
    return postRequest('/businessType/create', param);
  },

  // 删除业务类型 @author lihaifan
  delete: (businessTypeId) => {
    return getRequest(`/businessType/delete/${businessTypeId}`);
  },

  // 查询业务类型详情 @author lihaifan
  detail: (businessTypeId) => {
    return getRequest(`/businessType/get/${businessTypeId}`);
  },

  // 分页查询业务类型模块 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/businessType/page/query', param);
  },

  // 编辑业务类型 @author lihaifan
  update: (param) => {
    return postRequest('/businessType/update', param);
  },

  // 查询业务类型列表 @author yandy
  queryList: () => {
    return getRequest('/businessType/query/list');
  }
};
