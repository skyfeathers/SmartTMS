import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetDepreciationApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/depreciation/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/depreciation/add', param);
  },
  getDetail: (depreciationId) => {
    return getRequest(`/depreciation/detail/${depreciationId}`);
  },
  // 作废  @author lidoudou
  cancel: (depreciationId) => {
    return getRequest(`/depreciation/cancel/${depreciationId}`);
  },
  queryAsset: (params) => {
    return postRequest('/depreciation/asset/query', params);
  }
};
