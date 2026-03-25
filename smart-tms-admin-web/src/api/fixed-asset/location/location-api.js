import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const locationApi = {

  // 新建位置 @author lidoudou
  create: (param) => {
    return postRequest('/location/create', param);
  },

  // 删除位置 @author lidoudou
  delete: (yardId) => {
    return getRequest(`/location/delete/${yardId}`);
  },

  // 查询位置详情 @author lidoudou
  detail: (yardId) => {
    return getRequest(`/location/get/${yardId}`);
  },

  // 分页查询位置模块 @author lidoudou
  pageQuery: (param) => {
    return postRequest('/location/page/query', param);
  },

  // 编辑位置 @author lidoudou
  update: (param) => {
    return postRequest('/location/update', param);
  },

  // 查询列表 @author yandy
  queryList: (enterpriseId) => {
    let param = '';
    if (enterpriseId) {
      param = `enterpriseId=${enterpriseId}`;
    }
    return getRequest(`/location/query/list?${param}`);
  }
};
